package org.gabrieal.gymtracker.features.home.viewmodel

import gymtracker.composeapp.generated.resources.Res
import gymtracker.composeapp.generated.resources.habit_1
import gymtracker.composeapp.generated.resources.habit_2
import gymtracker.composeapp.generated.resources.habit_3
import gymtracker.composeapp.generated.resources.habit_4
import gymtracker.composeapp.generated.resources.habit_5
import gymtracker.composeapp.generated.resources.habit_6
import gymtracker.composeapp.generated.resources.habit_7
import gymtracker.composeapp.generated.resources.habit_8
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
import kotlinx.coroutines.delay
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
import org.gabrieal.gymtracker.features.home.view.HomeTab.viewModel
import org.gabrieal.gymtracker.util.navigation.AppNavigator

class HomeViewModel(private val homeRepo: HomeRepo) {
    private val viewModelScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        requestSpotifyToken(
            listOf(
                "https://open.spotify.com/track/5Js7i1H7S2fNe1sbWfihyr?si=de46fdd55efd4c1d",
                "https://open.spotify.com/track/0iaa1DkqOki4FFGq3QjGs3?si=65c78c9b834642d0",
                "https://open.spotify.com/track/3K5KXm1uZjiyQk0J7op1xf?si=01468c515fe14746"
            )
        )
    }

    val listOfInfluencers = listOf(
        Res.drawable.workout_1 to "Lean Beef Patty • Female fitness influencer known for strength training and high-intensity workouts",
        Res.drawable.workout_2 to "Arnold Schwarzenegger • 7x Mr. Olympia, iconic bodybuilder and actor, pioneer of modern bodybuilding",
        Res.drawable.workout_3 to "Sam Sulek • Rising fitness influencer known for his intense bulk and raw, heavy lifting style",
        Res.drawable.workout_4 to "CBum • Chris Bumstead, 5x Classic Physique Mr. Olympia, known for aesthetics and discipline",
        Res.drawable.workout_5 to "Jeff Nippard • Evidence-based bodybuilder and educator with a focus on science-driven training • The tailored workouts in this app are based on his philosophy",
        Res.drawable.workout_7 to "Arnold Schwarzenegger, Robbie Robinson & Ken Waller • Golden era legends promoting camaraderie and classic physiques",
        Res.drawable.workout_8 to "Mike Mentzer • Creator of the Heavy Duty training philosophy, known for intensity and low-volume training"
    ).shuffled()

    val randomSelectedHabitImage = listOf(
        Res.drawable.habit_1,
        Res.drawable.habit_2,
        Res.drawable.habit_3,
        Res.drawable.habit_4,
        Res.drawable.habit_5,
        Res.drawable.habit_6,
        Res.drawable.habit_7,
        Res.drawable.habit_8
    ).random()

    val restDayMessage = listOf(
        "Time to recharge!\nRest up and treat yourself today.\n🔋🍰😴",
        "Kick back, relax, and let your body recover.\n🧘‍♂️💤🛀",
        "Netflix, snacks, and gains incoming.\n📺🍿🔥",
        "Even superheroes take a break—enjoy your rest day!\n🦸‍♂️🛌✨",
        "Take a deep breath, prioritise recovery and self-care.\n🌿🫁🕯️",
        "Your body grows when you rest.\nEmbrace the pause and feel good today.\n🌙💪🧠"
    ).random()

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

    private fun requestSpotifyToken(trackId: List<String>) {
        viewModelScope.launch {
            try {
                val spotifyToken = homeRepo.requestSpotifyToken()
                getTrackInfo(trackId, spotifyToken?.access_token.orEmpty())
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
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



    fun getListOfWorkoutImages() = listOfInfluencers

    fun getSpotifyAlbumDescription(spotifyTracks: SpotifyTracks?): List<Pair<String?, String>> {
        return spotifyTracks?.tracks?.map { it ->
            it.album.images.firstOrNull()?.url to "${it.name} • ${it.album.name} • ${it.artists.joinToString(", ") { it.name }}"
        } ?: emptyList()
    }
}
