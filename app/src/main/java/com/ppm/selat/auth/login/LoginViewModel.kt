package com.ppm.selat.auth.login

import androidx.lifecycle.*
import com.ppm.selat.core.data.Resource
import com.ppm.selat.core.domain.usecase.AuthUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class LoginViewModel(private val authUseCase: AuthUseCase) : ViewModel() {
    val emailFlow = MutableStateFlow("")
    val passwordFlow = MutableStateFlow("")

    fun loginAccount() = authUseCase.loginToFirebase(emailFlow.value, passwordFlow.value).asLiveData()
}