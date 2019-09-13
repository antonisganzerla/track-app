package com.sgztech.rastreamento.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.sgztech.rastreamento.R
import com.sgztech.rastreamento.adapter.TrackObjectAdapter
import com.sgztech.rastreamento.core.CoreApplication
import com.sgztech.rastreamento.extension.validate
import com.sgztech.rastreamento.model.TrackObject
import com.sgztech.rastreamento.util.AlertDialogUtil
import com.sgztech.rastreamento.util.CodeUtil.filter
import com.sgztech.rastreamento.util.GoogleSignInUtil.getAccount
import com.sgztech.rastreamento.util.SnackBarUtil.show
import com.sgztech.rastreamento.view.TrackFragment.Companion.HAS_CHANGE
import kotlinx.android.synthetic.main.activity_track_object_list.*
import kotlinx.android.synthetic.main.dialog_add_track_object.view.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class TrackObjectListActivity : AppCompatActivity() {

    private val dialogView: View by lazy {
        layoutInflater.inflate(R.layout.dialog_add_track_object, null)
    }
    private val account: GoogleSignInAccount? by lazy {
        getAccount(applicationContext)
    }

    private val dialog: AlertDialog by lazy {
        AlertDialogUtil.buildCustomDialog(
            this,
            R.string.dialog_add_object_title,
            dialogView
        ).create()
    }

    private lateinit var list: MutableList<TrackObject>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_track_object_list)
        setupToolbar()
        loadData()
        setupDialog()
        setupFab()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        finish()
        return true
    }

    private fun setupToolbar() {
        toolbar.title = getString(R.string.toolbar_title_object_track)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun loadData() {
        GlobalScope.launch(context = Dispatchers.Main) {
            if (::list.isInitialized) {
                list.clear()
            }
            list = loadCodeList()
            setupRecyclerView()
        }
    }

    private suspend fun loadCodeList(): MutableList<TrackObject> {
        val result = GlobalScope.async {
            val dao = CoreApplication.database?.trackObjectDao()
            dao?.allByUser(getUserId())
        }
        return result.await()?.toMutableList() ?: mutableListOf()
    }

    private fun setupRecyclerView() {
        recycler_view_code_item.let {
            it.adapter = TrackObjectAdapter(list) {
                setupExtra()
            }
            it.layoutManager = LinearLayoutManager(this)
            it.setHasFixedSize(true)
        }
    }

    private fun setupDialog() {
        dialogView.btnSaveTrackObjet.setOnClickListener {
            if (dialogView.etName.validate() && dialogView.etCode.validate(true)) {
                saveTrackObject(
                    dialogView.etName.text.toString(),
                    dialogView.etCode.text.toString()
                )
                dialog.dismiss()
            }
        }
        dialogView.etCode.filters = filter(applicationContext)
    }

    private fun setupFab() {
        fab_list_code.setOnClickListener {
            dialog.show()
        }
    }

    private fun saveTrackObject(name: String, code: String) {
        val trackObject = TrackObject(name = name, code = code, idUser = getUserId())
        GlobalScope.launch {
            val dao = CoreApplication.database?.trackObjectDao()
            dao?.add(trackObject)
        }
        loadData()
        show(recycler_view_code_item, R.string.msg_track_object_saved)
        cleanFieldsDialog()
        setupExtra()
    }

    private fun setupExtra() {
        val resultIntent = Intent()
        resultIntent.putExtra(HAS_CHANGE, true)
        setResult(Activity.RESULT_OK, resultIntent)
    }

    private fun cleanFieldsDialog() {
        dialogView.etName.setText("")
        dialogView.etCode.setText("")
    }

    private fun getUserId(): String {
        return account.let {
            if (it != null && it.id != null) {
                it.id!!
            } else {
                ""
            }
        }
    }
}
