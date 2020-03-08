package com.godelsoft.bestsemi_final

import kotlinx.coroutines.delay
import java.lang.Exception

object EventsProvider {
    private var allEvents = mutableListOf<Event>()

    // Обновляет список и возвращает null в случае успеха, иначе - вернёт описание ошибки
    suspend fun reload(): String? {
        allEvents.clear()
        delay(2000)
        // TODO: Загрузка данных из firebase
        allEvents.addAll(listOf(
            Event(
                "8:30",
                "Guy in the mirror",
                "Math",
                "Lectures at 8 AM are defenitly illegal :("
            ),
            Event(
                "4:21",
                "Our leader",
                "Important event",
                "Too late to wake up"
            )
        ))
        return null // Ok
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