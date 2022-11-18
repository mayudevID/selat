package com.ppm.selat.core.data

import com.ppm.selat.core.data.source.remote.RegionDataSource
import com.ppm.selat.core.data.source.remote.network.ApiResponse
import com.ppm.selat.core.domain.model.District
import com.ppm.selat.core.domain.model.Province
import com.ppm.selat.core.domain.model.Regency
import com.ppm.selat.core.domain.model.Village
import com.ppm.selat.core.domain.repository.IRegionRepository
import com.ppm.selat.core.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class RegionRepository(private val regionDataSource: RegionDataSource) : IRegionRepository {
    override fun getListProvince(): Flow<Resource<List<Province>>> {
        return flow {
            emit(Resource.Loading())
            val response = regionDataSource.getDataProvince()
            when (val result = response.first()) {
                is ApiResponse.Success -> {
                    emit(Resource.Success(DataMapper.mapProvinceResponseToDomain(result.data)))
                }
                is ApiResponse.Empty -> {
                    emit(Resource.Success(listOf()))
                }
                is ApiResponse.Error -> {
                    emit(Resource.Error(result.errorMessage))
                }
            }
        }
    }

    override fun getListRegency(num: String): Flow<Resource<List<Regency>>> {
        return flow {
            emit(Resource.Loading())
            val response = regionDataSource.getDataRegency(num)
            when (val result = response.first()) {
                is ApiResponse.Success -> {
                    emit(Resource.Success(DataMapper.mapRegencyResponseToDomain(result.data)))
                }
                is ApiResponse.Empty -> {
                    emit(Resource.Success(listOf()))
                }
                is ApiResponse.Error -> {
                    emit(Resource.Error(result.errorMessage))
                }
            }
        }
    }

    override fun getListDistrict(num: String): Flow<Resource<List<District>>> {
        return flow {
            emit(Resource.Loading())
            val response = regionDataSource.getDataDistrict(num)
            when (val result = response.first()) {
                is ApiResponse.Success -> {
                    emit(Resource.Success(DataMapper.mapDistrictResponseToDomain(result.data)))
                }
                is ApiResponse.Empty -> {
                    emit(Resource.Success(listOf()))
                }
                is ApiResponse.Error -> {
                    emit(Resource.Error(result.errorMessage))
                }
            }
        }
    }

    override fun getListVillage(num: String): Flow<Resource<List<Village>>> {
        return flow {
            emit(Resource.Loading())
            val response = regionDataSource.getDataVillage(num)
            when (val result = response.first()) {
                is ApiResponse.Success -> {
                    emit(Resource.Success(DataMapper.mapVillageResponseToDomain(result.data)))
                }
                is ApiResponse.Empty -> {
                    emit(Resource.Success(listOf()))
                }
                is ApiResponse.Error -> {
                    emit(Resource.Error(result.errorMessage))
                }
            }
        }
    }

}