package com.ppm.selat.core.data.source.remote

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.ppm.selat.core.data.Resource
import com.ppm.selat.core.data.source.remote.response.FirebaseResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

class AuthDataSource (private val firebaseAuth: FirebaseAuth){
    suspend fun loginToFirebase(email: String, password: String): Flow<FirebaseResponse<AuthResult>> {
        return flow {
            try {
                val authResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
                emit(FirebaseResponse.Success(authResult))
            } catch (e: FirebaseAuthException) {
                emit(FirebaseResponse.Error(e.errorCode))
            }
        }
    }

    suspend fun registerToFirebase(email: String, password: String) : Flow<FirebaseResponse<AuthResult>> {
        return flow {
            try {
                val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
                emit(FirebaseResponse.Success(authResult))
            } catch (e: FirebaseAuthException) {
                emit(FirebaseResponse.Error(e.errorCode))
            }
        }
    }

    fun isUserSigned() : Flow<Boolean> = flow {
        if (firebaseAuth.currentUser != null) {
            emit(true)
        } else {
            emit(false)
        }
    }

    suspend fun logoutFromFirebase() : Flow<FirebaseResponse<Boolean>> {
        return flow {
            try {
                firebaseAuth.signOut()
                emit(FirebaseResponse.Success(true))
            } catch (e: FirebaseAuthException){
                emit(FirebaseResponse.Error(e.message.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun resetPassword(email: String) : Flow<FirebaseResponse<Boolean>> {
        return flow {
            try {
                firebaseAuth.sendPasswordResetEmail(email).await()
                emit(FirebaseResponse.Success(true))
            } catch (e: FirebaseAuthException) {
                emit(FirebaseResponse.Error(e.message.toString()))
            }
        }
    }

    fun getUidUser() : String {
        return firebaseAuth.currentUser!!.uid
    }
}