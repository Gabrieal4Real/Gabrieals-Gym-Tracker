package org.gabrieal.gymtracker.data.di

import org.gabrieal.gymtracker.data.network.AuthService
import org.gabrieal.gymtracker.data.network.httpClient
import org.gabrieal.gymtracker.features.calculator.viewmodel.CalculatorViewModel
import org.gabrieal.gymtracker.features.createSplit.viewmodel.CreateSplitViewModel
import org.gabrieal.gymtracker.features.editPlan.viewmodel.EditPlanViewModel
import org.gabrieal.gymtracker.features.home.viewmodel.HomeViewModel
import org.gabrieal.gymtracker.features.landing.viewmodel.LandingViewModel
import org.gabrieal.gymtracker.features.loginRegister.repository.LoginRegisterRepo
import org.gabrieal.gymtracker.features.loginRegister.repository.LoginRegisterRepoImpl
import org.gabrieal.gymtracker.features.loginRegister.viewmodel.LoginRegisterViewModel
import org.gabrieal.gymtracker.features.makeAPlan.viewmodel.MakeAPlanViewModel
import org.gabrieal.gymtracker.features.profile.viewmodel.ProfileViewModel
import org.gabrieal.gymtracker.features.startWorkout.viewmodel.StartWorkoutViewModel
import org.gabrieal.gymtracker.features.viewAllWorkouts.viewmodel.ViewAllWorkoutViewModel
import org.koin.dsl.module

val appModule = module {
    single { httpClient }
    single { AuthService(get()) }

    //repositories
    single<LoginRegisterRepo> { LoginRegisterRepoImpl(get()) }

    //viewmodels
    factory { CalculatorViewModel() }
    factory { CreateSplitViewModel() }
    factory { EditPlanViewModel() }
    factory { HomeViewModel() }
    factory { LandingViewModel() }
    factory { LoginRegisterViewModel(get()) }
    factory { MakeAPlanViewModel() }
    factory { ProfileViewModel() }
    factory { StartWorkoutViewModel() }
    factory { ViewAllWorkoutViewModel() }
}