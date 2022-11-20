package com.ppm.selat.core.domain.usecase

import com.ppm.selat.core.data.Resource
import com.ppm.selat.core.domain.model.DataTypePay
import com.ppm.selat.core.domain.model.OrderData
import com.ppm.selat.core.domain.repository.IPaymentRepository
import io.grpc.internal.SharedResourceHolder
import kotlinx.coroutines.flow.Flow

class PaymentInteractor(private val paymentRepository: IPaymentRepository) : PaymentUseCase {
    override fun addOrder(orderData: OrderData) = paymentRepository.addOrder(orderData)

    override fun getHistoryPayment() = paymentRepository.getHistoryPayment()

    override fun getPaymentData(): Flow<Resource<OrderData>> {
        TODO("Not yet implemented")
    }

    override fun getListPaymentMethod() = paymentRepository.getListPaymentMethod() 

    override fun savePaymentMethod(dataTypePay: DataTypePay) = paymentRepository.savePaymentMethod(dataTypePay)

    override fun deletePaymentMethod(dataTypePay: DataTypePay) = paymentRepository.deletePaymentMethod(dataTypePay)
}