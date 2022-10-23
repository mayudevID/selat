package com.ppm.selat.core.domain.repository

import android.net.Uri
import com.google.firebase.auth.FirebaseUser
import com.ppm.selat.core.data.Resource
import com.ppm.selat.core.domain.model.Car
import com.ppm.selat.core.domain.model.UserData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface IAuthRepository {
    fun loginToFirebase(email: String, password: String) : Flow<Resource<Boolean>>
    fun registerToFirebase(name: String, email: String, password: String) : Flow<Resource<Boolean>>
    fun getUserStream() : MutableStateFlow<UserData>
    fun isUserSigned() : Flow<Boolean>
    fun logoutFromFirebase() : Flow<Resource<Boolean>>
    fun updateName(name: String) : Flow<Resource<Boolean>>
    fun updateEmail(email: String) : Flow<Resource<Boolean>>
    fun updatePhone(phone: String) : Flow<Resource<Boolean>>
    fun updatePhoto(photo: Uri) : Flow<Resource<String>>
    fun saveNewUserData(user: UserData): Flow<Resource<Boolean>>
    fun resetPassword(email: String): Flow<Resource<Boolean>>
}