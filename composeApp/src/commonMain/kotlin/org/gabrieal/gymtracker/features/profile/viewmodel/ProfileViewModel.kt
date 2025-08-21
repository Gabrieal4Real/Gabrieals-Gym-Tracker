package org.gabrieal.gymtracker.features.profile.viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.gabrieal.gymtracker.data.model.CalorieInput
import org.gabrieal.gymtracker.data.model.Profile
import org.gabrieal.gymtracker.data.model.SelectedExerciseList
import org.gabrieal.gymtracker.data.network.APIService
import org.gabrieal.gymtracker.data.sqldelight.deleteSpotifyTokenFromDB
import org.gabrieal.gymtracker.data.sqldelight.getProfileFromDB
import org.gabrieal.gymtracker.data.sqldelight.getSelectedRoutineListFromDB
import org.gabrieal.gymtracker.data.sqldelight.getSpotifyTokenFromDB
import org.gabrieal.gymtracker.data.sqldelight.setProfileToDB
import org.gabrieal.gymtracker.data.sqldelight.updateSpotifyTokenToDB
import org.gabrieal.gymtracker.features.profile.repository.ProfileRepo
import org.gabrieal.gymtracker.util.app.generateGoalBreakdown
import org.gabrieal.gymtracker.util.enums.ActivityLevel
import org.gabrieal.gymtracker.util.enums.Gender
import org.gabrieal.gymtracker.util.navigation.AppNavigator
import org.gabrieal.gymtracker.util.systemUtil.PKCE
import org.gabrieal.gymtracker.util.systemUtil.SpotifyRedirectHandler

class ProfileViewModel(private val profileRepo: ProfileRepo) {

    private val viewModelScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            SpotifyRedirectHandler.codeFlow.collect { code ->
                loginViaSpotify(code)
            }
        }
    }

    private var currentVerifier: String = ""

    fun updateContext() {
        loadRoutines()
        loadProfile()
    }

    private fun loadRoutines() {
        val routines = getSelectedRoutineListFromDB()
        _uiState.update {
            if (routines.isEmpty()) {
                return
            }
            it.copy(selectedRoutineList = routines)
        }
    }

    private fun loadProfile() {
        val profile = getProfileFromDB()
        _uiState.update { it.copy(profile = profile) }
    }

    fun setWeightHeightBMIClicked(weightHeightBMIClicked: Int) {
        _uiState.update { it.copy(weightHeightBMIClicked = weightHeightBMIClicked) }
    }

    private fun updateProfile(update: (Profile) -> Profile) {
        val currentProfile = _uiState.value.profile ?: Profile()
        val updatedProfile = update(currentProfile)
        _uiState.update { it.copy(profile = updatedProfile) }
        saveProfile()
    }

    fun updateWeight(weight: Double?) = updateProfile { it.copy(weight = weight) }

    fun updateHeight(height: Double?) = updateProfile { it.copy(height = height) }

    fun updateAge(age: Int?) = updateProfile { it.copy(age = age) }

    private fun saveProfile() {
        val profile = _uiState.value.profile
        profile?.let { setProfileToDB(it) }
    }

    fun navigateToEditSplit(routines: List<SelectedExerciseList>) =
        AppNavigator.navigateToEditSplit(routines)

    fun navigateToWorkoutHistory() = AppNavigator.navigateToWorkoutHistory()

    fun navigateToProteinCalculator() =
        AppNavigator.navigateToCalculatorScreen("Protein Intake", uiState.value.profile)

    fun navigateToMaintenanceCalculator() {
        val input = CalorieInput(
            gender = uiState.value.profile?.gender ?: Gender.MALE,
            age = uiState.value.profile?.age ?: 27,
            weightKg = uiState.value.profile?.weight ?: 70.0,
            heightCm = uiState.value.profile?.height ?: 175.0,
            activityLevel = uiState.value.profile?.activityLevel ?: ActivityLevel.MODERATELY_ACTIVE
        )

        val breakdown = generateGoalBreakdown(input)

        breakdown.forEach {
            println("${it.label} (${it.weightChangePerWeekKg} kg/week): ${it.calories} kcal/day (${it.percentageOfMaintenance}%)")
        }
    }

    fun launchSpotifyAuthBrowser() {
        currentVerifier = PKCE.generateCodeVerifier()
        val challenge = PKCE.generateCodeChallenge(currentVerifier)
        setSpotifyUrl(APIService.authUrl(challenge))
    }

    fun setSpotifyUrl(url: String?) {
        _uiState.update { it.copy(spotifyUrl = url) }
    }

    fun loginViaSpotify(accessToken: String) {
        AppNavigator.showLoading()

        viewModelScope.launch {
            profileRepo.getExchangeToken(accessToken, currentVerifier)
                .catch { e ->
                    _uiState.update { it.copy(error = e.message) }
                }
                .collect { response ->
                    updateSpotifyTokenToDB(response)
                    getSpotifyUserInfo()
                }
            AppNavigator.hideLoading()
        }
    }

    fun getSpotifyUserInfo() {
        val spotifyToken = getSpotifyTokenFromDB()

        if (spotifyToken?.access_token.isNullOrBlank()) return

        AppNavigator.showLoading()
        viewModelScope.launch {
            profileRepo.getSpotifyProfile(spotifyToken.access_token)
                .catch { e ->
                    _uiState.update { it.copy(error = e.message) }
                }
                .collect { userInfo ->
                    updateProfile {
                        it.copy(
                            userName = userInfo.display_name,
                            email = userInfo.email,
                            profileImage = userInfo.images?.firstOrNull()?.url
                        )
                    }
                }
            AppNavigator.hideLoading()
        }
    }

    fun setLoggingOut(loggingOut: Boolean) = _uiState.update { it.copy(loggingOut = loggingOut) }

    fun logout() {
        deleteSpotifyTokenFromDB()
        updateProfile {
            it.copy(
                userName = null,
                email = null,
                profileImage = null
            )
        }

        setLoggingOut(false)
    }
}

