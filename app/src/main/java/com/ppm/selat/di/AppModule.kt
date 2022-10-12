package com.ppm.selat.di

import android.app.Application
import android.content.Context
import com.ppm.selat.core.domain.usecase.AuthInteractor
import com.ppm.selat.core.domain.usecase.AuthUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
abstract class AppModule {

    @Binds
    abstract fun provideAuthUseCase(authInteractor: AuthInteractor): AuthUseCase
}