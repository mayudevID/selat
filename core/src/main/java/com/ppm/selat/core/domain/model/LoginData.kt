package com.ppm.selat.core.domain.model

data class LoginData(
    val email: String,
    val password: String,
    val deviceData: String,
    val lastLogin: String,
)
