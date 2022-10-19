package com.ppm.selat.edit_profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ppm.selat.core.domain.usecase.AuthUseCase
import kotlinx.coroutines.flow.MutableStateFlow

class EditProfileViewModel(private val authUseCase: AuthUseCase): ViewModel() {
    lateinit var nameInit: String
    var nameFlow = MutableStateFlow("")
    lateinit var emailInit: String
    var emailFlow = MutableStateFlow("")

    fun checkNameIsChanged() : Boolean {
        return nameInit != nameFlow.value
    }

    fun checkEmailIsChanged() : Boolean {
        return emailInit != emailFlow.value
    }

    val userDataStream = authUseCase.getUserStream()
}