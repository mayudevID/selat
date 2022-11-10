package com.ppm.selat.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.*

@Serializable
@Parcelize
data class UserData(
    val id: String? = null,
    val name: String? = null,
    val placeDateOfBirth: String? = null,
    val email: String? = null,
    val job: String? = null,
    val address: String? = null,
    val photoUrl: String? = null,
    val phone: String? = null,
) : Parcelable
