package com.ppm.selat.core.data.source.remote

import com.google.firebase.firestore.*
import com.ppm.selat.core.data.source.remote.response.FirebaseResponse
import com.ppm.selat.core.domain.model.UserData
import com.ppm.selat.core.utils.TypeDataEdit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await


class UserFirestoreDataSource(private val firestore: FirebaseFirestore) {
    private var userDb: CollectionReference = firestore.collection("users")

    suspend fun getUserDataFromFirestore(uid: String): Flow<FirebaseResponse<DocumentSnapshot>> {
        return flow {
            try {
                val user = userDb.document(uid).get().await()
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
                    "photoUrl" to user.photoUrl,
                    "phone" to user.phone,
                    "pdob" to user.placeDateOfBirth,
                    "job" to user.job,
                    "address" to user.address
                )
                userDb.document(user.id as String).set(userData).await()
                emit(FirebaseResponse.Success(true))
            } catch (e: FirebaseFirestoreException) {
                emit(FirebaseResponse.Error(e.message.toString()))
            }
        }
    }

    suspend fun updateProfile(typeDataEdit: TypeDataEdit, dataChange: String, uid: String): Flow<FirebaseResponse<Boolean>> {
        return flow {
            try {
                lateinit var dataMap : Map<String, String>
                when (typeDataEdit) {
                    TypeDataEdit.NAME -> {
                        dataMap = mapOf(
                            "name" to dataChange,
                        )
                    }
                    TypeDataEdit.PDOB -> {
                        dataMap = mapOf(
                            "pdob" to dataChange,
                        )
                    }
                    TypeDataEdit.EMAIL -> {
                        dataMap = mapOf(
                            "email" to dataChange,
                        )
                    }
                    TypeDataEdit.PHONE -> {
                        dataMap = mapOf(
                            "phone" to dataChange,
                        )
                    }
                    TypeDataEdit.JOB -> {
                        dataMap = mapOf(
                            "job" to dataChange,
                        )
                    }
                    TypeDataEdit.ADDRESS -> {
                        dataMap = mapOf(
                            "address" to dataChange,
                        )
                    }
                }
                userDb.document(uid).update(dataMap).await()
                emit(FirebaseResponse.Success(true))
            } catch (e: FirebaseFirestoreException) {
                emit(FirebaseResponse.Error(e.message.toString()))
            }
        }
    }

    suspend fun updatePhoto(photoUrl: String, uid: String): Flow<FirebaseResponse<Boolean>> {
        return flow {
            try {
                val nameData = mapOf(
                    "photoUrl" to photoUrl,
                )
                userDb.document(uid).update(nameData).await()
                emit(FirebaseResponse.Success(true))
            } catch (e: FirebaseFirestoreException) {
                emit(FirebaseResponse.Error(e.message.toString()))
            }
        }
    }

    fun disablePersistence() : Boolean {
        val settings = FirebaseFirestoreSettings.Builder().setPersistenceEnabled(false).build()
        firestore.firestoreSettings = settings
        return true
    }
}