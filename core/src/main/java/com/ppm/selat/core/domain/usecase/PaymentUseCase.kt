package com.ppm.selat.core.domain.usecase

import com.ppm.selat.core.data.Resource
import com.ppm.selat.core.domain.model.DataTypePay
import com.ppm.selat.core.domain.model.OrderData
import kotlinx.coroutines.flow.Flow

interface PaymentUseCase {
    fun addOrder(orderData: OrderData) : Flow<Resource<OrderData>>
    fun getHistoryPayment() : Flow<Resource<List<OrderData>>>
    fun getPaymentData() : Flow<Resource<OrderData>>
    fun getListPaymentMethod() : Flow<Resource<List<DataTypePay>>>
    fun savePaymentMethod(dataTypePay: DataTypePay) : Flow<Resource<Boolean>>
    fun deletePaymentMethod(dataTypePay: DataTypePay) : Flow<Resource<Boolean>>
}