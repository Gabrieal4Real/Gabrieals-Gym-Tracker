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
import org.gabrieal.gymtracker.data.model.FirebaseInfo
import org.gabrieal.gymtracker.data.model.Profile
import org.gabrieal.gymtracker.features.loginRegister.repository.LoginRegisterRepo
import org.gabrieal.gymtracker.util.navigation.AppNavigator
import org.gabrieal.gymtracker.util.systemUtil.getFirebaseInfoFromSharedPreferences
import org.gabrieal.gymtracker.util.systemUtil.setFirebaseInfoToSharedPreferences

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
                val authResponse = loginRegisterRepo.loginUser(email, password)
                setFirebaseInfoToSharedPreferences(
                    FirebaseInfo(
                        uid = authResponse.localId,
                        token = authResponse.idToken
                    )
                )
                saveUser()
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
                val authResponse = loginRegisterRepo.registerUser(email, password)
                setFirebaseInfoToSharedPreferences(
                    FirebaseInfo(
                        uid = authResponse.localId,
                        token = authResponse.idToken
                    )
                )
                saveUser()
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
                _isLoading.value = false
            }
        }
    }

    private fun saveUser() {
        val firebaseInfo = getFirebaseInfoFromSharedPreferences()
        if (firebaseInfo.uid.isNullOrEmpty() || firebaseInfo.token.isNullOrEmpty()) {
            _isLoading.value = false
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val isSuccess = loginRegisterRepo.saveUser(
                    firebaseInfo.uid!!,
                    firebaseInfo.token!!,
                    uiState.value.profile
                )
                if (isSuccess) {
                    callback?.invoke(uiState.value.profile)
                    AppNavigator.dismissBottomSheet()
                    clear()
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

    fun updateProfileEmail(email: String) = updateProfile { it.copy(email = email) }

    private fun clear() {
        viewModelScope.cancel()
    }

    fun updatePassword(password: String) {
        _uiState.update { it.copy(password = password) }
    }

    fun updateProfileName(userName: String) = updateProfile { it.copy(userName = userName) }
}