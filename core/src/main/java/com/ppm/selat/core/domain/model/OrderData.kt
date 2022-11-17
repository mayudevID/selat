package com.ppm.selat.core.domain.model

import android.os.Parcelable
import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class OrderData(
    val id: String,
    val idCar: String,
    val paymentNumber: String,
    val brand: String,
    val manufacturer: String,
    val rentDays: Int,
    val price: Int,
    val paymentTypeName: String,
    val dateOrder: String,
) : Parcelable

fun convertToListOrderData(doc: DocumentSnapshot) : List<OrderData> {
    Log.d("TransactionActivity", doc.toString())
    val mapOfOrder = doc.data as Map<*, *>
    val listOrderData = ArrayList<OrderData>()
    mapOfOrder.map {
        val orderData = it.value as Map<*, *>
        listOrderData.add(
            OrderData(
                id = orderData["id"] as String,
                idCar = orderData["carId"] as String,
                brand = orderData["brand"] as String,
                manufacturer = orderData["manufacturer"] as String,
                dateOrder = orderData["dateOrder"] as String,
                paymentTypeName =  orderData["paymentTypeName"] as String,
                price = (orderData["price"] as Long).toInt(),
                rentDays = (orderData["rentDays"] as Long).toInt(),
                paymentNumber = orderData["paymentNumber"] as String
            )
        )
    }
    return listOrderData
}