package com.ppm.selat.core.domain.usecase

import com.ppm.selat.core.data.Resource
import com.ppm.selat.core.domain.repository.IAuthRepository
import com.ppm.selat.core.domain.repository.ICarRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthInteractor @Inject constructor(private val authRepository: IAuthRepository): AuthUseCase {
    override fun loginToFirebase(email: String, password: String) = authRepository.loginToFirebase(email, password)
    override fun registerToFirebase(name: String, email: String, password: String) = authRepository.registerToFirebase(name, email, password)
}