package com.ppm.selat.pick_car

import androidx.lifecycle.ViewModel
import com.ppm.selat.Manufacturer
import com.ppm.selat.TypeCar
import kotlinx.coroutines.flow.MutableStateFlow

class PickCarViewModel : ViewModel() {
    val manufacturer = MutableStateFlow(Manufacturer.ALL)
    val typeCar = MutableStateFlow(TypeCar.ALL)
}