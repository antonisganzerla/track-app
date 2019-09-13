package com.sgztech.rastreamento

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sgztech.rastreamento.model.Track
import kotlinx.android.synthetic.main.event_card_view.view.*

class TrackEventAdapter(
    private val list: MutableList<Track>): RecyclerView.Adapter<TrackEventAdapter.TrackEventViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackEventViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.event_card_view, parent, false)
        return TrackEventViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: TrackEventViewHolder, position: Int) {
        holder.bind(list[position])
    }


    inner class TrackEventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(track: Track) {
            itemView.tvStatus.text = track.status
            itemView.tvLocal.text = track.locale
            itemView.tvData.text = track.trackedAt
            //itemView.tvData.text = track.trackedAt.toDate().toString()
            itemView.tvObservacao.text = track.observation
        }
    }
}