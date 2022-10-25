package com.ppm.selat.auth.reset_password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ppm.selat.core.domain.usecase.AuthUseCase
import com.ppm.selat.core.utils.emailPattern
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow

class ResetPasswordViewModel(private val authUseCase: AuthUseCase) : ViewModel() {
    var emailInput = MutableStateFlow("")
    var emailIsValid = MutableStateFlow(true)

    fun resetPassword() = authUseCase.resetPassword(emailInput.value).asLiveData()
}