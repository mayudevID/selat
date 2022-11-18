package com.ppm.selat.core.domain.model

import com.google.gson.annotations.SerializedName
import com.ppm.selat.core.utils.Region

data class Regency(
    val provinceId: String,
    val name: String,
    val id: String
)