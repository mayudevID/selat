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
        }
    }

    suspend fun registerToFirebase(email: String, password: String) : Flow<FirebaseResponse<String>> {
        return flow {
            try {
                val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
                emit(FirebaseResponse.Success(authResult.user?.uid.toString()))
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

    suspend fun logoutFromFirebase() : Flow<Resource<Boolean>> {
        return flow {
            try {
                firebaseAuth.signOut()
                emit(Resource.Success(true))
            } catch (e: Exception){
                emit(Resource.Error(e.message.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }
}