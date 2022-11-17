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
import com.ppm.selat.core.utils.TypeDataEdit
import kotlinx.coroutines.cancel
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.*

class EditProfileViewModel(private val authUseCase: AuthUseCase) : ViewModel() {
    lateinit var editMode: TypeDataEdit
    lateinit var oldUserData: UserData
    var textValue: String = ""
    var dateBirth: String = ""
    var isDateChanged = MutableStateFlow(false)

    fun updateProfile() = authUseCase.updateProfile(
        editMode,
        if (textValue.isEmpty() || textValue == "") "Tidak ada data" else textValue
    ).asLiveData()

    fun getPassword() = authUseCase.getPassword().asLiveData()
}
