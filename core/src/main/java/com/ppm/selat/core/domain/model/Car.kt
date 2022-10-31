package com.ppm.selat.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Car(
    val id: String,
    val carImage: String,
    val carManufacturer: String,
    val carBrand: String,
    val typeCar: String,
    val price: Int,
    val rating: Double,
    val yearProduction: Int,
    val available: Int,
) : Parcelable
