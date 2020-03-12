package com.godelsoft.bestsemi_final

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.*
import androidx.recyclerview.widget.RecyclerView
import com.godelsoft.bestsemi_final.model.Event
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
        private var date: TextView = itemView.findViewById(R.id.date)
        private var conLay: ConstraintLayout = itemView.findViewById(R.id.conLay)

        fun bind(event: Event) {
            header.text = event.event.header
            body.text = event.event.body
            sender.text = event.event.sender
            when (event.event.category) {
                EventCategory.PERSONAL ->
                    categoryColor.setBackgroundColor(getColor(context, R.color.colorEventPersonal))
                EventCategory.GLOBAL ->
                    categoryColor.setBackgroundColor(getColor(context, R.color.colorEventGlobal))
                EventCategory.LBG ->
                    categoryColor.setBackgroundColor(getColor(context, R.color.colorEventLGB))
            }
            time.text = CalFormatter.timef(event.event.date)
            date.text = CalFormatter.datef(event.event.date)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        return EventViewHolder(
            LayoutInflater
                .from(context)
                .inflate(R.layout.card_event, parent, false)
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