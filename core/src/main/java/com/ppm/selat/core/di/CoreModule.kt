package com.ppm.selat.core.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.ppm.selat.core.data.AuthRepository
import com.ppm.selat.core.data.CarRepository
import com.ppm.selat.core.data.source.local.UserLocalDataSource
import com.ppm.selat.core.data.source.remote.AuthDataSource
import com.ppm.selat.core.data.source.remote.CarDataSource
import com.ppm.selat.core.data.source.remote.UserFirestoreDataSource
import com.ppm.selat.core.data.source.remote.StorageDataSource
import com.ppm.selat.core.domain.repository.IAuthRepository
import com.ppm.selat.core.domain.repository.ICarRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single { androidContext().resources }
    single { androidContext().getSharedPreferences("APP_DATA", Context.MODE_PRIVATE) }
}

val networkModule = module {
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }
    single { FirebaseStorage.getInstance() }
}

val repositoryModule = module {
    single { UserLocalDataSource(get()) }
    single { AuthDataSource(get()) }
    single { CarDataSource(get()) }
    single { UserFirestoreDataSource(get()) }
    single { StorageDataSource(get()) }

    single<IAuthRepository> {
        AuthRepository(
            get(),
            get(),
            get(),
            get(),
            get(),
        )
    }

    single<ICarRepository> {
        CarRepository(
            get(),
        )
    }
}