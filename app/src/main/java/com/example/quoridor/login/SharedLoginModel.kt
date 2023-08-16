package com.example.quoridor.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel



class SharedLoginModel : ViewModel(){
    private val _loginSuccess = MutableLiveData<Boolean>()
    val loginSuccess: LiveData<Boolean> = _loginSuccess

    fun setLoginSuccess(success: Boolean) {
        _loginSuccess.value = success
    }
}