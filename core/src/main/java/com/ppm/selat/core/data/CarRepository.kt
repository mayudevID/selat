package com.ppm.selat.core.data

import androidx.core.util.rangeTo
import com.ppm.selat.core.data.source.remote.CarDataSource
import com.ppm.selat.core.data.source.remote.response.FirebaseResponse
import com.ppm.selat.core.domain.model.Car
import com.ppm.selat.core.domain.repository.ICarRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow


class CarRepository(private val carDataSource: CarDataSource) : ICarRepository {
    override fun getAllCars(): Flow<Resource<List<Car>>> {
        return flow {
            emit(Resource.Loading())
            val response = carDataSource.getAllCars()
            when (val result = response.first()) {
                is FirebaseResponse.Success -> {
                    val querySnapshot = result.data
                    val listCar = querySnapshot.documents.map {
                        data ->
                        Car(
                            id = data.id,
                            carImage = data["ssss"].toString(),
                            carManufacturer = data["manufacturer"].toString(),
                            carBrand = data["brand"].toString(),
                            typeCar = data["type"].toString(),
                            price = Integer.parseInt(data["price"].toString()),
                            rating = data["rating"].toString().toDouble(),
                            yearProduction = Integer.parseInt(data["year_prod"].toString()),
                            available = Integer.parseInt(data["price"].toString())
                        )
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