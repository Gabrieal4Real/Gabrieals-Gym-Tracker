package org.gabrieal.gymtracker.features.home.viewmodel

import gymtracker.composeapp.generated.resources.Res
import gymtracker.composeapp.generated.resources.workout_1
import gymtracker.composeapp.generated.resources.workout_2
import gymtracker.composeapp.generated.resources.workout_3
import gymtracker.composeapp.generated.resources.workout_4
import gymtracker.composeapp.generated.resources.workout_5
import gymtracker.composeapp.generated.resources.workout_7
import gymtracker.composeapp.generated.resources.workout_8
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.gabrieal.gymtracker.data.model.SelectedExerciseList
import org.gabrieal.gymtracker.data.model.SpotifyTracks
import org.gabrieal.gymtracker.data.sqldelight.getSelectedRoutineListFromDB
import org.gabrieal.gymtracker.data.sqldelight.setSelectedRoutineListToDB
import org.gabrieal.gymtracker.features.home.repository.HomeRepo
import org.gabrieal.gymtracker.util.navigation.AppNavigator

class HomeViewModel(private val homeRepo: HomeRepo) {
    private val viewModelScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun updateContext() = loadRoutines()

    private fun loadRoutines() {
        val routines = getSelectedRoutineListFromDB()
        _uiState.update {
            if (routines.isEmpty()) {
                navigateToCreateSplit()
            }

            it.copy(
                selectedRoutineList = routines,
                hasRoutines = routines.isNotEmpty()
            )
        }
    }

    private fun navigateToCreateSplit() = AppNavigator.navigateToCreateSplit()

    fun navigateToStartWorkout(
        selectedExerciseList: SelectedExerciseList,
        callback: (SelectedExerciseList) -> Unit
    ) {
        AppNavigator.navigateToStartWorkout(selectedExerciseList, callback)
    }

    private fun saveRoutineList() {
        val sortedRoutineList = _uiState.value.selectedRoutineList
        setSelectedRoutineListToDB(sortedRoutineList)
        AppNavigator.navigateToRoot()
    }

    fun updateSelectedRoutine(selectedRoutine: SelectedExerciseList) {
        uiState.value.selectedRoutineList.forEachIndexed { index, routine ->
            if (routine.day == selectedRoutine.day) {
                _uiState.update {
                    it.copy(
                        selectedRoutineList = it.selectedRoutineList.toMutableList().apply {
                            this[index] = selectedRoutine
                        }
                    )
                }
                saveRoutineList()
            }
        }
    }

    fun requestSpotifyToken(trackId: List<String>) {
        viewModelScope.launch {
            AppNavigator.showLoading()
            try {
                val spotifyToken = homeRepo.requestSpotifyToken()
                getTrackInfo(trackId, spotifyToken?.access_token.orEmpty())
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            } finally {
                AppNavigator.hideLoading()
            }
        }
    }

    private fun getTrackInfo(trackId: List<String>, token: String) {
        viewModelScope.launch {
            try {
                val spotifyTracks = homeRepo.getTrackInfo(trackId, token)
                _uiState.update { it.copy(spotifyTracks = spotifyTracks) }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            } finally {
                AppNavigator.hideLoading()
            }
        }
    }

    fun getListOfWorkoutImages() = listOf(
        Res.drawable.workout_1 to "Lean Beef Patty • Female fitness influencer known for strength training and high-intensity workouts",
        Res.drawable.workout_2 to "Arnold Schwarzenegger • 7x Mr. Olympia, iconic bodybuilder and actor, pioneer of modern bodybuilding",
        Res.drawable.workout_3 to "Sam Sulek • Rising fitness influencer known for his intense bulk and raw, heavy lifting style",
        Res.drawable.workout_4 to "CBum • Chris Bumstead, 5x Classic Physique Mr. Olympia, known for aesthetics and discipline",
        Res.drawable.workout_5 to "Jeff Nippard • Evidence-based bodybuilder and educator with a focus on science-driven training • The tailored workouts in this app are based on his philosophy",
        Res.drawable.workout_7 to "Arnold Schwarzenegger, Robbie Robinson & Ken Waller • Golden era legends promoting camaraderie and classic physiques",
        Res.drawable.workout_8 to "Mike Mentzer • Creator of the Heavy Duty training philosophy, known for intensity and low-volume training"
    )

    fun getSpotifyAlbumDescription(spotifyTracks: SpotifyTracks?): List<Pair<String?, String>> {
        return spotifyTracks?.tracks?.map { it ->
            it.album.images.firstOrNull()?.url to "${it.name} • ${it.album.name} • ${it.artists.joinToString(", ") { it.name }}"
        } ?: emptyList()
    }
}
