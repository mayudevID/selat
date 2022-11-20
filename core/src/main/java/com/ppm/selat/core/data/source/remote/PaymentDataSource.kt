package com.ppm.selat.core.data.source.remote

import com.google.firebase.firestore.*
import com.ppm.selat.core.data.source.remote.network.FirebaseResponse
import com.ppm.selat.core.domain.model.DataTypePay
import com.ppm.selat.core.domain.model.OrderData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class PaymentDataSource(firestore: FirebaseFirestore) {
    private val orderDb: CollectionReference = firestore.collection("order")
    private val paymentMethodDB: CollectionReference = firestore.collection("payment_method")

    suspend fun addOrder(orderData: OrderData, uid: String): Flow<FirebaseResponse<Boolean>> {
        return flow {
            try {
                val dataMap = mapOf(
                    "id" to orderData.id,
                    "carId" to orderData.idCar,
                    "brand" to orderData.brand,
                    "manufacturer" to orderData.manufacturer,
                    "dateOrder" to orderData.dateOrder,
                    "paymentTypeName" to orderData.paymentTypeName,
                    "price" to orderData.price,
                    "rentDays" to orderData.rentDays,
                    "paymentNumber" to orderData.paymentNumber,
                )
                orderDb.document(uid).update(mapOf(orderData.id to dataMap)).await()
                emit(FirebaseResponse.Success(true))
            } catch (e: FirebaseFirestoreException) {
                emit(FirebaseResponse.Error(e.message.toString()))
            }
        }
    }

    suspend fun getHistoryPayment(uid: String): Flow<FirebaseResponse<DocumentSnapshot>> {
        return flow {
            try {
                val listTransaction = orderDb.document(uid).get().await()
                emit(FirebaseResponse.Success(listTransaction))
            } catch (e: FirebaseFirestoreException) {
                emit(FirebaseResponse.Error(e.message.toString()))
            }
        }
    }

    suspend fun getListPaymentMethod(uid: String): Flow<FirebaseResponse<QuerySnapshot>> {
        return flow {
            try {
                val listPaymentMethod = paymentMethodDB.document(uid).collection("PAYMENT_METHOD").get().await()
                emit(FirebaseResponse.Success(listPaymentMethod))
            } catch (e: FirebaseFirestoreException) {
                emit(FirebaseResponse.Error(e.message.toString()))
            }
        }
    }

    suspend fun savePaymentMethod(
        uid: String,
        dataTypePay: DataTypePay
    ): Flow<FirebaseResponse<Boolean>> {
        return flow {
            try {
                val mapData = mapOf(
                    "id" to dataTypePay.id,
                    "number" to dataTypePay.number,
                    "type" to dataTypePay.type,
                    "value" to dataTypePay.value,
                    "name" to dataTypePay.name
                )
                paymentMethodDB.document(uid).collection("PAYMENT_METHOD").document()
                        .set(mapData).await()
                emit(FirebaseResponse.Success(true))
            } catch (e: FirebaseFirestoreException) {
                emit(FirebaseResponse.Error(e.message.toString()))
            }
        }
    }

    suspend fun deletePaymentMethod(
        uid: String,
        dataTypePay: DataTypePay
    ): Flow<FirebaseResponse<Boolean>> {
        return flow {
            try {
                val colPayMethod = paymentMethodDB.document(uid).collection("PAYMENT_METHOD")
                colPayMethod.document(dataTypePay.id).delete().await()
                emit(FirebaseResponse.Success(true))
            } catch (e: FirebaseFirestoreException) {
                emit(FirebaseResponse.Error(e.message.toString()))
            }
        }
    }
}