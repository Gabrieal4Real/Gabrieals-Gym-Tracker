package org.gabrieal.gymtracker.data.di

import org.gabrieal.gymtracker.data.network.FirebaseService
import org.gabrieal.gymtracker.data.network.httpClient
import org.gabrieal.gymtracker.features.loginRegister.repository.LoginRegisterRepo
import org.gabrieal.gymtracker.features.loginRegister.repository.LoginRegisterRepoImpl
import org.gabrieal.gymtracker.features.loginRegister.viewmodel.LoginRegisterViewModel
import org.koin.dsl.module

val appModule = module {
    single { httpClient }
    single { FirebaseService(get()) }

    //repositories
    single<LoginRegisterRepo> { LoginRegisterRepoImpl(get()) }

    //viewmodels
    factory { LoginRegisterViewModel(get()) }
}