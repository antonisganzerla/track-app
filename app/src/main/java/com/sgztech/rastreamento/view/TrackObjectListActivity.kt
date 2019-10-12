package com.sgztech.rastreamento.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.sgztech.rastreamento.R
import com.sgztech.rastreamento.adapter.TrackObjectAdapter
import com.sgztech.rastreamento.extension.gone
import com.sgztech.rastreamento.extension.validate
import com.sgztech.rastreamento.extension.visible
import com.sgztech.rastreamento.model.TrackObject
import com.sgztech.rastreamento.util.AlertDialogUtil
import com.sgztech.rastreamento.util.CodeUtil.filter
import com.sgztech.rastreamento.util.PreferenceUtil.getUserId
import com.sgztech.rastreamento.util.SnackBarUtil.show
import com.sgztech.rastreamento.view.TrackFragment.Companion.HAS_CHANGE
import com.sgztech.rastreamento.viewmodel.TrackObjectViewModel
import kotlinx.android.synthetic.main.activity_track_object_list.*
import kotlinx.android.synthetic.main.dialog_add_track_object.view.*
import kotlinx.android.synthetic.main.toolbar.*
import org.koin.android.ext.android.inject

class TrackObjectListActivity : AppCompatActivity() {

    private val dialogView: View by lazy {
        layoutInflater.inflate(R.layout.dialog_add_track_object, null)
    }

    private val dialog: AlertDialog by lazy {
        AlertDialogUtil.buildCustomDialog(
            this,
            R.string.dialog_add_object_title,
            dialogView
        ).create()
    }

    private val viewModel: TrackObjectViewModel by inject()
    private lateinit var adapter: TrackObjectAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_track_object_list)
        setupToolbar()
        setupAdapter()
        setupRecyclerView()
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
        viewModel.getAll(userId()).observe(
            this, Observer {
                adapter.setTrackObjects(it)
                setupListVisibility(it)
            }
        )
    }

    private fun setupAdapter(){
        adapter = TrackObjectAdapter{ trackObject ->
            viewModel.delete(trackObject)
            show(recycler_view_code_item, R.string.msg_track_object_deleted)
            setupExtra()
        }
    }

    private fun setupRecyclerView() {
        recycler_view_code_item.let {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(this)
            it.setHasFixedSize(true)
        }
    }

    private fun setupDialog() {
        dialogView.btnSaveTrackObjet.setOnClickListener {
            if (dialogView.etName.validate(textInputLayout = dialogView.textInputLayoutName) &&
                dialogView.etCode.validate(true, dialogView.textInputLayoutCode)
            ) {
                saveTrackObject(
                    dialogView.etName.text.toString(),
                    dialogView.etCode.text.toString()
                )
                cleanFieldsDialog()
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
        val trackObject = TrackObject(name = name, code = code, idUser = userId())
        viewModel.insert(trackObject)
        show(recycler_view_code_item, R.string.msg_track_object_saved)
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

    private fun setupListVisibility(list: List<TrackObject>) {
        if (list.isEmpty()) {
            recycler_view_code_item.gone()
            panel_empty_list.visible()
        } else {
            recycler_view_code_item.visible()
            panel_empty_list.gone()
        }
    }

    private fun userId(): String {
        return getUserId(applicationContext)
    }
}
