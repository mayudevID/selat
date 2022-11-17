package com.ppm.selat.core.data

import com.ppm.selat.core.data.source.remote.AuthDataSource
import com.ppm.selat.core.data.source.remote.CarFirestoreDataSource
import com.ppm.selat.core.data.source.remote.PaymentDataSource
import com.ppm.selat.core.data.source.remote.response.FirebaseResponse
import com.ppm.selat.core.domain.model.OrderData
import com.ppm.selat.core.domain.model.convertToListOrderData
import com.ppm.selat.core.domain.repository.IPaymentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class PaymentRepository(
    private val paymentDataSource: PaymentDataSource,
    private val authDataSource: AuthDataSource,
    private val carFirestoreDataSource: CarFirestoreDataSource,
) :
    IPaymentRepository {
    override fun addOrder(orderData: OrderData): Flow<Resource<OrderData>> {
        return flow {
            emit(Resource.Loading())
            val getDataAvailable = carFirestoreDataSource.getAvailableCar(orderData.idCar)
            when (getDataAvailable.first()) {
                0 -> {
                    emit(Resource.Error("EMPTY"))
                }
                else -> {
                    val orderProcess = paymentDataSource.addOrder(orderData, authDataSource.getUidUser())
                    when (val result = orderProcess.first()) {
                        is FirebaseResponse.Success -> {
                            emit(Resource.Success(orderData))
                        }
                        is FirebaseResponse.Error -> {
                            emit(Resource.Error(result.errorMessage))
                        }
                        is FirebaseResponse.Empty -> {

                        }
                    }
                }
            }
        }
    }

    override fun getHistoryPayment(): Flow<Resource<List<OrderData>>> {
        return flow{
            emit(Resource.Loading())
            val doc = paymentDataSource.getHistoryPayment(authDataSource.getUidUser())
            when (val result = doc.first()) {
                is FirebaseResponse.Success -> {
                    val docSnapshot = result.data
                    emit(Resource.Success(convertToListOrderData(docSnapshot)))
                }
                is FirebaseResponse.Error -> {
                    emit(Resource.Error(result.errorMessage))

                }
                is FirebaseResponse.Empty -> {

                }
            }
        }
    }

    override fun getPaymentData(): Flow<Resource<OrderData>> {
        TODO("Not yet implemented")
    }

}