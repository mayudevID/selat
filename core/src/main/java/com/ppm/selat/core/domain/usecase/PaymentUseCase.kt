package com.ppm.selat.core.domain.usecase

import com.ppm.selat.core.data.Resource
import com.ppm.selat.core.domain.model.OrderData
import kotlinx.coroutines.flow.Flow

interface PaymentUseCase {
    fun addOrder(orderData: OrderData) : Flow<Resource<Boolean>>
    fun getHistoryPayment() : Flow<Resource<List<OrderData>>>
    fun getPaymentData() : Flow<Resource<OrderData>>
}