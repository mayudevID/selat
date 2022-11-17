package com.ppm.selat.set_pin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ppm.selat.core.domain.usecase.AuthUseCase
import kotlinx.coroutines.flow.MutableStateFlow

class SetPinViewModel(private val authUseCase: AuthUseCase) : ViewModel() {
    val PIN_TEMP = MutableStateFlow("")
    val PIN_NEW = MutableStateFlow("")
    var pinEmpty = MutableStateFlow(true)

    fun checkPIN() = authUseCase.getPIN().asLiveData()
    fun sendPIN() = authUseCase.setPIN(PIN_NEW.value).asLiveData()
}