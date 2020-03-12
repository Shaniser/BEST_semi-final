package com.godelsoft.bestsemi_final

import com.godelsoft.bestsemi_final.model.Event
import com.godelsoft.bestsemi_final.util.EventUtil
import com.google.firebase.auth.FirebaseAuth
import java.util.*


object EventsProvider {
    private var allEvents = mutableListOf<Event>()

    private var isDataLoaded = false //Позволяет не перезагружать данные в случае, если
    fun needsReload() = !isDataLoaded

    // Обновляет список
    fun reload() {
        reload {}
    }

    fun reload(callback: () -> Unit) {
        allEvents.clear()
        if (!isDataLoaded) {
            EventUtil.getEvents { events ->
                events.forEach {
                    if (it.event.category != EventCategory.PERSONAL ||
                        it.event.personalId == FirebaseAuth.getInstance().currentUser?.uid)
                            allEvents.add(it)
                }
                callback()
            }
        }
    }

    fun addEvent(event: Event) {
        EventUtil.addEvent(event.event)
        reload()
    }

    fun deleteEvent(id: String) {
        EventUtil.deleteEvent(id)
        reload()
    }

    // Возвращает все события, которые пользователь в принципе может увидеть
    fun getAllAvailableEvents(): List<Event> {
        return allEvents.sortedBy { it.event.cal() }
    }

    // Возвращает количество событий, которые пользователь в принципе может увидеть
    fun getAllAvailableEventsCount(): Int {
        return allEvents.count()
    }

    // Возвращает все события за заданный день в порядке возрастания
    fun getEventsByDay(date: Calendar): List<Event> {
        return allEvents.filter {
            it.event.cal().get(Calendar.DAY_OF_MONTH) == date.get(Calendar.DAY_OF_MONTH) &&
                    it.event.cal().get(Calendar.MONTH) == date.get(Calendar.MONTH) &&
                    it.event.cal().get(Calendar.YEAR) == date.get(Calendar.YEAR)
        }.sortedBy { it.event.cal() }
    }

    // Возвращает все события по фильтру в порядке возрастания
    fun getEventsByFilter(filter: (Event) -> Boolean) : List<Event> {
        return allEvents.filter(filter).sortedBy { it.event.cal() }
    }
}
