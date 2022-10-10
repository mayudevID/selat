package com.ppm.selat.core.data

import com.ppm.selat.core.data.source.remote.AuthDataSource
import com.ppm.selat.core.data.source.remote.FirebaseResponse
import com.ppm.selat.core.data.source.remote.RemoteDataSource
import com.ppm.selat.core.domain.repository.IAuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(private val authDataSource: AuthDataSource) : IAuthRepository {
    override fun loginToFirebase(email: String, password: String): Flow<Resource<Boolean>> {
        return flow {
            emit(Resource.Loading())
            val result = authDataSource.loginToRegister(email, password)
            when (val firebaseResponse = result.first()) {
                is FirebaseResponse.Success ->  {
                    emit(Resource.Success(firebaseResponse.data))
                }
                is FirebaseResponse.Error -> {
                    emit(Resource.Error(firebaseResponse.errorMessage))
                }
                is FirebaseResponse.Empty -> {

                }
            }
        }
    }

    override fun registerToFirebase(name: String, email: String, password: String): Flow<Resource<Boolean>> {
        return flow {  }
    }
}