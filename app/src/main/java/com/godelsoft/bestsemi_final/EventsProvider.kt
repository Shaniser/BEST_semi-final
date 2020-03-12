package com.godelsoft.bestsemi_final

import com.godelsoft.bestsemi_final.model.Event
import com.godelsoft.bestsemi_final.util.CalFormatter
import com.godelsoft.bestsemi_final.util.EventUtil
import kotlinx.coroutines.delay
import java.util.*


object EventsProvider {
    private var allEvents = mutableListOf<Event>()

    private var isDataLoaded = false //Позволяет не перезагружать данные в случае, если
    fun needsReload() = !isDataLoaded

    // Обновляет список и возвращает null в случае успеха, иначе - вернёт описание ошибки
    fun reload(): String? {
        if (!isDataLoaded) {
        /*
            allEvents.addAll(
                listOf(
                    Event(
                        "1L",
                        RawEvent(
                            Calendar.getInstance().also {
                                it.set(2020, 2, 10, 8, 30)
                            },
                            "Guy in the mirror",
                            "Math",
                            "Lectures at 8 AM are defenitly illegal :(",
                            EventCategory.GLOBAL,
                            null
                        )
                    ),
                    Event(
                        "1L",
                        RawEvent(
                            Calendar.getInstance().also {
                                it.set(2020, 3, 10, 8, 30)
                            },
                            "Guy in the mirror",
                            "Math",
                            "Lectures aaat 8 AM are defenitly illegal :(",
                            EventCategory.LBG,
                            null
                        )
                    ),
                    Event(
                        "1L",
                        RawEvent(
                            Calendar.getInstance().also {
                                it.set(2021, 3, 10, 8, 30)
                            },
                            "Guy in the mirror",
                            "Math",
                            "Lectures addt 8 AM are defenitly illegal :(",
                            EventCategory.GLOBAL,
                            null
                        )
                    ),
                    Event(
                        "2L",
                        RawEvent(
                            Calendar.getInstance().also {
                                it.set(2020, 3, 10, 4, 21)
                            },
                            "Our leader",
                            "Important event",

                            "Too late to wake up",
                            EventCategory.PERSONAL,
                            true
                        )
                    ),
                    Event(
                        "1L",
                        RawEvent(
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
            )*/
            EventUtil.getEvents { events ->
                allEvents = events
            }
            isDataLoaded = true
        }
        return null // Ok
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
