package com.ppm.selat.edit_profile

import android.net.Uri
import android.provider.ContactsContract.CommonDataKinds.Email
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ppm.selat.core.data.Resource
import com.ppm.selat.core.domain.model.UserData
import com.ppm.selat.core.domain.usecase.AuthUseCase
import kotlinx.coroutines.cancel
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.*

class EditProfileViewModel(private val authUseCase: AuthUseCase) : ViewModel() {
    lateinit var nameInit: String
    var nameFlow = MutableStateFlow("")
    lateinit var emailInit: String
    var emailFlow = MutableStateFlow("")

    var photoIsChanged = MutableStateFlow(false)
    var photoFlow = MutableStateFlow(Uri.parse(""))
    private var newPhotoUrl = MutableStateFlow("")

    var emailChangedError = MutableStateFlow("")
    var nameChangedError = MutableStateFlow("")
    private var photoChangedError = MutableStateFlow("")

    fun checkNameIsChanged(): Boolean = nameInit != nameFlow.value
    fun checkEmailIsChanged(): Boolean = emailInit != emailFlow.value

    val userDataStream = authUseCase.getUserStream()
    fun saveNewName() = authUseCase.updateName(nameFlow.value).asLiveData()
    fun saveNewEmail() = authUseCase.updateEmail(emailFlow.value).asLiveData()
    fun saveNewProfile() = authUseCase.updatePhoto(photoFlow.value).asLiveData()
}
