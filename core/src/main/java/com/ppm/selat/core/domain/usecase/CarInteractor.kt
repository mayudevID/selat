package com.ppm.selat.core.domain.usecase

import com.ppm.selat.core.data.Resource
import com.ppm.selat.core.domain.model.Car
import com.ppm.selat.core.domain.repository.ICarRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CarInteractor (private val carRepository: ICarRepository): CarUseCase {
    override fun getAllCars() = carRepository.getAllCars()
}