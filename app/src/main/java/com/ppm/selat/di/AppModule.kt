package com.ppm.selat.di

import com.ppm.selat.auth.login.LoginViewModel
import com.ppm.selat.auth.register.RegisterViewModel
import com.ppm.selat.auth.reset_password.ResetPasswordViewModel
import com.ppm.selat.change_password.ChangePasswordViewModel
import com.ppm.selat.core.domain.usecase.*
import com.ppm.selat.detail_car.DetailCarViewModel
import com.ppm.selat.detail_profile.DetailProfileViewModel
import com.ppm.selat.edit_profile.EditProfileViewModel
import com.ppm.selat.home.HomeViewModel
import com.ppm.selat.login_history.LoginHistoryViewModel
import com.ppm.selat.payment.PaymentViewModel
import com.ppm.selat.payment_method.PaymentMethodViewModel
import com.ppm.selat.pick_car.PickCarViewModel
import com.ppm.selat.profile.ProfileViewModel
import com.ppm.selat.proof_booking.ProofBookingViewModel
import com.ppm.selat.search_car.SearchCarViewModel
import com.ppm.selat.set_pin.SetPinViewModel
import com.ppm.selat.splash.SplashViewModel
import com.ppm.selat.transaction.TransactionViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val useCaseModule = module {
    factory<AuthUseCase> { AuthInteractor(get()) }
    factory<CarUseCase> { CarInteractor(get()) }
    factory<PaymentUseCase> { PaymentInteractor(get()) }
    factory<RegionUseCase> { RegionInteractor(get()) }
}

val viewModelModule = module {
    viewModel { SplashViewModel(get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { RegisterViewModel(get()) }
    viewModel { ResetPasswordViewModel(get()) }
    viewModel { HomeViewModel(get(), get()) }
    viewModel { ProfileViewModel(get()) }
    viewModel { SetPinViewModel(get()) }
    viewModel { EditProfileViewModel(get(), get()) }
    viewModel { DetailCarViewModel(get()) }
    viewModel { PaymentViewModel(get(), get()) }
    viewModel { DetailProfileViewModel(get()) }
    viewModel { LoginHistoryViewModel(get()) }
    viewModel { PickCarViewModel(get()) }
    viewModel { SearchCarViewModel(get()) }
    viewModel { ProofBookingViewModel(get()) }
    viewModel { TransactionViewModel(get(), get()) }
    viewModel { ChangePasswordViewModel(get()) }
    viewModel { PaymentMethodViewModel(get()) }
}