package com.ppm.selat.auth.login

import android.annotation.SuppressLint
import androidx.lifecycle.*
import com.ppm.selat.core.domain.model.LoginData
import com.ppm.selat.core.domain.usecase.AuthUseCase
import com.ppm.selat.core.utils.AESEncryption
import com.ppm.selat.core.utils.getDeviceName
import kotlinx.coroutines.flow.MutableStateFlow
import java.text.SimpleDateFormat
import java.util.*

class LoginViewModel(private val authUseCase: AuthUseCase) : ViewModel() {
    val emailFlow = MutableStateFlow("")
    val passwordFlow = MutableStateFlow("")

    @SuppressLint("SimpleDateFormat")
    private fun getDate(): String {
        val pattern = "E, dd MMM yyyy HH:mm:ss z"
        val simpleDateFormat = SimpleDateFormat(pattern)
        return simpleDateFormat.format(Date())
    }

    fun loginAccount() =
        authUseCase.loginToFirebase(
            LoginData(
                email = emailFlow.value,
                password = AESEncryption.encrypt(passwordFlow.value)!!,
                deviceData = getDeviceName() ?: "Unknown Device",
                lastLogin = getDate(),
            )
        )
            .asLiveData()
}