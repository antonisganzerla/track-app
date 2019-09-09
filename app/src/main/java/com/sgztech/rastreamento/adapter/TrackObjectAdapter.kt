package com.sgztech.rastreamento.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.sgztech.rastreamento.R
import com.sgztech.rastreamento.core.CoreApplication
import com.sgztech.rastreamento.model.TrackObject
import com.sgztech.rastreamento.util.AlertDialogUtil
import com.sgztech.rastreamento.util.SnackBarUtil
import kotlinx.android.synthetic.main.track_object_card_view.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TrackObjectAdapter(
    private val list: MutableList<TrackObject>,
    private val callBack: () -> Unit
) : RecyclerView.Adapter<TrackObjectAdapter.TrackObjectViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackObjectViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.track_object_card_view, parent, false)
        return TrackObjectViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: TrackObjectViewHolder, position: Int) {
        holder.bind(list[position], position)
    }


    inner class TrackObjectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(trackObject: TrackObject, position: Int) {
            itemView.tvObjectCode.text = trackObject.code
            itemView.tvObjectName.text = trackObject.name.plus(" - ")
            itemView.btnDeleteObjectTrack.setOnClickListener {
                createAlertDialog(trackObject, position).show()
            }
        }

        private fun createAlertDialog(trackObject: TrackObject, position: Int): AlertDialog {
            val context = itemView.context
            return AlertDialogUtil.create(
                context,
                R.string.msg_delete_track_object
            ) {
                deleteObject(trackObject)
                list.remove(trackObject)
                notifyItemRemoved(position)
                SnackBarUtil.show(itemView, R.string.msg_track_object_deleted)
                callBack()
            }
        }

        private fun deleteObject(trackObject: TrackObject) {
            GlobalScope.launch {
                val dao = CoreApplication.database?.trackObjectDao()
                dao?.delete(trackObject)
            }
        }
    }
}