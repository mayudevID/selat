package com.ppm.selat.core.domain.usecase
import com.ppm.selat.core.data.Resource
import com.ppm.selat.core.domain.repository.ICarRepository
import kotlinx.coroutines.flow.Flow

class CarInteractor (private val carRepository: ICarRepository): CarUseCase {
    override fun getAllCars() = carRepository.getAllCars()
    override fun getAvailableCar(carId: String) = carRepository.getAvailableCar(carId)
}