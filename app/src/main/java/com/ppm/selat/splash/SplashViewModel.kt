package com.ppm.selat.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.google.firebase.auth.FirebaseUser
import com.ppm.selat.core.domain.usecase.AuthUseCase

class SplashViewModel(private val authUseCase: AuthUseCase) : ViewModel() {
    fun disablePersistence() = authUseCase.disablePersistence()
    fun isUserSigned() = authUseCase.isUserSigned().asLiveData()
}