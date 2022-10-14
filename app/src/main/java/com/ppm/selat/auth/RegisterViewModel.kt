package com.ppm.selat.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ppm.selat.core.data.Resource
import com.ppm.selat.core.domain.usecase.AuthUseCase

class RegisterViewModel(private val authUseCase: AuthUseCase) : ViewModel() {
    fun registerAccount(name: String, email: String, password: String): LiveData<Resource<Boolean>> {
        return authUseCase.registerToFirebase(name, email, password).asLiveData()
    }
}