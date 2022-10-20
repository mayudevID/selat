package com.ppm.selat.core.domain.usecase

import android.net.Uri
import com.google.firebase.auth.FirebaseUser
import com.ppm.selat.core.data.Resource
import com.ppm.selat.core.domain.model.UserData
import com.ppm.selat.core.domain.repository.IAuthRepository
import com.ppm.selat.core.domain.repository.ICarRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class AuthInteractor(private val authRepository: IAuthRepository): AuthUseCase {
    override fun loginToFirebase(email: String, password: String) = authRepository.loginToFirebase(email, password)
    override fun registerToFirebase(name: String, email: String, password: String) = authRepository.registerToFirebase(name, email, password)
    override fun getUserStream() = authRepository.getUserStream()
    override fun isUserSigned() = authRepository.isUserSigned()
    override fun logoutFromFirebase() = authRepository.logoutFromFirebase()
    override fun updateName(name: String) = authRepository.updateName(name)
    override fun updateEmail(email: String) = authRepository.updateEmail(email)
    override fun updatePhone(phone: String) = authRepository.updatePhone(phone)
    override fun updatePhoto(photo: Uri) = authRepository.updatePhoto(photo)
}