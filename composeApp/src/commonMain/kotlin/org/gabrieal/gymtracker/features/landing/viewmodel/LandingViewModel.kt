package org.gabrieal.gymtracker.features.landing.viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.gabrieal.gymtracker.data.model.SelectedExerciseList
import org.gabrieal.gymtracker.data.model.SpotifyPlayerState
import org.gabrieal.gymtracker.data.sqldelight.getSelectedRoutineListFromDB
import org.gabrieal.gymtracker.data.sqldelight.getSpotifyTokenFromDB
import org.gabrieal.gymtracker.features.home.repository.HomeRepo
import org.gabrieal.gymtracker.features.landing.repository.LandingRepo
import org.gabrieal.gymtracker.util.app.resetAllCompletedStatus

class LandingViewModel(private val homeRepo: HomeRepo, private val landingRepo: LandingRepo) {
    private val viewModelScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    private val _uiState = MutableStateFlow(LandingUiState())
    val uiState: StateFlow<LandingUiState> = _uiState.asStateFlow()

    fun setCurrentlyActiveRoutine(currentlyActiveRoutine: SelectedExerciseList?) =
        _uiState.update { it.copy(currentlyActiveRoutine = currentlyActiveRoutine) }

    fun resetCompletedList() =
        setCompletedRoutineList(resetAllCompletedStatus(getSelectedRoutineListFromDB()))

    fun setCompletedRoutineList(resetCompletedList: Boolean) =
        _uiState.update { it.copy(resetCompletedList = resetCompletedList) }

    private var timerJob: Job? = null

    fun needRefreshCurrentPlayback() {
        val spotifyToken = getSpotifyTokenFromDB()

        if (spotifyToken?.access_token.isNullOrBlank()) return

        println("Refreshing playback...")

        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            getCurrentPlayback()
        }
    }

    private fun getCurrentWaitTimeInMillis(): Long {
        val currentPlayback = _uiState.value.spotifyPlayback
        if (currentPlayback == null || currentPlayback.is_playing == false) return 20_000L

        val currentWaitTime = currentPlayback.progress_ms ?: 0L
        val currentTrackDuration = currentPlayback.item?.duration_ms ?: 20_000L

        return currentTrackDuration - currentWaitTime
    }

    fun getCurrentPlayback() {
        val spotifyToken = getSpotifyTokenFromDB()

        if (spotifyToken?.access_token.isNullOrBlank()) return

        viewModelScope.launch {
            homeRepo.getCurrentPlayback(spotifyToken.access_token)
                .catch { e ->

                }
                .collect { currentPlayback ->
                    _uiState.update { it.copy(spotifyPlayback = currentPlayback) }
                    delay(getCurrentWaitTimeInMillis())
                    needRefreshCurrentPlayback()
                }
        }
    }

    fun postPlayerState(playerState: SpotifyPlayerState) {
        val spotifyToken = getSpotifyTokenFromDB()

        if (spotifyToken?.access_token.isNullOrBlank()) return

        viewModelScope.launch {
            landingRepo.postPlayerState(spotifyToken.access_token, playerState)
                .catch { e ->

                }
                .collect {
                    getCurrentPlayback()
                }
        }
    }
}
