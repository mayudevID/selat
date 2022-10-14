package com.ppm.selat.core.data

import android.content.ContentResolver
import android.content.res.Resources
import android.net.Uri
import android.util.Log
import com.google.firebase.FirebaseException
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.ktx.Firebase
import com.ppm.selat.R
import com.ppm.selat.core.data.source.local.UserLocalDataSource
import com.ppm.selat.core.data.source.remote.AuthDataSource
import com.ppm.selat.core.data.source.remote.FirestoreDataSource
import com.ppm.selat.core.data.source.remote.StorageDataSource
import com.ppm.selat.core.data.source.remote.response.FirebaseResponse
import com.ppm.selat.core.domain.model.UserData
import com.ppm.selat.core.domain.repository.IAuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AuthRepository @Inject constructor(
    private val authDataSource: AuthDataSource,
    private val userLocalDataSource: UserLocalDataSource,
    private val firestoreDataSource: FirestoreDataSource,
    private val storageDataSource: StorageDataSource,
    private val resources: Resources,
) : IAuthRepository {

    override fun loginToFirebase(email: String, password: String): Flow<Resource<Boolean>> {
        return flow {
            emit(Resource.Loading())
            Log.d("LoginActivity", "Loading 1")
            val result = authDataSource.loginToFirebase(email, password)
            when (val firebaseResponse = result.first()) {
                is FirebaseResponse.Success -> {
                    emit(Resource.Loading())
                    Log.d("LoginActivity", "Loading 2")
                    val userData =
                        firestoreDataSource.getUserDataFromFirestore(firebaseResponse.data.user!!.uid)
                    when (val dataResult = userData.first()) {
                        is FirebaseResponse.Success -> {
                            emit(Resource.Loading())
                            Log.d("LoginActivity", "Loading 3")
                            Log.d("LoginActivity", dataResult.data.id)
                            Log.d("LoginActivity", dataResult.data["name"].toString())
                            Log.d("LoginActivity", dataResult.data["email"].toString())
                            Log.d("LoginActivity", dataResult.data["photoUrl"].toString())
                            val dataTemp = UserData(
                                id = dataResult.data.id,
                                name = dataResult.data["name"].toString(),
                                email = dataResult.data["email"].toString(),
                                photoUrl = dataResult.data["photoUrl"].toString(),
                            )
                            Log.d("LoginActivity", dataTemp.toString())
                            val saveResult = userLocalDataSource.saveUserData(dataTemp)
                            val getResource = saveResult.first()
                            if (getResource[0] == true) {
                                emit(Resource.Success(true))
                            } else {
                                emit(Resource.Error(getResource[1] as String))
                            }
//                            when (val last = saveResult.first()) {
//                                is (last[0] == true) -> {
//
//                                }
//                                is Resource.Error -> {
//                                    emit(Resource.Error(last.message.toString()))
//                                }
//                                is Resource.Loading -> {}
//                            }
                        }
                        is FirebaseResponse.Error -> {
                            emit(Resource.Error(dataResult.errorMessage))
                        }
                        else -> {
                            emit(Resource.Error("Data user tidak ditemukan"))
                        }
                    }
                }
                is FirebaseResponse.Error -> {
                    emit(Resource.Error(firebaseResponse.errorMessage))
                }
                is FirebaseResponse.Empty -> {}
            }
        }
    }

    override fun registerToFirebase(
        name: String,
        email: String,
        password: String
    ): Flow<Resource<Boolean>> {
        return flow {
            emit(Resource.Loading())
            val result = authDataSource.registerToFirebase(email, password)
            when (val firebaseResponse = result.first()) {
                is FirebaseResponse.Success -> {
                    emit(Resource.Loading())
                    // Get Image
                    val resId = R.drawable.temp_pp
                    val targetPath = Uri.parse(
                        ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + resources.getResourcePackageName(
                            resId
                        ) + '/' + resources.getResourceTypeName(resId) + '/' + resources.getResourceEntryName(
                            resId
                        )
                    )
                    // Firebase Upload to Storage FIRESTORE DATA STORE
                    val uploadProfilePicture = storageDataSource.uploadProfilePicture(targetPath)
                    when (val result = uploadProfilePicture.first()) {
                        is FirebaseResponse.Success -> {
                            emit(Resource.Loading())
                            // Make UserData
                            val newUserData = UserData(
                                id = firebaseResponse.data,
                                name = name,
                                email = email,
                                photoUrl = result.data
                            )
                            // UserData to Firestore
                            val save = firestoreDataSource.createUserDataToFirestore(newUserData)
                            when (val saveResult = save.first()) {
                                is FirebaseResponse.Success -> {
                                    emit(Resource.Success(true))
                                }
                                is FirebaseResponse.Error -> {
                                    emit(Resource.Error(saveResult.errorMessage))
                                }
                                is FirebaseResponse.Empty -> {}
                            }
                        }
                        is FirebaseResponse.Error -> {
                            emit(Resource.Error(result.errorMessage))
                        }
                        is FirebaseResponse.Empty -> {}
                    }
                }
                is FirebaseResponse.Error -> {
                    emit(Resource.Error(firebaseResponse.errorMessage))
                }
                is FirebaseResponse.Empty -> {
                    emit(Resource.Error("Data tidak ditemukan"))
                }
            }
        }
    }
}