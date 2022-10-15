package com.ppm.selat.core.domain.repository

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
}