package com.github.csandiego.pocaccount.dao

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import javax.inject.Inject

class SharedPreferencesAuthenticationDao @Inject constructor() : AuthenticationDao {

    private val _userId = MutableLiveData<Long?>()
    override val userId: LiveData<Long?> get() = _userId

    override fun setUserId(id: Long) {
        _userId.value = id
    }

    override fun clear() {
        _userId.value = null
    }
}