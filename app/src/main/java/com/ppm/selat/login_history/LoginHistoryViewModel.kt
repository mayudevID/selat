package com.ppm.selat.login_history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ppm.selat.core.domain.usecase.AuthUseCase

class LoginHistoryViewModel(private val authUseCase: AuthUseCase) : ViewModel() {
    fun getHistoryLogin() = authUseCase.getHistoryLogin().asLiveData()
}