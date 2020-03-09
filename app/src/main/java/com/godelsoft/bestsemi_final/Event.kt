package com.godelsoft.bestsemi_final

import android.graphics.Color
import java.util.*


enum class EventCategory {
    LGB, GLOBAL, PERSONAL
}

data class Event(
    var id:             Long,           // ID события
    var date:           Calendar,       // Дата и время (Без учёта секунд)
    var sender:         String,         // Автор события
    var header:         String,         // Заголовок
    var body:           String,         // Комментарий
    var category:       EventCategory,  // Категория события (область видимости)
    var isSubscribed:   Boolean?        // Подписан ли на событие (или null, если оно обязательно)
)
