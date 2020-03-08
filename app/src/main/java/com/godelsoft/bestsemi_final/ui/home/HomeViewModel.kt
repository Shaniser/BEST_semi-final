package com.godelsoft.bestsemi_final.ui.home

import android.widget.Toast
import androidx.annotation.UiThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.godelsoft.bestsemi_final.Auth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val viewModelJob = Job()
    private val scope = CoroutineScope(Dispatchers.Main + viewModelJob)
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    @UiThread
    public fun makeAuth(email: String, passwd: String, callback: (Auth?, String?) -> Unit) {
        scope.launch(Dispatchers.IO) {
            val res = Auth.login(email, passwd)
            callback(res.first, res.second)
        }
    }

    @UiThread
    public fun makeRegister(
        email: String,
        login: String,
        passwd: String,
        callback: (String?) -> Unit
    ) {
        scope.launch(Dispatchers.IO) {
            val res = Auth.register(email, login, passwd)
            callback(res)
        }
    }

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text
}