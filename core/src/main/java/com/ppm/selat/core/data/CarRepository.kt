package com.ppm.selat.core.data

import androidx.core.util.rangeTo
import com.google.firebase.firestore.DocumentSnapshot
import com.ppm.selat.core.data.source.remote.CarDataSource
import com.ppm.selat.core.data.source.remote.response.FirebaseResponse
import com.ppm.selat.core.domain.model.Car
import com.ppm.selat.core.domain.model.CarImage
import com.ppm.selat.core.domain.model.Spec
import com.ppm.selat.core.domain.model.documentSnapshotToCar
import com.ppm.selat.core.domain.repository.ICarRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.Json.Default.decodeFromString


class CarRepository(private val carDataSource: CarDataSource) : ICarRepository {

    override fun getAllCars(): Flow<Resource<List<Car>>> {
        return flow {
            emit(Resource.Loading())
            val response = carDataSource.getAllCars()
            when (val result = response.first()) {
                is FirebaseResponse.Success -> {
                    val querySnapshot = result.data
                    val listCar = querySnapshot.documents.map {
                        data -> documentSnapshotToCar(data)
                    }
                    emit(Resource.Success(listCar))
                }
                is FirebaseResponse.Error -> {
                    emit(Resource.Error(result.errorMessage))
                }
                is FirebaseResponse.Empty -> {

                }
            }
        }
    }

}