package com.github.csandiego.pocaccount.wire

import com.github.csandiego.pocaccount.data.UserCredential
import com.github.csandiego.pocaccount.protobuf.UserRegistrationClient
import com.github.csandiego.pocaccount.protobuf.ValidationRequest
import com.github.csandiego.pocaccount.service.UserRegistrationService
import javax.inject.Inject

@Suppress("BlockingMethodInNonBlockingContext")
class WireUserRegistrationService @Inject constructor(private val client: UserRegistrationClient) :
    UserRegistrationService {

    override suspend fun validate(email: String) =
        client.Validate().execute(ValidationRequest(email)).valid

    override suspend fun register(credential: UserCredential) {
        val request = com.github.csandiego.pocaccount.protobuf.UserCredential(
            credential.email, credential.password
        )
        client.Register().execute(request)
    }
}