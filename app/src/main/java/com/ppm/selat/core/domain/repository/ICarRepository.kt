package com.ppm.selat.core.domain.repository

import com.ppm.selat.core.data.Resource
import com.ppm.selat.core.domain.model.Car
import kotlinx.coroutines.flow.Flow

interface ICarRepository {
    fun getCarByType() : Flow<Resource<List<Car>>>
}