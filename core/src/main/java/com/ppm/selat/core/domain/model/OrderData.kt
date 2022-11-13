package com.ppm.selat.core.domain.model

data class OrderData(
    val id: String,
    val brand: String,
    val manufacturer: String,
    val rentDays: Int,
    val price: Int,
    val paymentTypeName: String,
    val dateOrder: String,
)
