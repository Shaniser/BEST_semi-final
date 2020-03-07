package com.godelsoft.bestsemi_final

import kotlinx.coroutines.*

class Auth(accessToken: String) {
    init {
    }

    companion object {
        private suspend fun checkLogin(email: String, passwd: String): Boolean {
            delay(5000);
            return email == "admin@ya.ru" && passwd == "qwerty";
        }

        fun Login(email: String, passwd: String, callback: (Boolean, Auth?) -> Unit) {
            val job: Job = GlobalScope.launch(Dispatchers.IO) {
                if (checkLogin(email, passwd)) {
                    callback(true, Auth("123456"));
                }
                callback(false, null);
            }
        }
    }
}