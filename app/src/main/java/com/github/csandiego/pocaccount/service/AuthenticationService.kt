package com.github.csandiego.pocaccount.service

import com.github.csandiego.pocaccount.data.UserCredential

interface AuthenticationService {

    suspend fun authenticate(credential: UserCredential): Long
}