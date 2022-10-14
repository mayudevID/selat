package com.ppm.selat.core.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ppm.selat.auth.LoginViewModel
import com.ppm.selat.auth.RegisterViewModel
import com.ppm.selat.core.domain.usecase.AuthUseCase
import com.ppm.selat.di.AppScope
import javax.inject.Inject

@AppScope
class ViewModelAuthFactory @Inject constructor(private val authUseCase: AuthUseCase) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(authUseCase) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(authUseCase) as T
            }
            else -> throw Throwable("Unknown ViewModel class: " + modelClass.name)
        }
}