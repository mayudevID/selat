package com.ppm.selat.core.data

import com.ppm.selat.core.data.source.remote.AuthDataSource
import com.ppm.selat.core.data.source.remote.CarFirestoreDataSource
import com.ppm.selat.core.data.source.remote.PaymentDataSource
import com.ppm.selat.core.data.source.remote.response.FirebaseResponse
import com.ppm.selat.core.domain.model.OrderData
import com.ppm.selat.core.domain.repository.IPaymentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class PaymentRepository(private val paymentDataSource: PaymentDataSource, private val authDataSource: AuthDataSource) :
    IPaymentRepository{
    override fun addOrder(orderData: OrderData): Flow<Resource<Boolean>> {
        return flow {
            emit(Resource.Loading())
            val orderProcess = paymentDataSource.addOrder(orderData, authDataSource.getUidUser())
            when (val result = orderProcess.first()) {
                is FirebaseResponse.Success -> {
                    emit(Resource.Success(true))
                }
                is FirebaseResponse.Error -> {
                    emit(Resource.Error(result.errorMessage))
                }
                is FirebaseResponse.Empty -> {

                }
            }
        }
    }

    override fun getHistoryPayment(): Flow<Resource<List<OrderData>>> {
        TODO("Not yet implemented")
    }

    override fun getPaymentData(): Flow<Resource<OrderData>> {
        TODO("Not yet implemented")
    }

}