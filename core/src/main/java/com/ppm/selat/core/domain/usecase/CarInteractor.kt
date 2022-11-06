package com.ppm.selat.core.domain.usecase

import com.ppm.selat.core.data.Resource
import com.ppm.selat.core.domain.model.Car
import com.ppm.selat.core.utils.Manufacturer
import com.ppm.selat.core.utils.TypeCar
import com.ppm.selat.core.domain.repository.ICarRepository
import kotlinx.coroutines.flow.Flow

class CarInteractor(private val carRepository: ICarRepository) : CarUseCase {
    override fun getAllCars() = carRepository.getAllCars()
    override fun getCarDataByParams(
        manufacturer: Manufacturer,
        typeCar: TypeCar
    ) = carRepository.getCarDataByParams(manufacturer, typeCar)

    override fun getAvailableCar(carId: String) = carRepository.getAvailableCar(carId)
    override fun getCarBySearch(carName: String) = carRepository.getCarBySearch(carName)
}