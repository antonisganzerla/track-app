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
import com.sgztech.rastreamento.R
import com.sgztech.rastreamento.api.RetrofitInitializer
import com.sgztech.rastreamento.core.CoreApplication
import com.sgztech.rastreamento.extension.*
import com.sgztech.rastreamento.model.PostalSearch
import com.sgztech.rastreamento.model.TrackObject
import com.sgztech.rastreamento.util.GoogleSignInUtil.getAccount
import com.sgztech.rastreamento.util.SnackBarUtil.show
import com.sgztech.rastreamento.util.SnackBarUtil.showShort
import kotlinx.android.synthetic.main.event_card_view.view.*
import kotlinx.android.synthetic.main.fragment_track.*
import kotlinx.android.synthetic.main.postal_object_card_view.*
import kotlinx.android.synthetic.main.postal_object_card_view.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 */
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
    }

    private fun setupBtnTrack() {
        btnTrack.setOnClickListener {
            requireActivity().hideKeyBoard(it)
            val code = etCode.text.toString()
            if (code.isEmpty()) {
                showShort(it, R.string.msg_enter_code)
            } else {
                progressBar.visible()
                cardView.gone()
                sendRequest(code)
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
                val postalSearch = response.body()
                if (postalSearch == null || postalSearch.objeto.isEmpty() || postalSearch.objeto[0].erro != null) {
                    if (postalSearch != null && postalSearch.objeto.isNotEmpty()) {
                        show(btnTrack, postalSearch.objeto[0].erro)
                    } else {
                        show(btnTrack, R.string.msg_search_error)
                    }
                } else {
                    loadDataOnView(postalSearch)
                    show(btnTrack, R.string.msg_search_sucess)
                }
            }

            override fun onFailure(call: Call<PostalSearch>, t: Throwable) {
                progressBar.visible()
                show(btnTrack, R.string.msg_search_fail)
                t.message?.let {
                    showLog(it)
                }
            }
        })
    }

    private fun loadDataOnView(postalSearch: PostalSearch) {
        cardView.tvCategoria.text = postalSearch.objeto[0].categoria
        cardView.tvNome.text = postalSearch.objeto[0].nome
        cardView.tvSigla.text = postalSearch.objeto[0].sigla
        panelCard.removeAllViews()
        for (evento in postalSearch.objeto[0].evento) {
            val eventCardView = layoutInflater.inflate(R.layout.event_card_view, null)
            eventCardView.tvDescricao.text = evento.descricao
            eventCardView.tvData.text = evento.data.plus(" - ").plus(evento.hora)
            eventCardView.tvOrigem.text = evento.local
            eventCardView.tvDestino.text = evento.destino[0].local
            eventCardView.tvCidade.text = evento.destino[0].cidade
            eventCardView.tvUf.text = evento.destino[0].uf
            eventCardView.tvTipo.text = evento.tipo
            eventCardView.tvStatus.text = evento.status
            panelCard.addView(eventCardView)
        }
        cardView.visible()
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
                cardView.gone()
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

    companion object {
        const val REQUEST_CODE = 20
        const val HAS_CHANGE = "HAS_CHANGE"
    }
}
