package com.github.csandiego.pocaccount.registration

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.csandiego.pocaccount.data.UserCredential
import com.github.csandiego.pocaccount.service.UserRegistrationService
import kotlinx.coroutines.launch
import javax.inject.Inject

class RegistrationViewModel @Inject constructor(private val service: UserRegistrationService) : ViewModel() {

    private var _email = ""
    var email: String
        get() = _email
        set(value) {
            if (value != _email) {
                _email = value
                validateEmail(value)
            }
        }

    var password = ""

    private val _isEmailValid = MutableLiveData(false)
    val isEmailValid: LiveData<Boolean> get() = _isEmailValid

    private val _validationFailure = MutableLiveData(false)
    val validationFailure: LiveData<Boolean> get() = _validationFailure

    fun validationFailureHandled() {
        _validationFailure.value = false
    }

    private fun validateEmail(email: String) = viewModelScope.launch {
        try {
            _isEmailValid.value = service.validate(email)
        } catch (e: Exception) {
            _validationFailure.value = true
        }
    }

    private val _registrationSuccess = MutableLiveData(false)
    val registrationSuccess: LiveData<Boolean> get() = _registrationSuccess

    fun registrationSuccessHandled() {
        _registrationSuccess.value = false
    }

    private val _registrationFailure = MutableLiveData(false)
    val registrationFailure: LiveData<Boolean> get() = _registrationFailure

    fun registrationFailureHandled() {
        _registrationFailure.value = false
    }

    fun register() = viewModelScope.launch {
        try {
            service.register(UserCredential(_email, password))
            _registrationSuccess.value = true
        } catch (e: Exception) {
            _registrationFailure.value = true
        }
    }
}