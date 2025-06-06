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
import org.gabrieal.gymtracker.util.systemUtil.Loader
import org.gabrieal.gymtracker.util.systemUtil.getFirebaseInfoFromSharedPreferences
import org.gabrieal.gymtracker.util.systemUtil.setFirebaseInfoToSharedPreferences

class LoginRegisterViewModel(private val loginRegisterRepo: LoginRegisterRepo) {
    private val viewModelScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    private val _uiState = MutableStateFlow(LoginRegisterUiState())
    val uiState: StateFlow<LoginRegisterUiState> = _uiState.asStateFlow()

    private var callback: ((Profile?) -> Unit)? = null

    fun setProfile(profile: Profile) = _uiState.update { it.copy(profile = profile) }

    fun setCallback(callback: (Profile?) -> Unit) {
        this.callback = callback
    }

    fun changeRegisterMode() {
        _uiState.update { it.copy(isRegisterMode = !it.isRegisterMode) }
    }

    fun loginExistingUser(email: String, password: String) = performAuth(email, password, isRegister = false)

    fun registerNewUser(email: String, password: String) = performAuth(email, password, isRegister = true)

    private fun performAuth(email: String, password: String, isRegister: Boolean) {
        viewModelScope.launch {
            Loader.show()
            try {
                val authResponse = if (isRegister) {
                    loginRegisterRepo.registerUser(email, password)
                } else {
                    loginRegisterRepo.loginUser(email, password)
                }

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
                Loader.hide()
            }
        }
    }

    private fun saveUser() {
        val firebaseInfo = getFirebaseInfoFromSharedPreferences()
        if (firebaseInfo.uid.isNullOrBlank() || firebaseInfo.token.isNullOrBlank()) return

        viewModelScope.launch {
            Loader.show()
            try {
                updateProfile(uiState.value.email, uiState.value.userName)

                val isSuccess = loginRegisterRepo.saveUser(
                    firebaseInfo.uid!!,
                    firebaseInfo.token!!,
                    uiState.value.profile
                )
                if (isSuccess) {
                    clearUserInput()
                    callback?.invoke(uiState.value.profile)
                    AppNavigator.dismissBottomSheet()
                    clear()
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            } finally {
                Loader.hide()
            }
        }
    }

    private fun clearUserInput() {
        _uiState.update { it.copy(userName = "", email = "", password = "") }
    }

    private fun updateProfile(email: String? = null, userName: String? = null) {
        val currentProfile = _uiState.value.profile
        val updatedProfile = currentProfile.copy(
            email = email ?: currentProfile.email,
            userName = userName ?: currentProfile.userName
        )
        _uiState.update { it.copy(profile = updatedProfile) }
    }

    private fun clear() {
        viewModelScope.cancel()
    }

    fun updateError() = _uiState.update { it.copy(error = null) }

    fun updateName(userName: String) = _uiState.update { it.copy(userName = userName) }

    fun updateEmail(email: String) = _uiState.update { it.copy(email = email) }

    fun updatePassword(password: String) = _uiState.update { it.copy(password = password) }
}