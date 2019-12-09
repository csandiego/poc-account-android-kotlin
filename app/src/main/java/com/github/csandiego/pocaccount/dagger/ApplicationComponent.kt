package com.github.csandiego.pocaccount.dagger

import com.github.csandiego.pocaccount.PocAccount
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidInjectionModule::class,
    ApplicationModule::class,
    SharedPreferencesModule::class,
    WireModule::class
])
interface ApplicationComponent : AndroidInjector<PocAccount> {

    @Component.Factory
    interface Factory : AndroidInjector.Factory<PocAccount>
}