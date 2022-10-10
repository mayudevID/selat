package com.ppm.selat.core.data.source.remote

import android.util.Log
import com.ppm.selat.core.domain.model.Car
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

class RemoteDataSource {
//    suspend fun getCarByType() : Flow<FirebaseResponse<List<Car>>> {
//        return flow {
//
//        }
//    }
//
//    suspend fun getAllTourism(): Flow<ApiResponse<List<TourismResponse>>> {
//        //get data from remote api
//        return flow {
//            try {
//                val response = apiService.getList()
//                val dataArray = response.places
//                if (dataArray.isNotEmpty()){
//                    emit(ApiResponse.Success(response.places))
//                } else {
//                    emit(ApiResponse.Empty)
//                }
//            } catch (e : Exception){
//                emit(ApiResponse.Error(e.toString()))
//                Log.e("RemoteDataSource", e.toString())
//            }
//        }.flowOn(Dispatchers.IO)
}

