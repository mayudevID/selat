package com.ppm.selat.core.data.source.remote

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.ppm.selat.core.data.source.remote.response.FirebaseResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthDataSource @Inject constructor(private val firebaseAuth: FirebaseAuth){
    suspend fun loginToFirebase(email: String, password: String): Flow<FirebaseResponse<AuthResult>> {
        return flow {
            try {
                val authResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
                emit(FirebaseResponse.Success(authResult))
            } catch (e: FirebaseAuthException) {
                emit(FirebaseResponse.Error(e.errorCode))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun registerToFirebase(name: String, email: String, password: String) : Flow<FirebaseResponse<Boolean>> {
        return flow {
            try {
                firebaseAuth.createUserWithEmailAndPassword(email, password).await()
                emit(FirebaseResponse.Success(true))
            } catch (e: FirebaseAuthException) {
                emit(FirebaseResponse.Error(e.errorCode))
            }
        }.flowOn(Dispatchers.IO)
    }
}