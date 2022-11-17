package com.ppm.selat.core.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.ppm.selat.core.data.AuthRepository
import com.ppm.selat.core.data.CarRepository
import com.ppm.selat.core.data.PaymentRepository
import com.ppm.selat.core.data.source.local.UserLocalDataSource
import com.ppm.selat.core.data.source.remote.*
import com.ppm.selat.core.data.source.remote.network.ApiService
import com.ppm.selat.core.domain.repository.IAuthRepository
import com.ppm.selat.core.domain.repository.ICarRepository
import com.ppm.selat.core.domain.repository.IPaymentRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


val databaseModule = module {
    single { androidContext().resources }
    single { androidContext().getSharedPreferences("APP_DATA", Context.MODE_PRIVATE) }
}

val networkModule = module {
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }
    single { FirebaseStorage.getInstance() }
    single {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()
    }
    single {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://tourism-api.dicoding.dev/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
        retrofit.create(ApiService::class.java)
    }
}

val repositoryModule = module {
    single { UserLocalDataSource(get()) }
    single { AuthDataSource(get()) }
    single { PaymentDataSource(get()) }
    single { CarFirestoreDataSource(get()) }
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

    single<IPaymentRepository> {
        PaymentRepository(
            get(),
            get(),
            get(),
        )
    }
}