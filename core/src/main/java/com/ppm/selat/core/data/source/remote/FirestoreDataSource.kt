package com.ppm.selat.core.data.source.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.ppm.selat.core.data.Resource
import com.ppm.selat.core.data.source.remote.response.FirebaseResponse
import com.ppm.selat.core.domain.model.UserData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

class FirestoreDataSource (
    private val firestore: FirebaseFirestore,
) {
    suspend fun getUserDataFromFirestore(uid: String): Flow<FirebaseResponse<DocumentSnapshot>> {
        return flow {
            try {
                val user = firestore.collection("users").document(uid).get().await()
                emit(FirebaseResponse.Success(user))
            } catch (e: FirebaseFirestoreException) {
                emit(FirebaseResponse.Error(e.message.toString()))
            }
        }
    }

    suspend fun createUserDataToFirestore(user: UserData): Flow<FirebaseResponse<Boolean>> {
        return flow {
            try {
                val userData = hashMapOf(
                    "name" to user.name,
                    "email" to user.email,
                    "photoUrl" to user.photoUrl
                )
                firestore.collection("users").document(user.id as String).set(userData).await()
                emit(FirebaseResponse.Success(true))
            } catch (e: FirebaseFirestoreException) {
                emit(FirebaseResponse.Error(e.message.toString()))
            }
        }
    }

    suspend fun updateName(name: String, uid: String): Flow<Resource<Boolean>> {
        return flow {
            emit(Resource.Loading())
            try {
                val nameData = mapOf(
                    "name" to name,
                )
                firestore.collection("users").document(uid).update(nameData).await()
                emit(Resource.Success(true))
            } catch (e: FirebaseFirestoreException) {
                emit(Resource.Error(e.message.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun updatePhoto(photoUrl: String, uid: String): Flow<Resource<Boolean>> {
        return flow {
            emit(Resource.Loading())
            try {
                val nameData = mapOf(
                    "photoUrl" to photoUrl,
                )
                firestore.collection("users").document(uid).update(nameData).await()
                emit(Resource.Success(true))
            } catch (e: FirebaseFirestoreException) {
                emit(Resource.Error(e.message.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }
}