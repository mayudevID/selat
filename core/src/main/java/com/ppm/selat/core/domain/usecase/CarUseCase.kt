package com.ppm.selat.core.domain.usecase

import com.ppm.selat.core.utils.Manufacturer
import com.ppm.selat.core.utils.TypeCar
import com.ppm.selat.core.data.Resource
import com.ppm.selat.core.domain.model.Car
import kotlinx.coroutines.flow.Flow

interface CarUseCase {
    fun getAllCars() : Flow<Resource<List<Car>>>
    fun getCarDataByParams(manufacturer: Manufacturer, typeCar: TypeCar) : Flow<Resource<List<Car>>>
    fun getAvailableCar(carId: String) : Flow<Int>
    fun getCarBySearch(carName: String) : Flow<Resource<List<Car>>>
    fun getSingleDataCar(id: String) : Flow<Resource<Car>>
}