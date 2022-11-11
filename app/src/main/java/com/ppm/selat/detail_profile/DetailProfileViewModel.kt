package com.ppm.selat.detail_profile

import androidx.lifecycle.ViewModel
import com.ppm.selat.core.domain.usecase.AuthUseCase

class DetailProfileViewModel(private val authUseCase: AuthUseCase): ViewModel() {
    fun getDataStream() = authUseCase.getUserStream()
}