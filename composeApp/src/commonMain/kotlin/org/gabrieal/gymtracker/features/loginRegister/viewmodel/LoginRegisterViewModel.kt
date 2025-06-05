package org.gabrieal.gymtracker.features.loginRegister.viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.gabrieal.gymtracker.data.model.Profile
import org.gabrieal.gymtracker.data.model.SelectedExercise
import org.gabrieal.gymtracker.features.loginRegister.repository.LoginRegisterRepo
import org.gabrieal.gymtracker.util.navigation.AppNavigator

class LoginRegisterViewModel(private val loginRegisterRepo: LoginRegisterRepo) {
    private val viewModelScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    private val _uiState = MutableStateFlow(LoginRegisterUiState())
    val uiState: StateFlow<LoginRegisterUiState> = _uiState.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private var callback: ((Profile?) -> Unit)? = null

    fun setProfile(profile: Profile) {
        _uiState.update { it.copy(profile = profile) }
    }

    fun setCallback(callback: (Profile?) -> Unit) {
        this.callback = callback
    }

    fun changeRegisterMode() {
        _uiState.update { it.copy(isRegisterMode = !uiState.value.isRegisterMode) }
    }

    fun loginExistingUser(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val isSuccess = loginRegisterRepo.loginAndSave(email, password, uiState.value.profile)
                if (isSuccess) {
                    callback?.invoke(uiState.value.profile)
                    AppNavigator.dismissBottomSheet()
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun registerNewUser(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val isSuccess = loginRegisterRepo.registerAndSave(email, password, uiState.value.profile)
                if (isSuccess) {
                    callback?.invoke(uiState.value.profile)
                    AppNavigator.dismissBottomSheet()
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            } finally {
                _isLoading.value = false
            }
        }
    }


    private fun updateProfile(update: (Profile) -> Profile) {
        val currentProfile = _uiState.value.profile
        val updatedProfile = update(currentProfile)
        _uiState.update { it.copy(profile = updatedProfile) }
    }

    fun updateProfileEmail(email: String)  = updateProfile { it.copy(email = email) }

    fun clear() {
        viewModelScope.cancel()
    }

    fun updatePassword(password: String) {
        _uiState.update { it.copy(password = password) }
    }

    fun updateProfileName(userName: String)  = updateProfile { it.copy(userName = userName) }
}