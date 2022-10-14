package com.ppm.selat.core.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Inject
import javax.inject.Singleton


@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideResources(appContext: Context): Resources {
        return appContext.resources
    }

    @Provides
    @Singleton
    fun provideSharedPreference(appContext: Context): SharedPreferences {
        return appContext.getSharedPreferences("APP_DATA", Context.MODE_PRIVATE)
    }
}