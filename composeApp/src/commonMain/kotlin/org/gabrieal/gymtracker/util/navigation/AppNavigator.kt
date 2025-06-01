package org.gabrieal.gymtracker.util.navigation

import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.gabrieal.gymtracker.model.SelectedExercise
import org.gabrieal.gymtracker.model.SelectedExerciseList
import org.gabrieal.gymtracker.views.screens.CreateSplitScreen
import org.gabrieal.gymtracker.views.screens.EditPlanScreen
import org.gabrieal.gymtracker.views.screens.MakeAPlanScreen
import org.gabrieal.gymtracker.views.screens.StartWorkoutScreen
import org.gabrieal.gymtracker.views.screens.landingTabs.HomeTab
import org.gabrieal.gymtracker.views.screens.landingTabs.ViewAllWorkoutTabScreen


object AppNavigator {
    private var navigatorInstance: Navigator? = null

    private val _navigationEvents = MutableStateFlow<NavigationEvent?>(null)
    val navigationEvents: StateFlow<NavigationEvent?> = _navigationEvents.asStateFlow()


    fun setNavigator(navigator: Navigator) {
        navigatorInstance = navigator
    }

    fun navigateToHome() {
        _navigationEvents.value = NavigationEvent.NavigateTo(HomeTab)
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
}

sealed class NavigationEvent {
    data class NavigateTo(val screen: Screen) : NavigationEvent()
    object NavigateBack : NavigationEvent()
    object NavigateToRoot : NavigationEvent()
}
