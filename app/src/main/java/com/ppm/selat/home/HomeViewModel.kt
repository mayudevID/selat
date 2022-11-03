package com.ppm.selat.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.ppm.selat.core.domain.usecase.AuthUseCase
import com.ppm.selat.core.domain.usecase.CarUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val authUseCase: AuthUseCase, private val carUseCase: CarUseCase) :
    ViewModel() {

    val userDataStream = authUseCase.getUserStream().asLiveData()
    val getAllCars = carUseCase.getAllCars().asLiveData()
}