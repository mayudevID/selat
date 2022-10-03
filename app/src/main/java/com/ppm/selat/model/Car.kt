package com.ppm.selat.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Car(
    val id: Int?,
    val carImage: String?,
    val carName: String?,
    val typeCar: String?,
    val price: Int?,
    val rating: Double?,
    val yearProduction: Int?
) : Parcelable
