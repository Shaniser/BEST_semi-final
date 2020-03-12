package com.godelsoft.bestsemi_final

import com.godelsoft.bestsemi_final.util.CalFormatter
import java.util.*

enum class EventsFilterDateType { ALL, TODAY, WEEK, DATE, FUTURE }

class EventsFilter {
    var showPersonal: Boolean = true
    var showGlobal: Boolean = true
    var showLBG: Boolean = false

    var dateType = EventsFilterDateType.ALL
    var filterDate = Calendar.getInstance()

    fun checkCategory(cat: EventCategory): Boolean {
        return when (cat) {
            EventCategory.PERSONAL -> showPersonal
            EventCategory.GLOBAL -> showGlobal
            EventCategory.LBG -> showLBG
        }
    }

    fun checkDate(cal: Calendar): Boolean {
        val today = Calendar.getInstance()
        return when (dateType) {
            EventsFilterDateType.ALL -> true
            EventsFilterDateType.TODAY -> CalFormatter.checkDaysEq(today, cal)
            EventsFilterDateType.WEEK -> {
                today.get(Calendar.WEEK_OF_YEAR) == cal.get(Calendar.WEEK_OF_YEAR) &&
                today.get(Calendar.YEAR) == cal.get(Calendar.YEAR)
            }
            EventsFilterDateType.DATE -> CalFormatter.checkDaysEq(filterDate, cal)
            EventsFilterDateType.FUTURE -> today.before(cal)
        }
    }

    companion object {
        var filter: EventsFilter = EventsFilter()
    }
}