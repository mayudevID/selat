package com.ppm.selat.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ppm.selat.core.domain.usecase.CarUseCase
import com.ppm.selat.core.domain.usecase.PaymentUseCase

class TransactionViewModel(private val carUseCase: CarUseCase,private val paymentUseCase: PaymentUseCase) : ViewModel() {
    fun getHistoryOrder() = paymentUseCase.getHistoryPayment().asLiveData()
    fun getSingleCarData(idCar: String) = carUseCase.getSingleDataCar(idCar).asLiveData()
}