package com.ppm.selat.core.data.source.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthDataSource @Inject constructor(private val firebaseAuth: FirebaseAuth){
    suspend fun loginToRegister(email: String, password: String): Flow<FirebaseResponse<Boolean>> {
        return flow {
            try {
                firebaseAuth.signInWithEmailAndPassword(email, password).await()
                emit(FirebaseResponse.Success(true))
            } catch (e: FirebaseAuthException) {
                emit(FirebaseResponse.Error(e.errorCode))
            }
        }.flowOn(Dispatchers.IO)
    }
}