package com.ppm.selat.payment_method

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ppm.selat.core.domain.model.DataTypePay
import com.ppm.selat.core.domain.usecase.PaymentUseCase

class PaymentMethodViewModel(private val paymentUseCase: PaymentUseCase) : ViewModel() {
    fun getListPaymentMethod() = paymentUseCase.getListPaymentMethod().asLiveData()
    fun savePaymentMethod(dataTypePay: DataTypePay) = paymentUseCase.savePaymentMethod(dataTypePay).asLiveData()
    fun deletePaymentMethod(dataTypePay: DataTypePay) = paymentUseCase.deletePaymentMethod(dataTypePay).asLiveData()
}