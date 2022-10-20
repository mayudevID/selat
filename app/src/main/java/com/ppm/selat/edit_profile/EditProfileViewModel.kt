package com.ppm.selat.edit_profile

import android.provider.ContactsContract.CommonDataKinds.Email
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ppm.selat.core.data.Resource
import com.ppm.selat.core.domain.usecase.AuthUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class EditProfileViewModel(private val authUseCase: AuthUseCase): ViewModel() {
    lateinit var nameInit: String
    var nameFlow = MutableStateFlow("")
    lateinit var emailInit: String
    var emailFlow = MutableStateFlow("")
    var photoIsChanged = MutableStateFlow(false)

    fun checkNameIsChanged() : Boolean = nameInit != nameFlow.value
    fun checkEmailIsChanged() : Boolean = emailInit != emailFlow.value

    val userDataStream = authUseCase.getUserStream()

    fun saveProfile() = flow<Resource<Boolean>> {
        emit(Resource.Loading())

        if (checkEmailIsChanged()) {
            val updateE = authUseCase.updateEmail(emailFlow.value)
            when (val resultE = updateE.first()) {
                is Resource.Success -> {
                }
                is Resource.Error -> {
                }
                is Resource.Loading -> {
                }
            }
        }

        if (checkNameIsChanged()) {
            val updateE = authUseCase.updateName(nameFlow.value)
            when (val resultE = updateE.first()) {
                is Resource.Success -> {
                }
                is Resource.Error -> {
                }
                is Resource.Loading -> {
                }
            }
        }

        if (photoIsChanged.value) {
            val updateE = authUseCase.updateName(nameFlow.value)
            when (val resultE = updateE.first()) {
                is Resource.Success -> {
                }
                is Resource.Error -> {
                }
                is Resource.Loading -> {
                }
            }
        }
    }.asLiveData()
}