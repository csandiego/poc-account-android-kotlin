package com.github.csandiego.pocaccount.dagger

import com.github.csandiego.pocaccount.dao.AuthenticationDao
import com.github.csandiego.pocaccount.dao.SharedPreferencesAuthenticationDao
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface SharedPreferencesModule {

    @Singleton
    @Binds
    fun provideAuthenticationDao(dao: SharedPreferencesAuthenticationDao): AuthenticationDao
}