package com.ppm.selat.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.*

@Serializable
@Parcelize
data class UserData(
    val id: String?,
    val name: String?,
    val placeDateOfBirth: String?,
    val email: String?,
    val job: String?,
    val address: String?,
    val photoUrl: String?,
    val phone: String?,

) : Parcelable
