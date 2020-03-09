package com.godelsoft.bestsemi_final

import kotlinx.coroutines.delay
import java.util.*

object EventsProvider {
    private var allEvents = mutableListOf<Event>()

    private var isDataLoaded = false //Позволяет не перезагружать данные в случае, если
    fun needsReload() = !isDataLoaded

    // Обновляет список и возвращает null в случае успеха, иначе - вернёт описание ошибки
    suspend fun reload(): String? {
        allEvents.clear()
        delay(2000)
        // TODO: Загрузка данных из firebase
        allEvents.addAll(
            listOf(
                Event(
                    1L,
                    Date(2020, 3, 10, 8, 30),
                    "Guy in the mirror",
                    "Math",
                    "Lectures at 8 AM are defenitly illegal :(",
                    EventCategory.GLOBAL,
                    null
                ),
                Event(
                    2L,
                    Date(2020, 3, 10, 4, 21),
                    "Our leader",
                    "Important event",
                    "Too late to wake up",
                    EventCategory.PERSONAL,
                    true
                )
            )
        )
        if (!isDataLoaded)
            isDataLoaded = true
        return null // Ok
    }

    suspend fun addEvent(event: Event) {
        delay(2000)
        // TODO: Работа с firebase
    }

    suspend fun deleteEvent(id: Long) {
        delay(2000)
        // TODO: Работа с firebase
    }

    // Возвращает все события, которые пользователь в принципе может увидеть
    fun getAllAvaiableEvents(): MutableList<Event> {
        return allEvents
    }

    // Возвращает количество событий, которые пользователь в принципе может увидеть
    fun getAllAvaiableEventsCount(): Int {
        return allEvents.count()
    }
}