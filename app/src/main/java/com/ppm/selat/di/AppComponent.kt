package com.ppm.selat.di

import com.ppm.selat.splash.SplashActivity
import com.ppm.selat.auth.LoginActivity
import com.ppm.selat.auth.RegisterActivity
import com.ppm.selat.core.di.CoreComponent
import com.ppm.selat.home.HomeActivity
import com.ppm.selat.profile.ProfileActivity
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

    fun inject(activity: SplashActivity)
    fun inject(activity: LoginActivity)
    fun inject(activity: RegisterActivity)
    fun inject(activity: HomeActivity)
    fun inject(activity: ProfileActivity)
}