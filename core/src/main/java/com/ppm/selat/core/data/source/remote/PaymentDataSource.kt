package com.ppm.selat.core.data.source.remote

import com.google.firebase.firestore.CollectionReference
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
                    "carId" to orderData.id,
                    "brand" to orderData.brand,
                    "manufacturer" to orderData.manufacturer,
                    "dateOrder" to orderData.dateOrder,
                    "paymentTypeName" to orderData.paymentTypeName,
                    "price" to orderData.price,
                    "rentDays" to orderData.rentDays,
                )
                orderDb.document(uid).set(orderData.id to dataMap).await()
                emit(FirebaseResponse.Success(true))
            } catch (e: FirebaseFirestoreException) {
                emit(FirebaseResponse.Error(e.message.toString()))
            }
        }
    }
}