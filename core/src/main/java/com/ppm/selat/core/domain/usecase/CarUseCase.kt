package com.ppm.selat.core.domain.usecase

import com.ppm.selat.core.data.Resource
import com.ppm.selat.core.domain.model.Car
import kotlinx.coroutines.flow.Flow

interface CarUseCase {
    fun getAllCars() : Flow<Resource<List<Car>>>
}