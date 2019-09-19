package com.sgztech.rastreamento.view


import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.sgztech.rastreamento.R
import com.sgztech.rastreamento.adapter.TrackEventAdapter
import com.sgztech.rastreamento.api.RetrofitInitializer
import com.sgztech.rastreamento.core.CoreApplication
import com.sgztech.rastreamento.extension.*
import com.sgztech.rastreamento.model.PostalSearch
import com.sgztech.rastreamento.model.Track
import com.sgztech.rastreamento.model.TrackObject
import com.sgztech.rastreamento.util.CodeUtil.filter
import com.sgztech.rastreamento.util.CodeUtil.isValid
import com.sgztech.rastreamento.util.GoogleSignInUtil.getAccount
import com.sgztech.rastreamento.util.SnackBarUtil.show
import kotlinx.android.synthetic.main.fragment_track.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TrackFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_track, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSpinner()
        setupEtCode()
        setupFab()
        setupBtnTrack()
        loadTrackObjects()
        setupSwipe()
        setupAds()
    }

    private fun setupAds() {
        MobileAds.initialize(requireContext())
        val adRequest = AdRequest.Builder().addTestDevice(getString(R.string.test_device)).build()
        adView.loadAd(adRequest)
    }

    private fun setupSwipe() {
        swipe.setOnRefreshListener {
            if (isValid(etCode, textInputLayout)) {
                swipe.visible()
                recycler_view_events.gone()
                sendRequest(etCode.text.toString())
            } else {
                swipe.gone()
            }
        }
    }

    private fun setupBtnTrack() {
        btnTrack.setOnClickListener {
            requireActivity().hideKeyBoard(it)
            if(isValid(etCode, textInputLayout)){
                progressBar.visible()
                recycler_view_events.gone()
                sendRequest(etCode.text.toString())
            }
        }
    }

    private fun sendRequest(code: String) {
        val call = RetrofitInitializer().service().findObject(code)
        call.enqueue(object : Callback<PostalSearch> {
            override fun onResponse(
                call: Call<PostalSearch>,
                response: Response<PostalSearch>
            ) {
                progressBar.gone()
                swipe.isRefreshing = false
                val postalSearch = response.body()
                if (postalSearch == null) {
                    val errorBody = response.errorBody()
                    if(errorBody != null){
                        show(btnTrack, R.string.msg_search_error_not_found)
                    }else{
                        show(btnTrack, R.string.msg_search_error)
                    }
                } else {
                    loadDataOnView(postalSearch)
                    show(btnTrack, R.string.msg_search_sucess)
                }
            }

            override fun onFailure(call: Call<PostalSearch>, t: Throwable) {
                progressBar.gone()
                swipe.gone()
                show(btnTrack, R.string.msg_search_fail)
                t.message?.let {
                    showLog(it)
                }
            }
        })
    }

    private fun loadDataOnView(postalSearch: PostalSearch) {
        setupRecyclerView(postalSearch.data.tracks.toMutableList().asReversed())
    }

    private fun setupFab() {
        fab.setOnClickListener {
            startActivityForResult(
                requireActivity().getIntent(TrackObjectListActivity::class.java),
                REQUEST_CODE
            )
        }
    }

    private fun setupEtCode() {
        etCode.filters = filter(requireContext())
        etCode.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // unnecessary implementation
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // unnecessary implementation
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.length == 0) {
                    spinnerCodes.setSelection(0)
                }
            }
        })
    }

    private fun setupSpinner() {
        spinnerCodes.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val selectedObject = parent.getItemAtPosition(position).toString()
                if (selectedObject == getString(R.string.select_track_object)) {
                    etCode.setText("")
                } else {
                    val index = selectedObject.indexOf("-") + 2
                    val code = selectedObject.substring(index, selectedObject.length)
                    etCode.setText(code)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // unnecessary implementation
            }
        }
    }

    private fun loadTrackObjects() {
        GlobalScope.launch(context = Dispatchers.Main) {
            val trackObjectList = loadCodeList()
            val list = mutableListOf<String>()
            list.add(getString(R.string.select_track_object))
            for (trackObject in trackObjectList) {
                list.add(trackObject.name.plus(" - ").plus(trackObject.code))
            }
            spinnerCodes.adapter = ArrayAdapter(
                requireContext(),
                R.layout.track_object_list_item,
                list
            )
        }
    }

    private suspend fun loadCodeList(): MutableList<TrackObject> {
        val result = GlobalScope.async {
            val dao = CoreApplication.database?.trackObjectDao()
            dao?.allByUser(getUserId())
        }
        return result.await()?.toMutableList() ?: mutableListOf()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        data?.let {
            if (requestCode == REQUEST_CODE && data.getBooleanExtra(HAS_CHANGE, false)) {
                loadTrackObjects()
                recycler_view_events.gone()
            }
        }
    }

    private fun getUserId(): String {
        return getAccount(requireContext()).let {
            if (it != null && it.id != null) {
                it.id!!
            } else {
                ""
            }
        }
    }

    private fun setupRecyclerView(tracks: MutableList<Track>) {
        recycler_view_events.let {
            it.adapter = TrackEventAdapter(tracks)
            it.layoutManager = LinearLayoutManager(activity)
            it.setHasFixedSize(true)
        }
        recycler_view_events.visible()
    }

    companion object {
        const val REQUEST_CODE = 20
        const val HAS_CHANGE = "HAS_CHANGE"
    }
}
