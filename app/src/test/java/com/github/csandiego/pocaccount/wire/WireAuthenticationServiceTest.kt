package com.github.csandiego.pocaccount.wire

import com.github.csandiego.pocaccount.protobuf.AuthenticationClient
import com.github.csandiego.pocaccount.protobuf.AuthenticationResponse
import com.github.csandiego.pocaccount.protobuf.UserCredential
import com.squareup.wire.GrpcCall
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class WireAuthenticationServiceTest {

    private lateinit var client: TestAuthenticationClient
    private lateinit var service: WireAuthenticationService
    private val credential =
        com.github.csandiego.pocaccount.data.UserCredential("someone@somewhere.com", "password")

    @Before
    fun setUp() {
        client = TestAuthenticationClient()
        service = WireAuthenticationService(client)
    }

    @Test
    fun whenAuthenticateThenCopyCredential() = runBlocking {
        val call = object : TestGrcpCall<UserCredential, AuthenticationResponse>() {
            lateinit var credential: UserCredential
            override suspend fun execute(request: UserCredential): AuthenticationResponse {
                credential = request
                return AuthenticationResponse(0L)
            }
        }.also { client.authenticateCall = it }
        service.authenticate(credential)
        assertEquals(credential.email, call.credential.email)
        assertEquals(credential.password, call.credential.password)
    }

    @Test
    fun whenAuthenticateThenUnwrapUserId() = runBlocking {
        val call = object : TestGrcpCall<UserCredential, AuthenticationResponse>() {
            val response = AuthenticationResponse(1L)
            override suspend fun execute(request: UserCredential) = response
        }.also { client.authenticateCall = it }
        assertEquals(call.response.user_id, service.authenticate(credential))
    }

    @Suppress("TestFunctionName")
    private class TestAuthenticationClient : AuthenticationClient {
        lateinit var authenticateCall: GrpcCall<UserCredential, AuthenticationResponse>
        override fun Authenticate() = authenticateCall
    }
}