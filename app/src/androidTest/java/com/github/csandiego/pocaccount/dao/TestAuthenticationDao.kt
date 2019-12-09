package com.github.csandiego.pocaccount.dao

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TestAuthenticationDao @Inject constructor(): AuthenticationDao {

    val mutableUserId = MutableLiveData<Long?>()
    override val userId: LiveData<Long?> get() = mutableUserId

    override fun setUserId(id: Long) {
        mutableUserId.value = id
    }

    override fun clear() {
        mutableUserId.value = null
    }
}