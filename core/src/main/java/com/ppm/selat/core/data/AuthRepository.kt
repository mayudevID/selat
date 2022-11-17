package com.ppm.selat.core.data

import android.content.ContentResolver
import android.content.res.Resources
import android.net.Uri
import android.util.Log
import com.ppm.selat.core.R
import com.ppm.selat.core.data.source.local.UserLocalDataSource
import com.ppm.selat.core.data.source.remote.AuthDataSource
import com.ppm.selat.core.data.source.remote.UserFirestoreDataSource
import com.ppm.selat.core.data.source.remote.StorageDataSource
import com.ppm.selat.core.data.source.remote.network.FirebaseResponse
import com.ppm.selat.core.domain.model.LoginData
import com.ppm.selat.core.domain.model.RegisterData
import com.ppm.selat.core.domain.model.UserData
import com.ppm.selat.core.domain.repository.IAuthRepository
import com.ppm.selat.core.utils.TypeDataEdit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class AuthRepository(
    private val authDataSource: AuthDataSource,
    private val userLocalDataSource: UserLocalDataSource,
    private val userFirestoreDataSource: UserFirestoreDataSource,
    private val storageDataSource: StorageDataSource,
    private val resources: Resources,
) : IAuthRepository {

    override fun loginToFirebase(loginData: LoginData): Flow<Resource<Boolean>> {
        return flow {
            emit(Resource.Loading())
            Log.d("LoginActivity", "Loading 1")
            val result = authDataSource.loginToFirebase(loginData.email, loginData.password)
            when (val firebaseResponse = result.first()) {
                is FirebaseResponse.Success -> {
                    emit(Resource.Loading())
                    val userData =
                        userFirestoreDataSource.getUserDataFromFirestore(
                            firebaseResponse.data.user!!.uid,
                            loginData
                        )
                    when (val dataResult = userData.first()) {
                        is FirebaseResponse.Success -> {
                            emit(Resource.Loading())
                            val dataTemp = UserData(
                                id = dataResult.data.id,
                                name = dataResult.data["name"].toString(),
                                email = dataResult.data["email"].toString(),
                                photoUrl = dataResult.data["photoUrl"].toString(),
                                phone = dataResult.data["phone"].toString(),
                                placeDateOfBirth = dataResult.data["pdob"].toString(),
                                address = dataResult.data["address"].toString(),
                                job = dataResult.data["job"].toString()
                            )
                            Log.d("LoginActivity", dataTemp.toString())
                            val saveResult = userLocalDataSource.saveUserData(dataTemp)
                            when (val getResource = saveResult.first()) {
                                is Resource.Success -> {
                                    emit(Resource.Success(true))
                                }
                                is Resource.Error -> {
                                    emit(Resource.Error(getResource.message.toString()))
                                }
                                is Resource.Loading -> {

                                }
                            }
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

    override fun registerToFirebase(registerData: RegisterData): Flow<Resource<Boolean>> {
        return flow {
            emit(Resource.Loading())
            Log.d("RegisterActivity", "Data Register: $registerData")
            val result =
                authDataSource.registerToFirebase(registerData.email, registerData.password)
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
                    val uploadProfilePicture = storageDataSource.uploadProfilePicture(
                        targetPath, firebaseResponse.data.user!!.uid
                    )
                    when (val resultUpload = uploadProfilePicture.first()) {
                        is FirebaseResponse.Success -> {
                            emit(Resource.Loading())
                            // Make UserData
                            val newUserData = UserData(
                                id = firebaseResponse.data.user!!.uid,
                                name = registerData.name,
                                email = registerData.email,
                                photoUrl = resultUpload.data,
                                phone = "Tidak ada data",
                                placeDateOfBirth = "Tidak ada data",
                                address = "Tidak ada data",
                                job = "Tidak ada data"
                            )
                            // UserData to Firestore
                            val save =
                                userFirestoreDataSource.createUserDataToFirestore(
                                    newUserData,
                                    registerData,
                                )
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
                            emit(Resource.Error(resultUpload.errorMessage))
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

    override fun logoutFromFirebase(): Flow<Resource<Boolean>> {
        return flow {
            emit(Resource.Loading())
            val clearData = userLocalDataSource.removeUserData()
            when (val result = clearData.first()) {
                is Resource.Success -> {
                    emit(Resource.Loading())
                    val resultLogout = authDataSource.logoutFromFirebase()
                    when (val result = resultLogout.first()) {
                        is FirebaseResponse.Success -> {
                            emit(Resource.Success(true))
                        }
                        is FirebaseResponse.Error -> {
                            emit(Resource.Error(result.errorMessage))
                        }
                        is FirebaseResponse.Empty -> {}
                    }
                }
                is Resource.Error -> {
                    emit(Resource.Error(result.message.toString()))
                }
                is Resource.Loading -> {
                    emit(Resource.Loading())
                }
            }
        }
    }

    override fun updateProfile(typeDataEdit: TypeDataEdit, data: String): Flow<Resource<Boolean>> {
        return flow {
            emit(Resource.Loading())
            val uid = authDataSource.getUidUser()
            val dataRes = userFirestoreDataSource.updateProfile(typeDataEdit, data, uid)
            when (val result = dataRes.first()) {
                is FirebaseResponse.Success -> {
                    emit(Resource.Loading())
                    val oldUserData = userLocalDataSource.getSingleUserData()
                    val newUserData = UserData(
                        id = oldUserData.id,
                        name = if (typeDataEdit == TypeDataEdit.NAME) data else oldUserData.name,
                        email = if (typeDataEdit == TypeDataEdit.EMAIL) data else oldUserData.email,
                        photoUrl = oldUserData.photoUrl,
                        phone = if (typeDataEdit == TypeDataEdit.PHONE) data else oldUserData.phone,
                        placeDateOfBirth = if (typeDataEdit == TypeDataEdit.PDOB) data else oldUserData.placeDateOfBirth,
                        address = if (typeDataEdit == TypeDataEdit.ADDRESS) data else oldUserData.address,
                        job = if (typeDataEdit == TypeDataEdit.JOB) data else oldUserData.job,
                    )
                    val saveLocal = userLocalDataSource.saveUserData(newUserData)
                    when (val resultSaveLocal = saveLocal.first()) {
                        is Resource.Success -> {
                            if (typeDataEdit == TypeDataEdit.EMAIL) {
                                val resultChangeEmail = authDataSource.changeEmail(data)
                                when (val resultChange = resultChangeEmail.first()) {
                                    is FirebaseResponse.Success -> {
                                        emit(Resource.Success(true))
                                    }
                                    is FirebaseResponse.Error -> {
                                        emit(Resource.Error(resultChange.errorMessage))
                                    }
                                    else -> {}
                                }
                            } else {
                                emit(Resource.Success(true))
                            }
                        }
                        is Resource.Error -> {
                            emit(Resource.Error(resultSaveLocal.message.toString()))
                        }
                        is Resource.Loading -> {}
                    }
                }
                is FirebaseResponse.Error -> {
                    emit(Resource.Error(result.errorMessage))
                }
                is FirebaseResponse.Empty -> {}
            }
        }
    }

    override fun updatePhoto(photo: Uri): Flow<Resource<String>> {
        return flow {
            emit(Resource.Loading())
            val uid = authDataSource.getUidUser()
            val userDataTemp = userLocalDataSource.getSingleUserData()
            val deletePhoto = storageDataSource.deleteProfilePicture(userDataTemp.photoUrl!!, uid)
            when (val resultDelete = deletePhoto.first()) {
                is FirebaseResponse.Success -> {
                    emit(Resource.Loading())
                    val uploadStorage = storageDataSource.uploadProfilePicture(photo, uid)
                    when (val resultUpload = uploadStorage.first()) {
                        is FirebaseResponse.Success -> {
                            emit(Resource.Loading())
                            val dataRes =
                                userFirestoreDataSource.updatePhoto(resultUpload.data, uid)
                            when (val result = dataRes.first()) {
                                is FirebaseResponse.Success -> {
                                    emit(Resource.Loading())
                                    val oldUserData = userLocalDataSource.getSingleUserData()
                                    val newUserData = UserData(
                                        id = oldUserData.id,
                                        name = oldUserData.name,
                                        email = oldUserData.email,
                                        photoUrl = resultUpload.data,
                                        phone = oldUserData.phone,
                                        placeDateOfBirth = oldUserData.placeDateOfBirth,
                                        address = oldUserData.placeDateOfBirth,
                                        job = oldUserData.job
                                    )
                                    val saveLocal = userLocalDataSource.saveUserData(newUserData)
                                    when (val resultSaveLocal = saveLocal.first()) {
                                        is Resource.Success -> {
                                            emit(Resource.Success("true"))
                                        }
                                        is Resource.Error -> {
                                            emit(Resource.Error(resultSaveLocal.message.toString()))
                                        }
                                        is Resource.Loading -> {}
                                    }
                                }
                                is FirebaseResponse.Error -> {
                                    emit(Resource.Error(result.errorMessage))
                                }
                                is FirebaseResponse.Empty -> {
                                }
                            }
                        }
                        is FirebaseResponse.Error -> {
                            emit(Resource.Error(resultUpload.errorMessage))
                        }
                        is FirebaseResponse.Empty -> {}
                    }
                }
                is FirebaseResponse.Error -> {
                    emit(Resource.Error(resultDelete.errorMessage))
                }
                is FirebaseResponse.Empty -> {}
            }
        }
    }

    override fun saveNewUserData(user: UserData): Flow<Resource<Boolean>> {
        return flow {
            emit(Resource.Loading())
            val saveResult = userLocalDataSource.saveUserData(user)
            when (val getResource = saveResult.first()) {
                is Resource.Success -> {
                    emit(Resource.Success(true))
                }
                is Resource.Error -> {
                    emit(Resource.Error(getResource.message.toString()))
                }
                is Resource.Loading -> {

                }
            }
        }
    }

    override fun resetPassword(email: String): Flow<Resource<Boolean>> {
        return flow {
            emit(Resource.Loading())
            val resetPassword = authDataSource.resetPassword(email)
            when (val resetResult = resetPassword.first()) {
                is FirebaseResponse.Success -> {
                    emit(Resource.Success(true))
                }
                is FirebaseResponse.Error -> {
                    emit(Resource.Error(resetResult.errorMessage))
                }
                is FirebaseResponse.Empty -> {}
            }
        }
    }

    override fun getPassword(): Flow<Resource<String>> {
        return flow {
            emit(Resource.Loading())
            val getData = userLocalDataSource.getPassword()
            when (val result = getData.first()) {
                is Resource.Success -> {
                    emit(Resource.Success(result.data!!))
                }
                is Resource.Error -> {
                    emit(Resource.Error(result.message.toString()))
                }
                is Resource.Loading -> {

                }
            }
        }
    }

    override fun getPIN(): Flow<Resource<String>> {
        return flow {
            emit(Resource.Loading())
            val getData = userFirestoreDataSource.getPIN(authDataSource.getUidUser())
            when (val result = getData.first()) {
                is FirebaseResponse.Success -> {
                    emit(Resource.Success(result.data))
                }
                is FirebaseResponse.Error -> {
                    emit(Resource.Error(result.errorMessage))
                }
                is FirebaseResponse.Empty -> {}
            }
        }
    }

    override fun setPIN(PIN: String): Flow<Resource<Boolean>> {
        return flow {
            emit(Resource.Loading())
            val getData = userFirestoreDataSource.setPIN(PIN, authDataSource.getUidUser())
            when (val result = getData.first()) {
                is FirebaseResponse.Success -> {
                    emit(Resource.Success(result.data))
                }
                is FirebaseResponse.Error -> {
                    emit(Resource.Error(result.errorMessage))
                }
                is FirebaseResponse.Empty -> {}
            }
        }
    }

    override fun getHistoryLogin(): Flow<Resource<List<List<String>>>> {
        return flow {
            emit(Resource.Loading())
            val getData = userFirestoreDataSource.getHistoryLogin(authDataSource.getUidUser())
            when (val result = getData.first()) {
                is FirebaseResponse.Success -> {
                    val dataDoc = result.data.data as Map<*,*>
                    val dataNew = ArrayList<List<String>>()
                    dataDoc.map {
                        val dataTemp = it.value as List<*>
                        dataNew.add(listOf(dataTemp[0].toString(),  dataTemp[1].toString()))
                    }
                    emit(Resource.Success(dataNew))
                }
                is FirebaseResponse.Error -> {
                    emit(Resource.Error(result.errorMessage))
                }
                is FirebaseResponse.Empty -> {}
            }
        }
    }

    override fun disablePersistence() = userFirestoreDataSource.disablePersistence()

    override fun getUserStream() = userLocalDataSource.getDataStream()

    override fun isUserSigned() = authDataSource.isUserSigned()
}