package org.gabrieal.gymtracker.util.navigation

import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.bottomSheet.BottomSheetNavigator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.gabrieal.gymtracker.data.model.Profile
import org.gabrieal.gymtracker.data.model.SelectedExercise
import org.gabrieal.gymtracker.data.model.SelectedExerciseList
import org.gabrieal.gymtracker.features.calculator.view.CalculatorScreen
import org.gabrieal.gymtracker.features.createSplit.view.CreateSplitScreen
import org.gabrieal.gymtracker.features.editPlan.view.EditPlanScreen
import org.gabrieal.gymtracker.features.loginRegister.view.LoginRegisterBottomSheet
import org.gabrieal.gymtracker.features.makeAPlan.view.MakeAPlanScreen
import org.gabrieal.gymtracker.features.startWorkout.view.CurrentlyActiveWorkoutBottomSheet
import org.gabrieal.gymtracker.features.startWorkout.view.StartWorkoutScreen
import org.gabrieal.gymtracker.features.viewAllWorkouts.view.ViewAllWorkoutTabScreen


object AppNavigator {
    private var navigatorInstance: Navigator? = null
    private var bottomSheetNavigatorInstance: BottomSheetNavigator? = null

    private val _navigationEvents = MutableStateFlow<NavigationEvent?>(null)
    val navigationEvents: StateFlow<NavigationEvent?> = _navigationEvents.asStateFlow()


    fun setNavigator(navigator: Navigator) {
        navigatorInstance = navigator
    }

    fun setBottomSheetNavigator(navigator: BottomSheetNavigator) {
        bottomSheetNavigatorInstance = navigator
    }

    fun navigateToCreateSplit() {
        _navigationEvents.value = NavigationEvent.NavigateTo(CreateSplitScreen)
    }

    fun navigateToEditSplit(routines: List<SelectedExerciseList>) {
        CreateSplitScreen.setRoutines(routines)
        _navigationEvents.value = NavigationEvent.NavigateTo(CreateSplitScreen)
    }

    fun navigateToMakeAPlan(
        selectedDays: List<Boolean>,
        routines: List<SelectedExerciseList>,
        isEditMode: Boolean
    ) {
        MakeAPlanScreen.setEditMode(isEditMode)
        MakeAPlanScreen.setSelectedRoutineList(routines)
        MakeAPlanScreen.setSelectedDay(selectedDays)
        _navigationEvents.value = NavigationEvent.NavigateTo(MakeAPlanScreen)
    }

    fun navigateToEditPlan(
        day: String,
        exercises: List<SelectedExercise>?,
        callback: (List<SelectedExercise>) -> Unit,
        isEditMode: Boolean
    ) {
        EditPlanScreen.setSelectedDay(day)
        EditPlanScreen.setExercises(exercises)
        EditPlanScreen.setCallback(callback)
        EditPlanScreen.setEditMode(isEditMode)
        _navigationEvents.value = NavigationEvent.NavigateTo(EditPlanScreen)
    }

    fun navigateToViewAllWorkouts(callback: (String) -> Unit) {
        ViewAllWorkoutTabScreen.setCallback(callback)
        _navigationEvents.value = NavigationEvent.NavigateTo(ViewAllWorkoutTabScreen)
    }

    fun navigateBack() {
        _navigationEvents.value = NavigationEvent.NavigateBack
    }

    fun navigateToRoot() {
        _navigationEvents.value = NavigationEvent.NavigateToRoot
    }

    fun processNavigationEvent(event: NavigationEvent) {
        navigatorInstance?.let { navigator ->
            when (event) {
                is NavigationEvent.NavigateTo -> navigator.push(event.screen)
                is NavigationEvent.NavigateBack -> navigator.pop()
                is NavigationEvent.NavigateToRoot -> navigator.popUntilRoot()
            }
        }
        _navigationEvents.value = null
    }

    fun navigateToStartWorkout(
        selectedExerciseList: SelectedExerciseList,
        callback: (selectedExerciseList: SelectedExerciseList) -> Unit
    ) {
        StartWorkoutScreen.setCallback {
            callback(it)
        }
        StartWorkoutScreen.setSelectedExerciseList(selectedExerciseList)
        _navigationEvents.value = NavigationEvent.NavigateTo(StartWorkoutScreen)
    }

    fun navigateToCalculatorScreen(title: String, profile: Profile?) {
        CalculatorScreen.title = title
        profile?.let { CalculatorScreen.setProfile(it) }
        _navigationEvents.value = NavigationEvent.NavigateTo(CalculatorScreen)
    }

    fun openBottomSheetCurrentlyActiveWorkoutScreen(landingCurrentlyActiveRoutine: SelectedExerciseList) {
        CurrentlyActiveWorkoutBottomSheet.setSelectedExerciseList(landingCurrentlyActiveRoutine)
        bottomSheetNavigatorInstance?.show(CurrentlyActiveWorkoutBottomSheet)
    }

    fun openBottomSheetLoginRegisterScreen(profile: Profile?, callback: (Profile?) -> Unit) {
        LoginRegisterBottomSheet.setProfile(profile ?: Profile())
        LoginRegisterBottomSheet.setCallback(callback)
        bottomSheetNavigatorInstance?.show(LoginRegisterBottomSheet)
    }

    fun dismissBottomSheet() {
        bottomSheetNavigatorInstance?.hide()
    }
}

sealed class NavigationEvent {
    data class NavigateTo(val screen: Screen) : NavigationEvent()
    object NavigateBack : NavigationEvent()
    object NavigateToRoot : NavigationEvent()
}
