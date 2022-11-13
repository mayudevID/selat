package com.ppm.selat.payment

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ppm.selat.core.domain.model.Car
import com.ppm.selat.core.domain.model.OrderData
import com.ppm.selat.core.domain.usecase.AuthUseCase
import com.ppm.selat.core.domain.usecase.PaymentUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import java.text.SimpleDateFormat
import java.util.*

class PaymentViewModel(private val authUseCase: AuthUseCase, private val paymentUseCase: PaymentUseCase) : ViewModel() {
    var totalDay = MutableStateFlow(1)
    lateinit var carDataBuy: Car

    fun getPIN() = authUseCase.getPIN().asLiveData()
    fun getDataProfile() = authUseCase.getUserStream()

    @SuppressLint("SimpleDateFormat")
    private fun getDate(): String {
        val pattern = "dd MMM yyyy HH:mm:ss z"
        val simpleDateFormat = SimpleDateFormat(pattern)
        return simpleDateFormat.format(Date())
    }

    fun addOrder() = paymentUseCase.addOrder(
        OrderData(
            id = "7878",
            brand = carDataBuy.carBrand,
            manufacturer = carDataBuy.carManufacturer,
            price = carDataBuy.price,
            rentDays = totalDay.value,
            paymentTypeName = "CARD ++ MasterCard",
            dateOrder = getDate(),
        )
    ).asLiveData()
}