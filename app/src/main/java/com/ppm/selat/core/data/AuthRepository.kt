package com.ppm.selat.core.data

import com.ppm.selat.core.data.source.local.UserLocalDataSource
import com.ppm.selat.core.data.source.remote.AuthDataSource
import com.ppm.selat.core.data.source.remote.FirestoreDataSource
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
    private val firestoreDataSource: FirestoreDataSource
) : IAuthRepository {
    override fun loginToFirebase(email: String, password: String): Flow<Resource<Boolean>> {
        return flow {
            emit(Resource.Loading())
            val result = authDataSource.loginToFirebase(email, password)
            when (val firebaseResponse = result.first()) {
                is FirebaseResponse.Success -> {
                    val userData = firebaseResponse.data.user?.let {
                        firestoreDataSource.getUserDataFromFirestore(
                            it.uid)
                    }
                    when (val dataResult = userData?.first()) {
                        is Resource.Success -> {
                            val dataTemp = UserData(
                                id = dataResult.data?.id,
                                name = dataResult.data?.get("name") as String?,
                                email = dataResult.data?.get("email") as String?,
                                photoUrl = dataResult.data?.get("photoUrl") as String?,
                            )
                           when (val last = userLocalDataSource.saveUserData(dataTemp).first()) {
                               is Resource.Success -> {
                                   emit(Resource.Success(true))
                               }
                               is Resource.Error -> {
                                   emit(Resource.Error(last.message.toString()))
                               }
                               is Resource.Loading -> {
                                   emit(Resource.Loading())
                               }
                           }
                        }
                        is Resource.Error -> {
                            emit(Resource.Error(dataResult.message.toString()))
                        }
                        is Resource.Loading -> {
                            emit(Resource.Loading())
                        }
                        else -> {
                            emit(Resource.Error("Data tidak ditemukan"))
                        }
                    }
                }
                is FirebaseResponse.Error -> {
                    emit(Resource.Error(firebaseResponse.errorMessage))
                }
                is FirebaseResponse.Empty -> {

                }
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
//            val result = authDataSource.loginToRegister(email, password)
//            when (val firebaseResponse = result.first()) {
//                is FirebaseResponse.Success ->  {
//                    emit(Resource.Success(firebaseResponse.data))
//                }
//                is FirebaseResponse.Error -> {
//                    emit(Resource.Error(firebaseResponse.errorMessage))
//                }
//                is FirebaseResponse.Empty -> {
//
//                }
//            }
        }
    }
}