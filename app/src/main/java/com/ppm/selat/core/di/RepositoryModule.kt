package com.ppm.selat.core.di

import android.app.Application
import com.ppm.selat.core.data.AuthRepository
import com.ppm.selat.core.domain.repository.IAuthRepository
import dagger.Binds
import dagger.Module

@Module(includes = [NetworkModule::class, DatabaseModule::class])
abstract class RepositoryModule {

    //@Binds
    //abstract fun provideApplication(application: Application): Application

    @Binds
    abstract fun provideAuthRepository(authRepository: AuthRepository): IAuthRepository
}