package com.ppm.selat.core.domain.repository

import android.net.Uri
import com.google.firebase.auth.FirebaseUser
import com.ppm.selat.core.data.Resource
import com.ppm.selat.core.domain.model.Car
import com.ppm.selat.core.domain.model.LoginData
import com.ppm.selat.core.domain.model.RegisterData
import com.ppm.selat.core.domain.model.UserData
import com.ppm.selat.core.utils.TypeDataEdit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface IAuthRepository {
    fun loginToFirebase(loginData: LoginData) : Flow<Resource<Boolean>>
    fun registerToFirebase(registerData: RegisterData) : Flow<Resource<Boolean>>
    fun getUserStream() : MutableStateFlow<UserData>
    fun isUserSigned() : Flow<Boolean>
    fun logoutFromFirebase() : Flow<Resource<Boolean>>
    fun updateProfile(typeDataEdit: TypeDataEdit, date: String) : Flow<Resource<Boolean>>
    fun updatePhoto(photo: Uri) : Flow<Resource<String>>
    fun saveNewUserData(user: UserData): Flow<Resource<Boolean>>
    fun resetPassword(email: String): Flow<Resource<Boolean>>
    fun getPassword() : Flow<Resource<String>>
    fun getPIN() : Flow<Resource<String>>
    fun setPIN(PIN: String) : Flow<Resource<Boolean>>
    fun getHistoryLogin() : Flow<Resource<List<List<String>>>>
    fun disablePersistence() : Boolean
}