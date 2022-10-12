package com.ppm.selat.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.*

@Serializable
@Parcelize
data class UserData(
    val id: String?,
    val name: String?,
    val email: String?,
    val photoUrl: String?
) : Parcelable
