package com.github.csandiego.pocaccount.service

import com.github.csandiego.pocaccount.data.UserCredential

interface UserRegistrationService {

    suspend fun validate(email: String): Boolean

    suspend fun register(credential: UserCredential)
}