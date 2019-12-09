package com.github.csandiego.pocaccount.service

import com.github.csandiego.pocaccount.data.UserCredential
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TestUserRegistrationService @Inject constructor() : UserRegistrationService {

    lateinit var validateEmail: String
    var validateResult = false
    var validateException = false

    lateinit var registerCredential: UserCredential
    var registerException = false

    override suspend fun validate(email: String): Boolean {
        validateEmail = email
        if (validateException) {
            throw Exception()
        }
        return validateResult
    }

    override suspend fun register(credential: UserCredential) {
        registerCredential = credential
        if (registerException) {
            throw Exception()
        }
    }
}