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

fun documentSnapshotToCar(data: DocumentSnapshot): Car {
     return Car(
        id = data.id,
         location =  data["location"].toString(),
        carImage = documentSnapshotToCarImage(data),
        carManufacturer = data["manufacturer"].toString(),
        carBrand = data["brand"].toString(),
        typeCar = data["type"].toString(),
        price = Integer.parseInt(data["price"].toString()),
        rating = data["rating"].toString().toDouble(),
        yearProduction = Integer.parseInt(data["year_prod"].toString()),
        available = Integer.parseInt(data["price"].toString()),
        spec = documentSnapshotToSpec(data),
         latLng = convertGeoPointToListDouble(data.getGeoPoint("latlng")!!),
     )
}

fun documentSnapshotToCarImage(data: DocumentSnapshot) : CarImage {
    val dataPhoto  = (data.data?.get("photo") as Map<*, *>)
    return CarImage(
        primaryPhoto = dataPhoto["primary_photo"].toString(),
        sidePhoto = dataPhoto["side_photo"].toString()
    )
}

fun documentSnapshotToSpec(data: DocumentSnapshot) : Spec {
    val dataSpec = (data.data?.get("spec") as Map<*, *>)
    return Spec(
        a = dataSpec["a"].toString(),
        b = dataSpec["b"].toString(),
        c = dataSpec["c"].toString(),
    )
}

fun convertGeoPointToListDouble(geoPoint: GeoPoint) : List<Double> {
    return listOf(geoPoint.latitude, geoPoint.longitude)
}