package com.ppm.selat.core.domain.repository

import com.ppm.selat.core.data.Resource
import com.ppm.selat.core.domain.model.Car
import kotlinx.coroutines.flow.Flow

interface IAuthRepository {
    fun loginToFirebase(email: String, password: String) : Flow<Resource<Boolean>>
    fun registerToFirebase(name: String, email: String, password: String) : Flow<Resource<Boolean>>
}