package com.ppm.selat.core.data

import com.ppm.selat.core.utils.Manufacturer
import com.ppm.selat.core.utils.TypeCar
import com.ppm.selat.core.data.source.remote.CarFirestoreDataSource
import com.ppm.selat.core.data.source.remote.network.FirebaseResponse
import com.ppm.selat.core.domain.model.Car
import com.ppm.selat.core.domain.repository.ICarRepository
import com.ppm.selat.core.utils.convertTypeCarToString
import com.ppm.selat.core.utils.documentSnapshotToCar
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow


class CarRepository(private val carFirestoreDataSource: CarFirestoreDataSource) : ICarRepository {

    override fun getAllCars(): Flow<Resource<List<Car>>> {
        return flow {
            emit(Resource.Loading())
            val response = carFirestoreDataSource.getAllCars()
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
            val response = carFirestoreDataSource.getCarDataByParams(manufacturer)
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
            val response = carFirestoreDataSource.getCarBySearch(carName)
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

    override fun getAvailableCar(carId: String) = carFirestoreDataSource.getAvailableCar(carId)
}

