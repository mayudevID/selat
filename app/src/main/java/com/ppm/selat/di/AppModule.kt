package com.ppm.selat.di

import com.ppm.selat.core.domain.usecase.AuthInteractor
import com.ppm.selat.core.domain.usecase.AuthUseCase
import dagger.Binds
import dagger.Module

@Module
abstract class AppModule {

    @Binds
    abstract fun provideAuthUseCase(authInteractor: AuthInteractor): AuthUseCase
}