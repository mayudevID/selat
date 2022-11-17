package com.ppm.selat.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ppm.selat.core.domain.usecase.PaymentUseCase
import com.ppm.selat.payment.PaymentViewModel

class TransactionViewModel(private val paymentUseCase: PaymentUseCase) : ViewModel() {
    fun getHistoryOrder() = paymentUseCase.getHistoryPayment().asLiveData()
}