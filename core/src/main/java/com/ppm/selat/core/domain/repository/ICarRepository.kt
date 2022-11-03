package com.ppm.selat.core.domain.repository

import com.google.firebase.firestore.DocumentSnapshot
import com.ppm.selat.core.data.Resource
import com.ppm.selat.core.domain.model.Car
import kotlinx.coroutines.flow.Flow

interface ICarRepository {
    fun getAllCars() : Flow<Resource<List<Car>>>
    fun getAvailableCar(carId: String) : Flow<Int>
}