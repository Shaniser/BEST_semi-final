package com.godelsoft.bestsemi_final

import kotlinx.coroutines.*

class Auth(var accessToken: String) {

    companion object {
        // Функция принимает данные для авторизации и, после проверки, вызывает callback
        // Если логин-пароль верны, то в callback будет передан объект Auth
        // Иначе - будет передан null и описание ошибки
        // TODO: Заменить описание ошибки на коды
        fun login(email: String, passwd: String, callback: (Auth?, String) -> Unit) {
            GlobalScope.launch(Dispatchers.IO) {
                delay(3000);
                if (email == "admin@ya.ru" && passwd == "qwerty1") {
                    callback(Auth("123456"), "");
                }
                callback(null, "Unauthorized");
            }
        }

        // Функция принимает данные для регистрации и, после проверки, вызывает callback
        // В callback будет передано описание результата ("OK", "Error", etc);
        // TODO: Заменить описание результата на коды
        fun register(email: String, login: String, passwd: String, callback: (String) -> Unit) {
            GlobalScope.launch(Dispatchers.IO) {
                delay(3000);
                if (passwd != "12345") {
                    callback("Ok");
                }
                callback("Error");
            }
        }
    }
}