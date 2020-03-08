package com.godelsoft.bestsemi_final

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EventAdapter(private val context: Context) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {


    class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private  var header: TextView = itemView.findViewById(R.id.header)
        private  var body: TextView = itemView.findViewById(R.id.body)
        private var sender: TextView = itemView.findViewById(R.id.sender)
        private  var time: TextView = itemView.findViewById(R.id.time)

        fun bind(event: Event) {
            header.text = event.header
            body.text = event.body
            sender.text = event.sender
            time.text = event.time
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        return EventViewHolder(LayoutInflater
            .from(context)
            .inflate(R.layout.event_card, parent, false))
    }

    override fun getItemCount(): Int {
        return Event.events.size
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        Event.events[position]?.let { holder.bind(it) }
    }
}