package com.github.csandiego.pocaccount.service

import com.github.csandiego.pocaccount.data.UserCredential
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TestAuthenticationService @Inject constructor() : AuthenticationService {

    var authenticateUserId = 0L
    var authenticateException = false

    override suspend fun authenticate(credential: UserCredential): Long {
        if (authenticateException) {
            throw Exception()
        }
        return authenticateUserId
    }
}