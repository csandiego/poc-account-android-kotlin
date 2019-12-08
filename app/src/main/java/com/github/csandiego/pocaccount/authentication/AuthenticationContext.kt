package com.github.csandiego.pocaccount.authentication

import androidx.lifecycle.LiveData
import com.github.csandiego.pocaccount.data.UserCredential

interface AuthenticationContext {

    val userId: LiveData<Long?>

    suspend fun login(credential: UserCredential): Boolean

    fun logout()
}