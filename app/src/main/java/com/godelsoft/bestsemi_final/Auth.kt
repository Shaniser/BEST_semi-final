package com.godelsoft.bestsemi_final

import kotlinx.coroutines.*

class Auth(var accessToken: String) {

    //    public var accessToken = accessToken;
    init {
    }

    companion object {
        private suspend fun checkLogin(email: String, passwd: String): Boolean {
            delay(5000);
            return email == "admin@ya.ru" && passwd == "qwerty1";
        }

        fun Login(email: String, passwd: String, callback: (Auth?, String) -> Unit) {
            val job: Job = GlobalScope.launch(Dispatchers.IO) {
                if (checkLogin(email, passwd)) {
                    callback(Auth("123456"), "");
                }
                callback(null, "Unauthorized");
            }
        }
    }
}