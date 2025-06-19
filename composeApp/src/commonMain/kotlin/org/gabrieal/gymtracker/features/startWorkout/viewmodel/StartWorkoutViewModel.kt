package org.gabrieal.gymtracker.features.startWorkout.viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import network.chaintech.cmpcharts.common.extensions.roundTwoDecimal
import org.gabrieal.gymtracker.data.model.SelectedExerciseList
import org.gabrieal.gymtracker.data.model.WorkoutProgress
import org.gabrieal.gymtracker.data.sqldelight.getSpecificWorkoutHistoryFromDB
import org.gabrieal.gymtracker.data.sqldelight.setCurrentlyActiveRoutineToDB
import org.gabrieal.gymtracker.data.sqldelight.updateCurrentlyActiveRoutineToDB
import org.gabrieal.gymtracker.util.navigation.AppNavigator
import org.gabrieal.gymtracker.util.systemUtil.allowScreenSleep
import org.gabrieal.gymtracker.util.systemUtil.keepScreenOn
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class StartWorkoutViewModel {

    private val _uiState = MutableStateFlow(StartWorkoutUiState())
    private val viewModelScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    val uiState: StateFlow<StartWorkoutUiState> = _uiState.asStateFlow()

    private var callback: ((SelectedExerciseList) -> Unit)? = null
    private var failureCallback: (() -> Unit)? = null

    fun setSelectedExerciseList(selectedExerciseList: SelectedExerciseList) =
        _uiState.update { it.copy(selectedExerciseList = selectedExerciseList) }

    fun markWorkoutAsDone() {
        _uiState.update { it.copy(selectedExerciseList = it.selectedExerciseList?.copy(isCompleted = true)) }

        _uiState.value.selectedExerciseList?.let {
            callback?.invoke(it)
            AppNavigator.navigateBack()
        }
    }

    fun markWorkoutAsCancelled() {
        failureCallback?.invoke()
    }

    fun setCallback(callback: (SelectedExerciseList) -> Unit) {
        this.callback = callback
    }
    
    fun setFailureCallback(failureCallback: () -> Unit) {
        this.failureCallback = failureCallback
    }

    @OptIn(ExperimentalTime::class)
    fun startWorkout(currentActiveExercise: SelectedExerciseList?) {
        val weightList = mutableListOf<String>()
        val repsList = mutableListOf<MutableList<String>>()
        val completedSetsList = mutableListOf<MutableList<Boolean>>()
        val weightUnit = mutableListOf<Boolean>()

        currentActiveExercise?.exercises?.forEach { exercise ->
            val setCount = exercise.sets ?: 0

            weightList.add("")
            repsList.add(MutableList(setCount) { "" })
            completedSetsList.add(MutableList(setCount) { false })
            weightUnit.add(true)
        }

        setCurrentlyActiveRoutineToDB(
            currentActiveExercise, Clock.System.now(), WorkoutProgress(
                exerciseWeights = weightList,
                exerciseReps = repsList,
                exerciseSets = completedSetsList,
                exerciseWeightUnit = weightUnit
            )
        )

        updateCurrentActiveExercise(currentActiveExercise)
    }

    fun updateCurrentActiveExercise(currentActiveExercise: SelectedExerciseList?) =
        _uiState.update { it.copy(currentActiveExercise = currentActiveExercise) }

    fun setShowWarningReplace(show: Boolean) =
        _uiState.update { it.copy(showWarningReplace = show) }

    fun initializeCompletedSets(selectedExerciseList: SelectedExerciseList) {
        if (_uiState.value.workoutProgress.exerciseWeights.isNotEmpty()
            && selectedExerciseList.exercises?.size == _uiState.value.workoutProgress.exerciseWeights.size
            && selectedExerciseList == _uiState.value.selectedExerciseList
        ) {
            return
        }

        setSelectedExerciseList(selectedExerciseList)

        val expandedList = mutableListOf<Boolean>()

        selectedExerciseList.exercises?.forEach { _ ->
            expandedList.add(false)
        }

        _uiState.update { it.copy(expandedExercises = expandedList) }
    }

    fun toggleExerciseExpanded(index: Int) =
        _uiState.update { state ->
            val toggled = !state.expandedExercises[index]
            state.copy(expandedExercises = List(state.expandedExercises.size) { it == index && toggled })
        }

    private fun updateWorkoutProgress(update: (WorkoutProgress) -> WorkoutProgress) {
        val workoutProgress = _uiState.value.workoutProgress
        val updatedWorkoutProgress = update(workoutProgress)
        _uiState.update { it.copy(workoutProgress = updatedWorkoutProgress) }
        updateCurrentlyActiveRoutineToDB(updatedWorkoutProgress)
    }

    fun updateWeight(index: Int, weight: String) =
        updateWorkoutProgress {
            it.copy(
                exerciseWeights = it.exerciseWeights.toMutableList().apply { this[index] = weight })
        }

    fun toggleWeightUnit(index: Int) =
        updateWorkoutProgress {
            it.copy(
                exerciseWeightUnit = it.exerciseWeightUnit.toMutableList().apply { this[index] = !this[index] })
        }

    fun updateReps(index: Int, reps: String, repIndex: Int) =
        updateWorkoutProgress {
            it.copy(
                exerciseReps = it.exerciseReps.toMutableList().apply {
                    this[index] = this[index].toMutableList().apply { this[repIndex] = reps }
                })
        }

    fun updateExerciseSets(index: Int, position: Int) =
        updateWorkoutProgress {
            it.copy(
                exerciseSets = it.exerciseSets.toMutableList().apply {
                    this[index] =
                        this[index].toMutableList().apply { this[position] = !this[position] }
                })
        }

    fun toggleSetCompleted() {
        val weights = _uiState.value.workoutProgress.exerciseWeights
        val reps = _uiState.value.workoutProgress.exerciseReps
        val sets = _uiState.value.workoutProgress.exerciseSets
        val weightUnit = _uiState.value.workoutProgress.exerciseWeightUnit

        var completedVolume = 0.0

        weights.forEachIndexed { index, weight ->
            val repsList = reps[index]
            val setsList = sets[index]

            repsList.forEachIndexed { repIndex, reps ->
                if (setsList[repIndex]) {
                    completedVolume += if (weightUnit[index]) {
                        (if (weight.isEmpty()) 0.0 else weight.toDouble()) * (if (reps.isEmpty()) 0.0 else reps.toDouble())
                    } else {
                        (if (weight.isEmpty()) 0.0 else weight.toDouble() / 2.205) * (if (reps.isEmpty()) 0.0 else reps.toDouble())
                    }

                }
            }
        }

        _uiState.update { it.copy(completedVolume = completedVolume.roundTwoDecimal()) }
    }

    private var timerJob: Job? = null

    private fun pauseTimer() {
        timerJob?.cancel()
        _uiState.update { it.copy(isRunning = false) }
    }

    fun startTimer() {
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                keepScreenOn()
            }
        }

        timerJob?.cancel()
        _uiState.update { it.copy(isRunning = true) }

        timerJob = viewModelScope.launch {
            while (_uiState.value.currentTime > 0) {
                delay(1000L)
                if (_uiState.value.isRunning) {
                    _uiState.update { it.copy(currentTime = _uiState.value.currentTime - 1) }
                    if (_uiState.value.currentTime < 1) {
                        setShowNotification(true)
                    }
                }
            }
            resetTimer()
        }
    }

    fun restartTimer() {
        _uiState.update { it.copy(currentTime = _uiState.value.totalTime) }
        startTimer()
    }

    fun setShowNotification(show: Boolean) = _uiState.update { it.copy(showNotification = show) }

    fun setTotalTime(seconds: Int) {
        _uiState.update {
            it.copy(
                totalTime = seconds,
                currentTime = seconds,
                isRunning = false
            )
        }
        timerJob?.cancel()
    }

    fun startOrPauseTimer() = if (_uiState.value.isRunning) {
        pauseTimer()
    } else {
        startTimer()
    }

    fun addTime(seconds: Int) {
        _uiState.update {
            it.copy(
                totalTime = it.totalTime + seconds,
                currentTime = it.currentTime + seconds
            )
        }
    }

    fun resetTimer() {
        _uiState.update {
            it.copy(
                totalTime = 0,
                currentTime = 0,
                isRunning = false
            )
        }

        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                allowScreenSleep()
            }
        }
    }

    fun setWorkoutProgress(workoutProgress: WorkoutProgress) =
        _uiState.update { it.copy(workoutProgress = workoutProgress) }

    fun getPreviousWorkout(): List<String> {
        return getSpecificWorkoutHistoryFromDB(_uiState.value.selectedExerciseList?.routineName ?: "")?.workoutProgress?.exerciseWeights ?: emptyList()
    }
}
