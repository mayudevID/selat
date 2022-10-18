package com.ppm.selat.auth

import androidx.lifecycle.*
import com.ppm.selat.core.data.Resource
import com.ppm.selat.core.domain.usecase.AuthUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class LoginViewModel(private val authUseCase: AuthUseCase) : ViewModel() {
    val emailFlow = MutableStateFlow("")
    val passwordFlow = MutableStateFlow("")

    fun loginAccount(email: String, password: String): LiveData<Resource<Boolean>> {
        return authUseCase.loginToFirebase(email, password).asLiveData()
    }
}