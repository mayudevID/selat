package com.ppm.selat.core.domain.usecase;

import com.ppm.selat.core.data.Resource;
import com.ppm.selat.core.domain.model.District;
import com.ppm.selat.core.domain.model.Province;
import com.ppm.selat.core.domain.model.Regency;
import com.ppm.selat.core.domain.model.Village;
import kotlinx.coroutines.flow.Flow

interface RegionUseCase {
    fun getListProvince() : Flow<Resource<List<Province>>>
    fun getListRegency(num: String) : Flow<Resource<List<Regency>>>
    fun getListDistrict(num: String) : Flow<Resource<List<District>>>
    fun getListVillage(num: String) : Flow<Resource<List<Village>>>
}
