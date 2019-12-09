package com.github.csandiego.pocaccount.dagger

import com.github.csandiego.pocaccount.service.AuthenticationService
import com.github.csandiego.pocaccount.service.UserRegistrationService
import com.github.csandiego.pocaccount.wire.WireAuthenticationService
import com.github.csandiego.pocaccount.wire.WireUserRegistrationService
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module(includes = [WireRuntimeModule::class])
interface WireModule {

    @Singleton
    @Binds
    fun provideUserRegistrationService(service: WireUserRegistrationService): UserRegistrationService

    @Singleton
    @Binds
    fun provideAuthenticationService(service: WireAuthenticationService): AuthenticationService
}