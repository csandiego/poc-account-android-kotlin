package com.github.csandiego.pocaccount.authentication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.csandiego.pocaccount.data.UserCredential
import com.github.csandiego.pocaccount.service.AuthenticationService
import kotlinx.coroutines.launch

class AuthenticationViewModel(private val service: AuthenticationService) : ViewModel() {

    var email = ""
    var password = ""

    private val _authenticationSuccess = MutableLiveData(false)
    val authenticationSuccess: LiveData<Boolean> get() = _authenticationSuccess

    fun authenticationSuccessHandled() {
        _authenticationSuccess.value = false
    }

    private val _authenticationFailure = MutableLiveData(false)
    val authenticationFailure: LiveData<Boolean> get() = _authenticationFailure

    fun authenticationFailureHandled() {
        _authenticationFailure.value = false
    }

    fun authenticate() = viewModelScope.launch {
        try {
            service.authenticate(UserCredential(email, password))
            _authenticationSuccess.value = true
        } catch (e: Exception) {
            _authenticationFailure.value = true
        }
    }
}