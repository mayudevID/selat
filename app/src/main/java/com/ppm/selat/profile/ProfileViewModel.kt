package com.ppm.selat.profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ppm.selat.core.domain.usecase.AuthUseCase
import kotlinx.coroutines.flow.MutableStateFlow

class ProfileViewModel(private val authUseCase: AuthUseCase) : ViewModel() {
    var photoFlow = MutableStateFlow(Uri.parse(""))

    val userDataStream = authUseCase.getUserStream().asLiveData()
    fun logoutFromFirebase() = authUseCase.logoutFromFirebase().asLiveData()
    fun saveNewProfile() = authUseCase.updatePhoto(photoFlow.value).asLiveData()
}