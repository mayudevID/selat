package com.ppm.selat.core.data.source.remote

import com.google.firebase.firestore.*
import com.ppm.selat.core.data.source.remote.response.FirebaseResponse
import com.ppm.selat.core.data.source.snapshotFlow
import com.ppm.selat.core.domain.model.LoginData
import com.ppm.selat.core.domain.model.RegisterData
import com.ppm.selat.core.domain.model.UserData
import com.ppm.selat.core.utils.TypeDataEdit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import java.time.Instant
import kotlin.math.log


class UserFirestoreDataSource(private val firestore: FirebaseFirestore) {
    private lateinit var userDb: CollectionReference
    private lateinit var pinDb: CollectionReference
    private lateinit var passwordDb: CollectionReference
    private lateinit var historyLoginDb: CollectionReference

    private fun initFirestore() {
        userDb = firestore.collection("users")
        pinDb = firestore.collection("PIN")
        passwordDb = firestore.collection("password")
        historyLoginDb = firestore.collection("history_login")
    }

    suspend fun getUserDataFromFirestore(
        uid: String, loginData: LoginData
    ): Flow<FirebaseResponse<DocumentSnapshot>> {
        return flow {
            try {
                val user = userDb.document(uid).get().await()
                historyLoginDb.document(uid).update(
                    mapOf(
                        System.currentTimeMillis().toString() to listOf(loginData.lastLogin, loginData.deviceData)
                    )
                ).await()
                emit(FirebaseResponse.Success(user))
            } catch (e: FirebaseFirestoreException) {
                emit(FirebaseResponse.Error(e.message.toString()))
            }
        }
    }

    suspend fun createUserDataToFirestore(
        user: UserData, registerData: RegisterData,
    ): Flow<FirebaseResponse<Boolean>> {
        return flow {
            val id = user.id!!
            try {
                val userData = mapOf(
                    "name" to user.name,
                    "email" to user.email,
                    "photoUrl" to user.photoUrl,
                    "phone" to user.phone,
                    "pdob" to user.placeDateOfBirth,
                    "job" to user.job,
                    "address" to user.address
                )
                userDb.document(id).set(userData).await()
                pinDb.document(id).set(mapOf("PIN" to registerData.PIN)).await()
                passwordDb.document(id).set(mapOf("password" to registerData.password)).await()
                emit(FirebaseResponse.Success(true))
            } catch (e: FirebaseFirestoreException) {
                emit(FirebaseResponse.Error(e.message.toString()))
            }
        }
    }

    suspend fun updateProfile(
        typeDataEdit: TypeDataEdit, dataChange: String, uid: String
    ): Flow<FirebaseResponse<Boolean>> {
        return flow {
            try {
                lateinit var dataMap: Map<String, String>
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
                if (typeDataEdit == TypeDataEdit.EMAIL) {

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

    suspend fun getPassword(uid: String): Flow<FirebaseResponse<String>> {
        return flow {
            try {
                val result = passwordDb.document(uid).get().await()
                emit(FirebaseResponse.Success(result["password"].toString()))
            } catch (e: FirebaseFirestoreException) {
                emit(FirebaseResponse.Error(e.message.toString()))
            }
        }
    }

    suspend fun getPIN(uid: String): Flow<FirebaseResponse<String>> {
        return flow {
            try {
                val result = pinDb.document(uid).get().await()
                emit(FirebaseResponse.Success(result["PIN"].toString()))
            } catch (e: FirebaseFirestoreException) {
                emit(FirebaseResponse.Error(e.message.toString()))
            }
        }
    }

    suspend fun getHistoryLogin(uid: String): Flow<FirebaseResponse<DocumentSnapshot>> {
        return flow {
            try {
                val result = historyLoginDb.document(uid).get().await()
                emit(FirebaseResponse.Success(result))
            } catch (e: FirebaseFirestoreException) {
                emit(FirebaseResponse.Error(e.message.toString()))
            }
        }
    }

    fun disablePersistence(): Boolean {
        val settings = FirebaseFirestoreSettings.Builder().setPersistenceEnabled(false).build()
        firestore.firestoreSettings = settings
        initFirestore()
        return true
    }
}