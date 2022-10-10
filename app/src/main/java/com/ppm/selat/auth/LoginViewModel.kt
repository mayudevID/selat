package com.ppm.selat.auth

import androidx.lifecycle.*
import com.ppm.selat.core.data.Resource
import com.ppm.selat.core.domain.usecase.AuthUseCase
import kotlinx.coroutines.flow.Flow

class LoginViewModel(private val authUseCase: AuthUseCase) : ViewModel() {
    fun loginAccount(email: String, password: String): LiveData<Resource<Boolean>> {
        return authUseCase.loginToFirebase(email, password).asLiveData()
    }
}