package com.ppm.selat

import android.app.Application
import com.ppm.selat.core.di.CoreComponent
import com.ppm.selat.core.di.DaggerCoreComponent
import com.ppm.selat.di.AppComponent
import com.ppm.selat.di.DaggerAppComponent

open class MyApplication : Application() {

    private val coreComponent: CoreComponent by lazy {
        DaggerCoreComponent.factory().create(applicationContext)
    }

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(coreComponent)
    }
}