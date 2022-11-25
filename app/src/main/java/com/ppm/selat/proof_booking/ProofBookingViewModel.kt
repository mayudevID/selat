package com.ppm.selat.proof_booking

import androidx.lifecycle.ViewModel
import com.ppm.selat.core.domain.usecase.AuthUseCase

class ProofBookingViewModel(private val authUseCase: AuthUseCase): ViewModel() {
    val getUser = authUseCase.getUserStream()
}