package com.ppm.selat.search_car

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ppm.selat.core.domain.usecase.CarUseCase
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce

class SearchCarViewModel(private val carUseCase: CarUseCase) : ViewModel() {
    fun getDataBySearch(valTenp: String) = carUseCase.getCarBySearch(valTenp).asLiveData()
}