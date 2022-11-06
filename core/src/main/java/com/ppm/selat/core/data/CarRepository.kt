package com.ppm.selat.core.data

import com.ppm.selat.core.utils.Manufacturer
import com.ppm.selat.core.utils.TypeCar
import com.ppm.selat.core.data.source.remote.CarDataSource
import com.ppm.selat.core.data.source.remote.response.FirebaseResponse
import com.ppm.selat.core.domain.model.Car
import com.ppm.selat.core.domain.model.documentSnapshotToCar
import com.ppm.selat.core.domain.repository.ICarRepository
import com.ppm.selat.core.utils.convertTypeCarToString
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

    override fun getCarDataByParams(
        manufacturer: Manufacturer,
        typeCar: TypeCar
    ): Flow<Resource<List<Car>>> {
        return flow {
            emit(Resource.Loading())
            val response = carDataSource.getCarDataByParams(manufacturer)
            when (val result = response.first()) {
                is FirebaseResponse.Success -> {
                    val querySnapshot = result.data
                    var listCar = querySnapshot.documents.map {
                            data -> documentSnapshotToCar(data)
                    }
                    if (typeCar != TypeCar.ALL){
                        listCar = listCar.filter {
                                data -> data.typeCar == convertTypeCarToString(typeCar)
                        }
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

    override fun getCarBySearch(carName: String) : Flow<Resource<List<Car>>>{
        return flow {
            emit(Resource.Loading())
            val response = carDataSource.getCarBySearch(carName)
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

    override fun getAvailableCar(carId: String): Flow<Int> {
        return carDataSource.getAvailableCar(carId)
    }
}

