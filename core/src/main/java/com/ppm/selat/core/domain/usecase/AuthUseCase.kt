package com.ppm.selat.core.domain.usecase

import com.google.firebase.auth.FirebaseUser
import com.ppm.selat.core.data.Resource
import com.ppm.selat.core.domain.model.UserData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface AuthUseCase {
    fun loginToFirebase(email: String, password: String) : Flow<Resource<Boolean>>
    fun registerToFirebase(name: String, email: String, password: String) : Flow<Resource<Boolean>>
    fun getUserStream() : MutableStateFlow<UserData>
    fun isUserSigned() : Flow<Boolean>
    fun logoutFromFirebase() : Flow<Resource<Boolean>>
}