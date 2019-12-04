package com.github.csandiego.pocaccount.wire

import com.github.csandiego.pocaccount.data.UserCredential
import com.github.csandiego.pocaccount.protobuf.AuthenticationClient
import com.github.csandiego.pocaccount.service.AuthenticationService

@Suppress("BlockingMethodInNonBlockingContext")
class WireAuthenticationService(private val client: AuthenticationClient) : AuthenticationService {

    override suspend fun authenticate(credential: UserCredential): Long {
        val request = com.github.csandiego.pocaccount.protobuf.UserCredential(
            credential.email, credential.password
        )
        return client.Authenticate().execute(request).user_id
    }
}