package com.ppm.selat.core.data.source.remote

import android.util.Log
import com.ppm.selat.core.data.source.remote.network.ApiResponse
import com.ppm.selat.core.data.source.remote.network.ApiService
import com.ppm.selat.core.data.source.remote.response.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class RegionDataSource(private val apiService: ApiService) {
    suspend fun getDataProvince(): Flow<ApiResponse<List<ProvinceResponse>>> {
        return flow {
            try {
                val response = apiService.getListProvince()

                if (response.isNotEmpty()){
                    emit(ApiResponse.Success(response))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e : Exception){
                emit(ApiResponse.Error(e.toString()))
                Log.e("RemoteDataSource", e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getDataDistrict(num: String): Flow<ApiResponse<List<DistrictResponse>>> {
        return flow {
            try {
                val response = apiService.getListDistrict(num)

                if (response.isNotEmpty()){
                    emit(ApiResponse.Success(response))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e : Exception){
                emit(ApiResponse.Error(e.toString()))
                Log.e("RemoteDataSource", e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getDataRegency(num: String): Flow<ApiResponse<List<RegencyResponse>>> {
        return flow {
            try {
                val response = apiService.getListRegency(num)

                if (response.isNotEmpty()){
                    emit(ApiResponse.Success(response))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e : Exception){
                emit(ApiResponse.Error(e.toString()))
                Log.e("RemoteDataSource", e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getDataVillage(num: String): Flow<ApiResponse<List<VillageResponse>>> {
        return flow {
            try {
                val response = apiService.getListVillage(num)

                if (response.isNotEmpty()){
                    emit(ApiResponse.Success(response))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e : Exception){
                emit(ApiResponse.Error(e.toString()))
                Log.e("RemoteDataSource", e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }
}