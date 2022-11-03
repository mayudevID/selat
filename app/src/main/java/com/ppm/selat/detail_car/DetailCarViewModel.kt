package com.ppm.selat.detail_car

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.google.firebase.firestore.DocumentSnapshot
import com.ppm.selat.core.domain.usecase.CarUseCase

class DetailCarViewModel(private val carUseCase: CarUseCase) : ViewModel() {
    fun getAvailableCar(carId: String): LiveData<Int> {
        return carUseCase.getAvailableCar(carId).asLiveData()
    }
}