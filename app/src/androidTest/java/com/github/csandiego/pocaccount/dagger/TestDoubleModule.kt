package com.github.csandiego.pocaccount.dagger

import com.github.csandiego.pocaccount.dao.AuthenticationDao
import com.github.csandiego.pocaccount.dao.TestAuthenticationDao
import com.github.csandiego.pocaccount.service.AuthenticationService
import com.github.csandiego.pocaccount.service.TestAuthenticationService
import com.github.csandiego.pocaccount.service.TestUserRegistrationService
import com.github.csandiego.pocaccount.service.UserRegistrationService
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface TestDoubleModule {

    @Singleton
    @Binds
    fun provideAuthenticationDao(dao: TestAuthenticationDao): AuthenticationDao

    @Singleton
    @Binds
    fun provideUserRegistrationService(serivce: TestUserRegistrationService): UserRegistrationService

    @Singleton
    @Binds
    fun provideAuthenticationSerivce(service: TestAuthenticationService): AuthenticationService
}