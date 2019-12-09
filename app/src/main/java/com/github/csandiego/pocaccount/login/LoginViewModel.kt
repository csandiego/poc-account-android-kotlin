package com.github.csandiego.pocaccount.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.csandiego.pocaccount.R
import com.github.csandiego.pocaccount.authentication.AuthenticationContext
import com.github.csandiego.pocaccount.data.UserCredential
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginViewModel @Inject constructor(private val context: AuthenticationContext) : ViewModel() {

    var email = ""
    var password = ""

    private val _loginSuccess = MutableLiveData(false)
    val loginSuccess: LiveData<Boolean> get() = _loginSuccess

    fun loginSuccessHandled() {
        _loginSuccess.value = false
    }

    private val _loginFailure = MutableLiveData<Int?>()
    val loginFailure: LiveData<Int?> get() = _loginFailure

    fun loginFailureHandled() {
        _loginFailure.value = null
    }

    private val _loginError = MutableLiveData(false)
    val loginError: LiveData<Boolean> get() = _loginError

    fun loginErrorHandled() {
        _loginError.value = false
    }

    fun login() = viewModelScope.launch {
        try {
            if (context.login(UserCredential(email, password))) {
                _loginSuccess.value = true
            } else {
                _loginFailure.value = R.string.login_invalid_credential_message
            }
        } catch (e: Exception) {
            _loginError.value = true
        }
    }
}