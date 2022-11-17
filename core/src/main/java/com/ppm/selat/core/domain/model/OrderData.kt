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

