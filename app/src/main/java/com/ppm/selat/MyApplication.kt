package com.ppm.selat

import android.app.Application
import com.ppm.selat.core.di.databaseModule
import com.ppm.selat.core.di.networkModule
import com.ppm.selat.core.di.repositoryModule
import com.ppm.selat.di.useCaseModule
import com.ppm.selat.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

open class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@MyApplication)
            modules(
                listOf(
                    databaseModule,
                    networkModule,
                    repositoryModule,
                    useCaseModule,
                    viewModelModule
                )
            )
        }
    }
}