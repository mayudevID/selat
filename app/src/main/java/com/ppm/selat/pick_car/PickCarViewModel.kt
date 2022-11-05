package com.ppm.selat.pick_car

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ppm.selat.core.utils.Manufacturer
import com.ppm.selat.core.utils.TypeCar
import com.ppm.selat.core.domain.usecase.CarUseCase
import kotlinx.coroutines.flow.*

class PickCarViewModel(private val carUseCase: CarUseCase) : ViewModel() {
    fun getCarDataByParams(manufacturer: Manufacturer, typeCar: TypeCar) =
        carUseCase.getCarDataByParams(manufacturer, typeCar).asLiveData()
}