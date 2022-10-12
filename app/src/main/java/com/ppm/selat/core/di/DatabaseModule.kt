package com.ppm.selat.core.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Inject
import javax.inject.Singleton


@Module
class DatabaseModule {

//    private val appContext: Application = app
//
    @Provides
    @Singleton
    fun provideSharedPreference(appContext: Context): SharedPreferences {
        return appContext.getSharedPreferences("APP_DATA", Context.MODE_PRIVATE)
    }
}