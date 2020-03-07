package com.godelsoft.bestsemi_final

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// TODO: Implement this class

object EventsProvider {
    // Возвращает все события, которые пользователь впринципе может увидеть
    fun getAllAvaiableEvents(auth: Auth, callback: (List<Event>?, String) -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            delay(3000);
            // TODO: Используя данные из Auth получить список событий из firebase
            callback(null, "")
        }
    }

    // Возвращает события, связанные с пользователем
    // Фильтр для вывода getAllAvaiableEvents
    fun getUserEvents(auth: Auth, callback: (List<Event>?, String) -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            getAllAvaiableEvents(auth, fun (res: List<Event>?, err: String) {
                if (res == null)
                    callback(null, err)

                // TODO: Настроить фильтрацию на основе данных из Auth
                // callback(res.filter(...), "")
            })
        }
    }
}