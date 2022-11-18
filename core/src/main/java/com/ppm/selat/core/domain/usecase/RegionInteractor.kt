package com.ppm.selat.core.domain.usecase

import com.ppm.selat.core.data.RegionRepository
import com.ppm.selat.core.data.Resource
import com.ppm.selat.core.domain.model.District
import com.ppm.selat.core.domain.model.Province
import com.ppm.selat.core.domain.model.Regency
import com.ppm.selat.core.domain.model.Village
import com.ppm.selat.core.domain.repository.IRegionRepository
import kotlinx.coroutines.flow.Flow

class RegionInteractor(private val regionRepository: IRegionRepository) : RegionUseCase {
    override fun getListProvince() = regionRepository.getListProvince()
    override fun getListRegency(num: String) = regionRepository.getListRegency(num)
    override fun getListDistrict(num: String) = regionRepository.getListDistrict(num)
    override fun getListVillage(num: String) = regionRepository.getListVillage(num)
}