package com.godelsoft.bestsemi_final

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.*
import androidx.recyclerview.widget.RecyclerView
import com.godelsoft.bestsemi_final.util.CalFormatter


class EventAdapter(
     private val context: Context
) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    private var eventsList = mutableListOf<Event>()

    inner class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var header: TextView = itemView.findViewById(R.id.header)
        private var body: TextView = itemView.findViewById(R.id.body)
        private var sender: TextView = itemView.findViewById(R.id.sender)
        private var categoryColor: View = itemView.findViewById(R.id.categoryColor)
        private var time: TextView = itemView.findViewById(R.id.time)

        fun bind(event: Event) {
            header.text = event.header
            body.text = event.body
            sender.text = event.sender
            when (event.category) {
                EventCategory.PERSONAL ->
                    categoryColor.setBackgroundColor(getColor(context, R.color.colorEventPersonal))
                EventCategory.GLOBAL ->
                    categoryColor.setBackgroundColor(getColor(context, R.color.colorEventGlobal))
                EventCategory.LBG ->
                    categoryColor.setBackgroundColor(getColor(context, R.color.colorEventLGB))
            }
            time.text = "${CalFormatter.datef(event.date)} ${CalFormatter.timef(event.date)}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        return EventViewHolder(
            LayoutInflater
                .from(context)
                .inflate(R.layout.event_card, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return eventsList.count()
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        eventsList[position].let { holder.bind(it) }
    }

    fun update(data: List<Event>) {
        eventsList.clear()
        eventsList.addAll(data)
        notifyDataSetChanged()
    }
}