package org.gabrieal.gymtracker.util.navigation

import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.gabrieal.gymtracker.model.SelectedExercise
import org.gabrieal.gymtracker.views.screens.CreateSplitScreen
import org.gabrieal.gymtracker.views.screens.EditPlanScreen
import org.gabrieal.gymtracker.views.screens.HomeScreen
import org.gabrieal.gymtracker.views.screens.MakeAPlanScreen
import org.gabrieal.gymtracker.views.screens.ViewAllWorkoutScreen


object AppNavigator {
    private var navigatorInstance: Navigator? = null

    private val _navigationEvents = MutableStateFlow<NavigationEvent?>(null)
    val navigationEvents: StateFlow<NavigationEvent?> = _navigationEvents.asStateFlow()


    fun setNavigator(navigator: Navigator) {
        navigatorInstance = navigator
    }

    fun navigateToHome() {
        _navigationEvents.value = NavigationEvent.NavigateTo(HomeScreen)
    }

    fun navigateToCreateSplit() {
        _navigationEvents.value = NavigationEvent.NavigateTo(CreateSplitScreen)
    }

    fun navigateToMakeAPlan(selectedDays: List<Boolean>) {
        MakeAPlanScreen.setSelectedDay(selectedDays)
        _navigationEvents.value = NavigationEvent.NavigateTo(MakeAPlanScreen)
    }

    fun navigateToEditPlan(
        day: String,
        exercises: List<SelectedExercise>?,
        callback: (List<SelectedExercise>) -> Unit
    ) {
        EditPlanScreen.setSelectedDay(day)
        EditPlanScreen.setExercises(exercises)
        EditPlanScreen.setCallback(callback)
        _navigationEvents.value = NavigationEvent.NavigateTo(EditPlanScreen)
    }

    fun navigateToViewAllWorkouts(callback: (String) -> Unit) {
        ViewAllWorkoutScreen.setCallback(callback)
        _navigationEvents.value = NavigationEvent.NavigateTo(ViewAllWorkoutScreen)
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
}

sealed class NavigationEvent {
    data class NavigateTo(val screen: Screen) : NavigationEvent()
    object NavigateBack : NavigationEvent()
    object NavigateToRoot : NavigationEvent()
}
