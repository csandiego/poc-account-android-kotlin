package com.github.csandiego.pocaccount.authentication

import com.github.csandiego.pocaccount.data.UserCredential

interface AuthenticationContext {

    val userId: Long

    val isLoggedIn: Boolean

    fun login(credential: UserCredential): Boolean

    fun logout()
}