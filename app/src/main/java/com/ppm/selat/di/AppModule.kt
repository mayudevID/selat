package com.ppm.selat.di

import com.ppm.selat.auth.login.LoginViewModel
import com.ppm.selat.auth.register.RegisterViewModel
import com.ppm.selat.auth.reset_password.ResetPasswordViewModel
import com.ppm.selat.core.domain.usecase.AuthInteractor
import com.ppm.selat.core.domain.usecase.AuthUseCase
import com.ppm.selat.core.domain.usecase.CarInteractor
import com.ppm.selat.core.domain.usecase.CarUseCase
import com.ppm.selat.edit_profile.EditProfileViewModel
import com.ppm.selat.home.HomeViewModel
import com.ppm.selat.profile.ProfileViewModel
import com.ppm.selat.splash.SplashViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val useCaseModule = module {
    factory<AuthUseCase> { AuthInteractor(get()) }
    factory<CarUseCase> { CarInteractor(get()) }
}

val viewModelModule = module {
    viewModel { SplashViewModel(get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { RegisterViewModel(get()) }
    viewModel { ResetPasswordViewModel(get()) }
    viewModel { HomeViewModel(get(), get()) }
    viewModel { ProfileViewModel(get()) }
    viewModel { EditProfileViewModel(get()) }
}