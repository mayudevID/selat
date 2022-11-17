package com.ppm.selat.core.data.source.remote

import com.google.firebase.firestore.*
import com.ppm.selat.core.utils.Manufacturer
import com.ppm.selat.core.data.source.remote.network.FirebaseResponse
import com.ppm.selat.core.data.source.snapshotFlow
import com.ppm.selat.core.utils.convertManufacturerToString
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await

class CarFirestoreDataSource(firestore: FirebaseFirestore) {
    private var carDb: CollectionReference = firestore.collection("cars")

    suspend fun getAllCars(): Flow<FirebaseResponse<QuerySnapshot>> {
        return flow {
            try {
                val response = carDb.get().await()
                emit(FirebaseResponse.Success(response))
            } catch (e: FirebaseFirestoreException) {
                emit(FirebaseResponse.Error(e.message.toString()))
            }
        }
    }

    suspend fun getCarDataByParams(
        manufacturer: Manufacturer,
    ): Flow<FirebaseResponse<QuerySnapshot>> {
        return flow {
            try {
                val query: Query = if (manufacturer != Manufacturer.ALL) {
                    carDb.whereEqualTo("manufacturer", convertManufacturerToString(manufacturer))
                } else {
                    carDb.whereNotEqualTo("manufacturer", convertManufacturerToString(manufacturer))
                }
                val result = query.get().await()
                emit(FirebaseResponse.Success(result))
            } catch (e: FirebaseFirestoreException) {
                emit(FirebaseResponse.Error(e.message.toString()))
            }
        }
    }

    suspend fun getCarBySearch(carName: String): Flow<FirebaseResponse<QuerySnapshot>> {
        return flow {
            try {
                val response =
                    carDb.orderBy("brand").startAt(carName).endAt(carName + "\uf8ff").get().await()
                emit(FirebaseResponse.Success(response))
            } catch (e: FirebaseFirestoreException) {
                emit(FirebaseResponse.Error(e.message.toString()))
            }
        }
    }

    fun getAvailableCar(carId: String): Flow<Int> {
        return carDb.document(carId).snapshotFlow().map {
            it["available"].toString().toInt()
        }
    }
}