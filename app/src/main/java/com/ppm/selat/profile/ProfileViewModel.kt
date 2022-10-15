package com.ppm.selat.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ppm.selat.core.domain.usecase.AuthUseCase

class ProfileViewModel(private val authUseCase: AuthUseCase) : ViewModel() {
    val userDataStream = authUseCase.getUserStream().asLiveData()
    fun logoutFromFirebase() = authUseCase.logoutFromFirebase().asLiveData()
}