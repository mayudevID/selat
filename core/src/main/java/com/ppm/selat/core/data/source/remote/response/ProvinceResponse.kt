package com.ppm.selat.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class ProvinceResponse(
    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("id")
    val id: String
)