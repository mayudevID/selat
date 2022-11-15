package com.ppm.selat.payment

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ppm.selat.core.domain.model.Car
import com.ppm.selat.core.domain.model.DataTypePay
import com.ppm.selat.core.domain.model.OrderData
import com.ppm.selat.core.domain.usecase.AuthUseCase
import com.ppm.selat.core.domain.usecase.PaymentUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import java.text.SimpleDateFormat
import java.util.*

class PaymentViewModel(
    private val authUseCase: AuthUseCase,
    private val paymentUseCase: PaymentUseCase
) : ViewModel() {
    var totalDay = MutableStateFlow(1)
    lateinit var carDataBuy: Car
    var dataTypePay = MutableStateFlow(
        DataTypePay(
            id = "NULL",
            number = "NULL",
            type = "NULL",
            value = 0,
            name = "NULL"
        )
    )

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
            id = "${randGeneratedStr()}${(System.currentTimeMillis() / 100000)}",
            idCar = carDataBuy.id,
            paymentNumber = dataTypePay.value.number,
            brand = carDataBuy.carBrand,
            manufacturer = carDataBuy.carManufacturer,
            price = carDataBuy.price,
            rentDays = totalDay.value,
            paymentTypeName = "${dataTypePay.value.type} ++ ${dataTypePay.value.name}",
            dateOrder = getDate(),
        )
    ).asLiveData()

    val isEnough = combine(
        totalDay,
        dataTypePay,
    ) { totalDay, dataTypePay ->
        val isEnough = dataTypePay.value > (totalDay * carDataBuy.price)
        isEnough
    }

    fun randGeneratedStr(): String {
        val alphaNumericStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvxyz0123456789"
        val stringBuilder = StringBuilder(5);
        for (i in 1..5) {
            val ch = (alphaNumericStr.length * Math.random()).toInt()
            stringBuilder.append(alphaNumericStr.toCharArray()[ch])
        }
        return stringBuilder.toString();
    }
}