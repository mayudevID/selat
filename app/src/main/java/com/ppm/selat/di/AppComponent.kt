package com.ppm.selat.di

import android.content.Context
import com.ppm.selat.auth.LoginActivity
import com.ppm.selat.auth.RegisterActivity
import com.ppm.selat.core.di.CoreComponent
import dagger.Component

@AppScope
@Component(
    dependencies = [CoreComponent::class],
    modules = [AppModule::class]
)
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(coreComponent: CoreComponent): AppComponent
    }

    fun inject(activity: LoginActivity)
    fun inject(activity: RegisterActivity)
}