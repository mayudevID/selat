package com.ppm.selat.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ppm.selat.core.domain.usecase.AuthUseCase

class SplashViewModel(private val authUseCase: AuthUseCase) : ViewModel() {
    fun disablePersistence() = authUseCase.disablePersistence()
    val isUserSigned = authUseCase.isUserSigned().asLiveData()
}