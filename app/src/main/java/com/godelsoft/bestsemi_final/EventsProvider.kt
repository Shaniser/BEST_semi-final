package com.godelsoft.bestsemi_final

import kotlinx.coroutines.delay
import java.util.*


object EventsProvider {
    private var allEvents = mutableListOf<Event>()

    private var isDataLoaded = false //Позволяет не перезагружать данные в случае, если
    fun needsReload() = !isDataLoaded

    // Обновляет список и возвращает null в случае успеха, иначе - вернёт описание ошибки
    suspend fun reload(): String? {
//        allEvents.clear() // TODO: Temporary disabled
        delay(2000)
        // TODO: Загрузка данных из firebase
        if (!isDataLoaded) {
            allEvents.addAll(
                listOf(
                    Event(
                        1L,
                        Calendar.getInstance().also {
                            it.set(2020, 2, 10, 8, 30)
                        },
                        "Guy in the mirror",
                        "Math",
                        "Lectures at 8 AM are defenitly illegal :(",
                        EventCategory.GLOBAL,
                        null
                    ),
                    Event(
                        1L,
                        Calendar.getInstance().also {
                            it.set(2020, 3, 10, 8, 30)
                        },
                        "Guy in the mirror",
                        "Math",
                        "Lectures aaat 8 AM are defenitly illegal :(",
                        EventCategory.LGB,
                        null
                    ),
                    Event(
                        1L,
                        Calendar.getInstance().also {
                            it.set(2021, 3, 10, 8, 30)
                        },
                        "Guy in the mirror",
                        "Math",
                        "Lectures addt 8 AM are defenitly illegal :(",
                        EventCategory.GLOBAL,
                        null
                    ),
                    Event(
                        2L,
                        Calendar.getInstance().also {
                            it.set(2020, 3, 10, 4, 21)
                        },
                        "Our leader",
                        "Important event",
                        "Too late to wake up",
                        EventCategory.PERSONAL,
                        true
                    ),
                    Event(
                        1L,
                        Calendar.getInstance().also {
                            it.set(2020, 4, 10, 8, 30)
                        },
                        "Guy in the mirror",
                        "Math",
                        "Lectures at 8 AM qweare defenitly illegal :(",
                        EventCategory.GLOBAL,
                        null
                    )
                )
            )
            isDataLoaded = true
        }
        return null // Ok
    }

    suspend fun addEvent(event: Event) {
        delay(2000)
        allEvents.add(event)
        // TODO: Работа с firebase
    }

    suspend fun deleteEvent(id: Long) {
        delay(2000)
        // TODO: Работа с firebase
    }

    // Возвращает все события, которые пользователь в принципе может увидеть
    fun getAllAvaiableEvents(): List<Event> {
        return allEvents.sortedBy { it.date }
    }

    // Возвращает количество событий, которые пользователь в принципе может увидеть
    fun getAllAvaiableEventsCount(): Int {
        return allEvents.count()
    }

    // Возвращает все события за заданный день в порядке возрастания
    fun getEventsByDay(date: Calendar): List<Event> {
        return allEvents.filter {
            it.date.get(Calendar.DAY_OF_MONTH) == date.get(Calendar.DAY_OF_MONTH) &&
            it.date.get(Calendar.MONTH) == date.get(Calendar.MONTH) &&
            it.date.get(Calendar.YEAR) == date.get(Calendar.YEAR)
        }.sortedBy { it.date }
    }

    // Возвращает все события по фильтру в порядке возрастания
    fun getEventsByFilter(filter: (Event) -> Boolean) : List<Event> {
        return allEvents.filter(filter).sortedBy { it.date }
    }


    // Формирует строку с датой
    fun formatDate(c: Calendar): String {
        val day= {day: Int ->
            if (day < 10) "0$day" else "$day"
        } (c.get(Calendar.DAY_OF_MONTH))
        val month = {month: Int ->
            if (month < 10) "0$month" else "$month"
        } (c.get(Calendar.MONTH))
        val year = {year: Int ->
            if (year != Calendar.getInstance().get(Calendar.YEAR))
                (year % 100).toString()
            else
                ""
        } (c.get(Calendar.YEAR))
        return "$day.$month${if (year != "") ".$year" else ""}"
    }

    // Формирует строку со временем
    fun formatTime(c: Calendar): String {
        val hours= c.get(Calendar.HOUR).toString()
        val minutes = {month: Int ->
            if (month < 10) "0$month" else "$month"
        } (c.get(Calendar.MINUTE))
        return "$hours:$minutes"
    }

    fun getCalendarFromDate(d: Date): Calendar {
        return Calendar.getInstance().also {
            it.time = d // TODO: check this
        }
    }
}