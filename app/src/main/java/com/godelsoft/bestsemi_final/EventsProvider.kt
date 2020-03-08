package com.godelsoft.bestsemi_final

import kotlinx.coroutines.delay
import java.lang.Exception

object EventsProvider {
    private var isLoaded = false // Гарантирует как минимум один вызов reload
    private var allEvents = mutableListOf<Event>()

    // Обновляет список и возвращает null в случае успеха, иначе - вернёт описание ошибки
    suspend fun reload(auth: Auth): String? {
        allEvents.clear()
        if (auth.error != null)
            throw Exception("Auth error")
        delay(3000)
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
        return null
    }

    // Инициализирует список и возвращает null в случае успеха, иначе - вернёт описание ошибки
    suspend fun init(auth: Auth): String? {
        isLoaded = true
        return reload(auth)
    }

    // Возвращает все события, которые пользователь в принципе может увидеть
    fun getAllAvaiableEvents(): MutableList<Event> {
        if (!isLoaded) {
            throw Exception("Events was not loaded")
        }
        return allEvents
    }

    // Возвращает количество событий, которые пользователь в принципе может увидеть
    fun getAllAvaiableEventsCount(): Int {
        if (!isLoaded) {
            throw Exception("Events was not loaded")
        }
        return allEvents.count()
    }
}