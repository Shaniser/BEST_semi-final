package com.godelsoft.bestsemi_final

import androidx.annotation.UiThread
import kotlinx.coroutines.*

class Auth(
    val username: String,
    val accessToken: String // Данные для авторизации в firebase
) {

    companion object {
        // Функция принимает данные для авторизации и, после проверки, вызывает callback
        // Возвращает пару - Объект Auth или описание ошибки
        // TODO: Заменить описание ошибки на код
        suspend fun login(email: String, passwd: String): Pair<Auth?, String?> {
            delay(3000);
            if (email == "admin@ya.ru" && passwd == "qwerty1") {
                return Pair(Auth("Admin", "123456"), null)
            }
            return Pair(null, "Error")
        }

        // Функция принимает данные для регистрации и, после проверки, вызывает callback
        // В случае успеха вернёт null, иначе - описание ошибки
        // TODO: Заменить описание результата на коды
        suspend fun register(email: String, login: String, passwd: String): String? {
            delay(3000);
            if (passwd == "12345") {
                return "Error"
            }
            return null // Ok
        }
    }
}