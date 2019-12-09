package com.github.csandiego.pocaccount.dagger

import com.github.csandiego.pocaccount.protobuf.AuthenticationClient
import com.github.csandiego.pocaccount.protobuf.UserRegistrationClient
import com.squareup.wire.GrpcClient
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.Protocol
import javax.inject.Singleton

@Module
class WireRuntimeModule {

    @Singleton
    @Provides
    fun provideHttpClient() =
        OkHttpClient.Builder().protocols(listOf(Protocol.H2_PRIOR_KNOWLEDGE)).build()

    @Singleton
    @Provides
    fun provideGrpcClient(client: OkHttpClient) =
        GrpcClient.Builder().client(client).baseUrl("http://localhost:80").build()

    @Singleton
    @Provides
    fun provideUserRegistrationClient(client: GrpcClient) =
        client.create(UserRegistrationClient::class)

    @Singleton
    @Provides
    fun provideAuthenticationClient(client: GrpcClient) = client.create(AuthenticationClient::class)
}