package com.ppm.selat.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ppm.selat.core.domain.model.RegisterData
import com.ppm.selat.core.domain.usecase.AuthUseCase
import kotlinx.coroutines.flow.MutableStateFlow

class RegisterViewModel(private val authUseCase: AuthUseCase) : ViewModel() {
    val nameFlow = MutableStateFlow("")
    val emailFlow = MutableStateFlow("")
    val passwordFlow = MutableStateFlow("")
    val cPasswordFlow = MutableStateFlow("")
    val PIN = MutableStateFlow("")

    fun registerAccount() = authUseCase.registerToFirebase(
        RegisterData(
            name = nameFlow.value,
            email = emailFlow.value,
            password = passwordFlow.value,
            PIN = PIN.value
        )
    ).asLiveData()


    fun logoutForLogin() = authUseCase.logoutFromFirebase().asLiveData()
}