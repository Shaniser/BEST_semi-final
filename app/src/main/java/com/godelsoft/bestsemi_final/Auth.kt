package com.godelsoft.bestsemi_final

import androidx.annotation.UiThread
import kotlinx.coroutines.*

data class Auth(
    val username: String,
    val accessToken: String, // Данные для авторизации в firebase
    val error: String?
) {

    companion object {
        // Функция принимает данные для авторизации и, после проверки, вызывает callback
        // Возвращает пару - Объект Auth или описание ошибки
        // TODO: Заменить описание ошибки на код
        suspend fun login(email: String, passwd: String): Auth {
            delay(3000);
            if (email == "admin@ya.ru" && passwd == "qwerty1") {
                return authSuccess("Admin", "123456")
            }
            return authError("Error")
        }

        // Функция принимает данные для регистрации и, после проверки, вызывает callback
        // В случае успеха вернёт null, иначе - описание ошибки|
        // TODO: Заменить описание результата на коды
        suspend fun register(email: String, login: String, passwd: String): String? {
            delay(3000);
            if (passwd == "12345") {
                return "Error"
            }
            return null // Ok
        }

        private fun authSuccess(nick: String, token: String) = Auth(nick, token, null)
        private fun authError(description: String) = Auth("", "", description)
    }
}