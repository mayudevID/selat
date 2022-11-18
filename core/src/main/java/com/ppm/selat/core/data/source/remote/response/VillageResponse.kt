package com.ppm.selat.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class VillageResponse(
    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("district_id")
    val districtId: String
)
