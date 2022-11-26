package com.ppm.selat.edit_profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ppm.selat.core.data.Resource
import com.ppm.selat.core.domain.model.*
import com.ppm.selat.core.domain.usecase.AuthUseCase
import com.ppm.selat.core.domain.usecase.RegionUseCase
import com.ppm.selat.core.utils.TypeDataEdit
import com.ppm.selat.core.utils.getCapsSentences
import kotlinx.coroutines.flow.*

class EditProfileViewModel(
    private val authUseCase: AuthUseCase,
    private val regionUseCase: RegionUseCase
) : ViewModel() {
    lateinit var editMode: TypeDataEdit
    var textValue = MutableStateFlow("")
    var dateBirth = MutableStateFlow("")
    var neededPassword = MutableStateFlow("")

    fun updateProfile() : LiveData<Resource<Boolean>> {

        if (editMode == TypeDataEdit.PDOB) {
            textValue.value = "${textValue.value}, ${dateBirth.value}"
        }

        return authUseCase.updateProfile(
            editMode,
            if (textValue.value.isEmpty() || textValue.value == "") "Tidak ada data" else textValue.value,
            neededPassword.value
        ).asLiveData()
    }

    fun updateAddress() = authUseCase.updateProfile(
        editMode,
        if (textValue.value.isEmpty() || textValue.value == "") "Tidak ada data" else setAddress(),
        neededPassword.value
    ).asLiveData()

    private fun setAddress(): String {
        return "$textValue.value, ${getCapsSentences(villageTarget.value.name.lowercase())}, ${
            getCapsSentences(
                districtTarget.value.name.lowercase()
            )
        }, ${getCapsSentences(regencyTarget.value.name.lowercase())}, ${
            getCapsSentences(
                provinceTarget.value.name.lowercase()
            )
        }"
    }

    var provinceTarget = MutableStateFlow(Province("", ""))
    var districtTarget = MutableStateFlow(District("", "", ""))
    var regencyTarget = MutableStateFlow(Regency("", "", ""))
    var villageTarget = MutableStateFlow(Village("", "", ""))

    fun getPIN() = authUseCase.getPIN().asLiveData()

    fun getProvince() = regionUseCase.getListProvince().asLiveData()
    fun getRegency(num: String) = regionUseCase.getListRegency(num).asLiveData()
    fun getDistrict(num: String) = regionUseCase.getListDistrict(num).asLiveData()
    fun getVillage(num: String) = regionUseCase.getListVillage(num).asLiveData()
}
