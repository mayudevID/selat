package com.ppm.selat.core.data.source.remote.network

import com.ppm.selat.core.data.source.remote.response.DistrictResponse
import com.ppm.selat.core.data.source.remote.response.ProvinceResponse
import com.ppm.selat.core.data.source.remote.response.RegencyResponse
import com.ppm.selat.core.data.source.remote.response.VillageResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("provinces.json")
    suspend fun getListProvince(): List<ProvinceResponse>

    @GET("regencies/{num}.json")
    suspend fun getListRegency(@Path("num") number: String) : List<RegencyResponse>

    @GET("districts/{num}.json")
    suspend fun getListDistrict(@Path("num") number: String) : List<DistrictResponse>

    @GET("villages/{num}.json")
    suspend fun getListVillage(@Path("num") number: String) : List<VillageResponse>
}
