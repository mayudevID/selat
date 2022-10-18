package com.ppm.selat.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ppm.selat.core.data.Resource
import com.ppm.selat.core.domain.usecase.AuthUseCase
import kotlinx.coroutines.flow.MutableStateFlow

class RegisterViewModel(private val authUseCase: AuthUseCase) : ViewModel() {
    val nameFlow =  MutableStateFlow("")
    val emailFlow = MutableStateFlow("")
    val passwordFlow = MutableStateFlow("")
    val cPasswordFlow = MutableStateFlow("")

    fun registerAccount(name: String, email: String, password: String): LiveData<Resource<Boolean>> {
        return authUseCase.registerToFirebase(name, email, password).asLiveData()
    }

    fun logoutForLogin() = authUseCase.logoutFromFirebase().asLiveData()
}