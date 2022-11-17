package com.ppm.selat.change_password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ppm.selat.core.domain.usecase.AuthUseCase
import kotlinx.coroutines.flow.first

class ChangePasswordViewModel(private val authUseCase: AuthUseCase): ViewModel() {
    fun getUserNow() = authUseCase.getUserStream()
    fun resetPassword(email: String) = authUseCase.resetPassword(email).asLiveData()
}