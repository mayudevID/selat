package com.ppm.selat.di

import com.ppm.selat.auth.LoginViewModel
import com.ppm.selat.auth.RegisterViewModel
import com.ppm.selat.core.domain.usecase.AuthInteractor
import com.ppm.selat.core.domain.usecase.AuthUseCase
import com.ppm.selat.home.HomeViewModel
import com.ppm.selat.profile.ProfileViewModel
import com.ppm.selat.splash.SplashViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val useCaseModule = module {
    factory<AuthUseCase> { AuthInteractor(get()) }
}

val viewModelModule = module {
    viewModel { SplashViewModel(get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { RegisterViewModel(get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { ProfileViewModel(get()) }
}