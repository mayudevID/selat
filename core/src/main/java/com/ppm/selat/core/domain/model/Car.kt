package com.ppm.selat.core.domain.model

import android.os.Parcelable
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.GeoPoint
import com.google.type.LatLng
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import kotlinx.serialization.Serializable

@Parcelize
data class Car(
    val id: String,
    val latLng: List<Double>,
    val carImage: CarImage,
    val carManufacturer: String,
    val carBrand: String,
    val typeCar: String,
    val price: Int,
    val spec: Spec,
    val rating: Double,
    val yearProduction: Int,
    val available: Int,
    val location: String,
) : Parcelable

@Parcelize
data class CarImage(
    val primaryPhoto : String,
    val sidePhoto : String,
) : Parcelable

@Parcelize
data class Spec(
    val a : String,
    val b : String,
    val c : String,
) : Parcelable