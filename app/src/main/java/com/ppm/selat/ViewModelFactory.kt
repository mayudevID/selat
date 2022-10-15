package com.ppm.selat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ppm.selat.auth.LoginViewModel
import com.ppm.selat.auth.RegisterViewModel
import com.ppm.selat.core.domain.usecase.AuthUseCase
import com.ppm.selat.di.AppScope
import com.ppm.selat.home.HomeViewModel
import com.ppm.selat.profile.ProfileViewModel
import com.ppm.selat.splash.SplashViewModel
import javax.inject.Inject

@AppScope
class ViewModelFactory @Inject constructor(private val authUseCase: AuthUseCase) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        when {
            modelClass.isAssignableFrom(SplashViewModel::class.java) -> {
                SplashViewModel(authUseCase) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(authUseCase) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(authUseCase) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(authUseCase) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(authUseCase) as T
            }
            else -> throw Throwable("Unknown ViewModel class: " + modelClass.name)
        }
}