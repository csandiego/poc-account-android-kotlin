package com.github.csandiego.pocaccount.dao

import androidx.lifecycle.LiveData

interface AuthenticationDao {

    val userId: LiveData<Long?>

    fun setUserId(id: Long)

    fun clear()
}