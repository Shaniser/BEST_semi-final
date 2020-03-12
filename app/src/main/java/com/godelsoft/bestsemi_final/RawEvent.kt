package com.godelsoft.bestsemi_final

import com.godelsoft.bestsemi_final.util.CalFormatter
import java.util.*


enum class EventCategory {
    LBG, GLOBAL, PERSONAL
}

data class RawEvent(
    var date:           Date,       // Дата и время (Без учёта секунд)
    var sender:         String,         // Автор события
    var header:         String,         // Заголовок
    var body:           String,         // Комментарий
    var category:       EventCategory,  // Категория события (область видимости)
    var isSubscribed:   Boolean?        // Подписан ли на событие (или null, если оно обязательно)
) {
    constructor(): this(Date(0), "", "", "", EventCategory.GLOBAL, null)

    fun cal(): Calendar {
        return CalFormatter.getCalendarFromDate(date)
    }

}
