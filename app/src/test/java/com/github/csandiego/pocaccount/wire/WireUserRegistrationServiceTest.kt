package com.github.csandiego.pocaccount.wire

import com.github.csandiego.pocaccount.protobuf.*
import com.squareup.wire.GrpcCall
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class WireUserRegistrationServiceTest {

    private lateinit var client: TestUserRegistrationClient
    private lateinit var service: WireUserRegistrationService
    private val credential =
        com.github.csandiego.pocaccount.data.UserCredential("someone@somewhere.com", "password")

    @Before
    fun setUp() {
        client = TestUserRegistrationClient()
        service = WireUserRegistrationService(client)
    }

    @Test
    fun whenValidateThenWrapEmail() = runBlocking {
        val call = object : TestGrcpCall<ValidationRequest, ValidationResponse>() {
            lateinit var email: String
            override suspend fun execute(request: ValidationRequest): ValidationResponse {
                email = request.email
                return ValidationResponse(true)
            }
        }.also { client.validateCall = it }
        service.validate(credential.email)
        assertEquals(credential.email, call.email)
    }

    @Test
    fun whenValidateThenUnwrapValid() = runBlocking {
        val call = object : TestGrcpCall<ValidationRequest, ValidationResponse>() {
            val response = ValidationResponse(true)
            override suspend fun execute(request: ValidationRequest) = response
        }.also { client.validateCall = it }
        assertEquals(call.response.valid, service.validate(credential.email))
    }

    @Test
    fun whenRegisterThenCopyCredential() = runBlocking {
        val call = object : TestGrcpCall<UserCredential, Empty>() {
            lateinit var credential: UserCredential
            override suspend fun execute(request: UserCredential): Empty {
                credential = request
                return Empty()
            }
        }.also { client.registerCall = it }
        service.register(credential)
        assertEquals(credential.email, call.credential.email)
        assertEquals(credential.password, call.credential.password)
    }

    @Suppress("TestFunctionName")
    private class TestUserRegistrationClient : UserRegistrationClient {
        lateinit var validateCall: GrpcCall<ValidationRequest, ValidationResponse>
        lateinit var registerCall: GrpcCall<UserCredential, Empty>
        override fun Validate() = validateCall
        override fun Register() = registerCall
    }
}