package com.github.csandiego.pocaccount.authentication

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.csandiego.pocaccount.dao.AuthenticationDao
import com.github.csandiego.pocaccount.data.UserCredential
import com.github.csandiego.pocaccount.service.AuthenticationService
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DefaultAuthenticationContextTest {

    private lateinit var dao: TestAuthenticationDao
    private lateinit var service: TestAuthenticationService
    private lateinit var context: DefaultAuthenticationContext
    private val credential = UserCredential("someone@somewhere.com", "password")


    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        dao = TestAuthenticationDao()
        service = TestAuthenticationService()
        context = DefaultAuthenticationContext(dao, service).apply {
            userId.observeForever {}
        }
    }

    @Test
    fun givenNotLoggedInWhenInitializedThenUserIdIsNull() {
        assertNull(context.userId.value)
    }

    @Test
    fun givenLoggedInThenWhenInitializedThenUserIdNotNull() {
        dao.mutableUserId.value = 1L
        assertNotNull(context.userId.value)
    }

    @Test
    fun givenValidCredentialWhenLoginThenReturnTrue() = runBlocking {
        val id = 1L
        with(service) {
            authenticateUserId = id
            authenticateException = false
        }
        assertTrue(context.login(credential))
    }

    @Test
    fun givenValidCredentialWhenLoginThenUpdateDao() = runBlocking {
        val id = 1L
        with(service) {
            authenticateUserId = id
            authenticateException = false
        }
        context.login(credential)
        assertEquals(id, dao.mutableUserId.value)
    }

    @Test
    fun givenInvalidCredentialWhenLoginThenReturnFalse() = runBlocking {
        with(service) {
            authenticateUserId = 0L
            authenticateException = false
        }
        assertFalse(context.login(credential))
    }

    @Test
    fun givenLoggedInWhenLogoutThenUpdateDao() {
        dao.mutableUserId.value = 1L
        context.logout()
        assertNull(dao.mutableUserId.value)
    }

    private class TestAuthenticationDao:
        AuthenticationDao {

        val mutableUserId = MutableLiveData<Long?>()
        override val userId: LiveData<Long?> get() = mutableUserId

        override fun setUserId(id: Long) {
            mutableUserId.value = id
        }

        override fun clear() {
            mutableUserId.value = null
        }
    }

    private class TestAuthenticationService : AuthenticationService {
        var authenticateUserId = 0L
        var authenticateException = false
        override suspend fun authenticate(credential: UserCredential): Long {
            if (authenticateException) {
                throw Exception()
            }
            return authenticateUserId
        }
    }
}