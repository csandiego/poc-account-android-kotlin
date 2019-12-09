package com.github.csandiego.pocaccount.authentication

import com.github.csandiego.pocaccount.dao.AuthenticationDao
import com.github.csandiego.pocaccount.data.UserCredential
import com.github.csandiego.pocaccount.service.AuthenticationService
import javax.inject.Inject

class DefaultAuthenticationContext @Inject constructor(
    private val dao: AuthenticationDao,
    private val service: AuthenticationService
) : AuthenticationContext {

    override val userId = dao.userId

    override suspend fun login(credential: UserCredential): Boolean {
        val id = service.authenticate(credential)
        if (id > 0L) {
            dao.setUserId(id)
            return true
        }
        return false
    }

    override fun logout() {
        dao.clear()
    }
}