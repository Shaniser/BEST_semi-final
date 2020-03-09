package com.godelsoft.bestsemi_final

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.*
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.selects.select
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter


class EventAdapter(
     private val context: Context
) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    private var eventsList = mutableListOf<Event>()

    inner class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var header: TextView = itemView.findViewById(R.id.header)
        private var body: TextView = itemView.findViewById(R.id.body)
        private var sender: TextView = itemView.findViewById(R.id.sender)
        private var category: TextView = itemView.findViewById(R.id.category)
        private var time: TextView = itemView.findViewById(R.id.time)
        private var conLay: ConstraintLayout = itemView.findViewById(R.id.conLay)

        fun bind(event: Event) {
            header.text = event.header
            body.text = event.body
            sender.text = event.sender
            category.text = when (event.category) {
                EventCategory.PERSONAL -> context.getString(R.string.category_personal_name)
                EventCategory.GLOBAL -> context.getString(R.string.category_global_name)
                EventCategory.LGB -> context.getString(R.string.category_lgb_name)
            }
            time.text = SimpleDateFormat("dd.MM.yyyy hh:mm").format(event.date)

            when {
                event.isSubscribed == false ->
                    conLay.setBackgroundColor(getColor(context, R.color.colorEventInactive))
                event.category == EventCategory.PERSONAL ->
                    conLay.setBackgroundColor(getColor(context, R.color.colorEventPersonal))
                event.category == EventCategory.GLOBAL ->
                    conLay.setBackgroundColor(getColor(context, R.color.colorEventGlobal))
                event.category == EventCategory.LGB ->
                    conLay.setBackgroundColor(getColor(context, R.color.colorEventLGB))
            }
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