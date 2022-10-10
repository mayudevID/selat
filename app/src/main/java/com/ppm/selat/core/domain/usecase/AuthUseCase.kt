package com.ppm.selat.core.domain.usecase

import com.ppm.selat.core.data.Resource
import kotlinx.coroutines.flow.Flow

interface AuthUseCase {
    fun loginToFirebase(email: String, password: String) : Flow<Resource<Boolean>>
    fun registerToFirebase(name: String, email: String, password: String) : Flow<Resource<Boolean>>
}