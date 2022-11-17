package com.ppm.selat.core.data.source.remote

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.ppm.selat.core.data.source.remote.response.FirebaseResponse
import com.ppm.selat.core.domain.model.OrderData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class PaymentDataSource(firestore: FirebaseFirestore) {
    private val orderDb: CollectionReference = firestore.collection("order")

    suspend fun addOrder(orderData: OrderData, uid: String) : Flow<FirebaseResponse<Boolean>> {
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

    suspend fun getHistoryPayment(uid: String) : Flow<FirebaseResponse<DocumentSnapshot>> {
        return flow {
            try {
                val listTransaction = orderDb.document(uid).get().await()
                emit(FirebaseResponse.Success(listTransaction))
            } catch (e: FirebaseFirestoreException) {
                emit(FirebaseResponse.Error(e.message.toString()))
            }
        }
    }
}