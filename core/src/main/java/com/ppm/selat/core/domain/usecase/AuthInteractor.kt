package com.ppm.selat.core.domain.usecase

import android.net.Uri
import com.google.firebase.auth.FirebaseUser
import com.ppm.selat.core.data.Resource
import com.ppm.selat.core.domain.model.LoginData
import com.ppm.selat.core.domain.model.RegisterData
import com.ppm.selat.core.domain.model.UserData
import com.ppm.selat.core.domain.repository.IAuthRepository
import com.ppm.selat.core.domain.repository.ICarRepository
import com.ppm.selat.core.utils.TypeDataEdit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class AuthInteractor(private val authRepository: IAuthRepository): AuthUseCase {
    override fun loginToFirebase(loginData: LoginData) = authRepository.loginToFirebase(loginData)
    override fun registerToFirebase(registerData: RegisterData) = authRepository.registerToFirebase(registerData)
    override fun getUserStream() = authRepository.getUserStream()
    override fun isUserSigned() = authRepository.isUserSigned()
    override fun logoutFromFirebase() = authRepository.logoutFromFirebase()
    override fun updateProfile(typeDataEdit: TypeDataEdit, date: String) = authRepository.updateProfile(typeDataEdit, date)
    override fun updatePhoto(photo: Uri) = authRepository.updatePhoto(photo)
    override fun saveNewUserData(user: UserData) = authRepository.saveNewUserData(user)
    override fun resetPassword(email: String) = authRepository.resetPassword(email)
    override fun getPassword() = authRepository.getPassword()
    override fun getPIN() = authRepository.getPIN()
    override fun setPIN(PIN: String) = authRepository.setPIN(PIN)
    override fun getHistoryLogin() = authRepository.getHistoryLogin()
    override fun disablePersistence() = authRepository.disablePersistence()
}