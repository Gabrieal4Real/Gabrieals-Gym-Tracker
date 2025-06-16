package org.gabrieal.gymtracker.data.sqldelight

import org.gabrieal.gymtracker.data.model.FirebaseInfo
import org.gabrieal.gymtracker.data.model.Profile
import org.gabrieal.gymtracker.data.model.SelectedExerciseList
import org.gabrieal.gymtracker.data.model.WorkoutHistory
import org.gabrieal.gymtracker.data.model.WorkoutProgress
import org.gabrieal.gymtracker.util.systemUtil.formatInstantToDate
import org.gabrieal.gymtracker.util.systemUtil.parseDateToInstant
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

private val profileQuery = createDatabase().profileEntityQueries
private val selectedExerciseListQuery = createDatabase().selectedExerciseListEntityQueries
private val firebaseInfoQuery = createDatabase().firebaseInfoEntityQueries
private val currentlyActiveRoutineEntity = createDatabase().currentlyActiveRoutineEntityQueries
private val workoutHistoryEntity = createDatabase().workoutHistoryEntityQueries

@OptIn(ExperimentalTime::class)
fun updateWorkoutHistoryDB() {
    workoutHistoryEntity.insertIntoWorkoutHistory(formatInstantToDate(Clock.System.now(),"dd-MM-yyyy HH:mm:ss"))
}

fun getAllWorkoutHistoryFromDB(): List<WorkoutHistory> {
    val workoutHistory = workoutHistoryEntity.selectAllHistory().executeAsList()
    return workoutHistory.map {
        WorkoutHistory(
            id = it.id,
            finishedDate = it.finishedDate,
            routineName = it.routineName,
            startingDate = it.startingDate,
            exercises = it.exercises,
            startedOn = it.startedOn,
            workoutProgress = it.workoutProgress
        )
    }
}

@OptIn(ExperimentalTime::class)
fun setCurrentlyActiveRoutineToDB(activeRoutine: SelectedExerciseList?, startedOn: Instant, workoutProgress: WorkoutProgress) {
    if (activeRoutine == null) {
        currentlyActiveRoutineEntity.deleteCurrentlyActiveRoutine()
        return
    }

    currentlyActiveRoutineEntity.insertOrReplaceCurrentlyActiveRoutine(
        position = activeRoutine.position?.toLong(),
        day = activeRoutine.day,
        routineName = activeRoutine.routineName,
        isCompleted = if (activeRoutine.isCompleted) 0 else 1,
        startingDate = activeRoutine.startingDate,
        exercises = activeRoutine.exercises,
        startedOn = formatInstantToDate(startedOn, "dd-MM-yyyy HH:mm:ss"),
        workoutProgress = workoutProgress
    )
}

fun updateCurrentlyActiveRoutineToDB(workoutProgress: WorkoutProgress) {
    val currentlyActiveRoutine = currentlyActiveRoutineEntity.selectCurrentlyActiveRoutine().executeAsOneOrNull()

    if (currentlyActiveRoutine != null) {
        currentlyActiveRoutineEntity.insertOrReplaceCurrentlyActiveRoutine(
            position = currentlyActiveRoutine.position,
            day = currentlyActiveRoutine.day,
            routineName = currentlyActiveRoutine.routineName,
            isCompleted = currentlyActiveRoutine.isCompleted,
            startingDate = currentlyActiveRoutine.startingDate,
            exercises = currentlyActiveRoutine.exercises,
            startedOn = currentlyActiveRoutine.startedOn,
            workoutProgress = workoutProgress
        )
    }
}

@OptIn(ExperimentalTime::class)
fun getCurrentlyActiveRoutineFromDB(): Triple<SelectedExerciseList, Instant, WorkoutProgress>? {
    val currentlyActiveRoutine = currentlyActiveRoutineEntity.selectCurrentlyActiveRoutine().executeAsOneOrNull()

    currentlyActiveRoutine?.let {
        return Triple(
            SelectedExerciseList(
                position = it.position?.toInt(),
                day = it.day,
                routineName = it.routineName,
                isCompleted = it.isCompleted == 0.toLong(),
                startingDate = it.startingDate,
                exercises = it.exercises,
            ),
            parseDateToInstant(it.startedOn ?: "", "dd-MM-yyyy HH:mm:ss"),
            it.workoutProgress ?: WorkoutProgress()
        )
    }

    return null
}

fun setSelectedRoutineListToDB(list: List<SelectedExerciseList>) {
    selectedExerciseListQuery.deleteSelectedExerciseList()

    list.forEachIndexed { index, item ->
        selectedExerciseListQuery.insertOrReplaceSelectedExerciseList(
            id = index.toLong(),
            position = item.position?.toLong(),
            day = item.day,
            routineName = item.routineName,
            isCompleted = if (item.isCompleted) 0 else 1,
            startingDate = item.startingDate,
            exercises = item.exercises
        )
    }
}

fun getSelectedRoutineListFromDB(): MutableList<SelectedExerciseList> {
    val selectedExerciseList =
        selectedExerciseListQuery.selectAllSelectedExerciseLists().executeAsList()

    return selectedExerciseList.map {
        SelectedExerciseList(
            position = it.position?.toInt(),
            day = it.day,
            routineName = it.routineName,
            isCompleted = it.isCompleted == 0.toLong(),
            startingDate = it.startingDate,
            exercises = it.exercises
        )
    }.toMutableList()
}

fun setProfileToDB(profile: Profile) {
    profileQuery.insertOrReplaceProfile(
        email = profile.email,
        userName = profile.userName,
        weight = profile.weight,
        height = profile.height,
        age = profile.age?.toLong(),
        goal = profile.goal,
        activityLevel = profile.activityLevel,
        gender = profile.gender
    )
}

fun getProfileFromDB(): Profile {
    val profile = profileQuery.selectProfile().executeAsOneOrNull()

    profile?.let {
        return Profile(
            email = it.email,
            userName = it.userName,
            weight = it.weight,
            height = it.height,
            age = it.age?.toInt(),
            goal = it.goal,
            activityLevel = it.activityLevel,
            gender = it.gender
        )
    }

    return Profile()
}

fun setFirebaseInfoToDB(firebaseInfo: FirebaseInfo) {
    firebaseInfoQuery.insertOrReplaceFirebaseInfo(
        uid = firebaseInfo.uid,
        token = firebaseInfo.token,
        refreshToken = firebaseInfo.refreshToken,
        expiresAt = firebaseInfo.expiresAt
    )
}

fun getFirebaseInfoFromDB(): FirebaseInfo {
    val firebaseInfo = firebaseInfoQuery.selectFirebaseInfo().executeAsOneOrNull()

    firebaseInfo?.let {
        return FirebaseInfo(
            uid = it.uid,
            token = it.token,
            refreshToken = it.refreshToken,
            expiresAt = it.expiresAt
        )
    }

    return FirebaseInfo()
}