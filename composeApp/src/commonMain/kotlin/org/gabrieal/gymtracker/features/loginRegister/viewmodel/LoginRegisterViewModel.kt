package org.gabrieal.gymtracker.features.loginRegister.viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.gabrieal.gymtracker.data.model.FirebaseInfo
import org.gabrieal.gymtracker.data.model.Profile
import org.gabrieal.gymtracker.features.loginRegister.repository.LoginRegisterRepo
import org.gabrieal.gymtracker.util.navigation.AppNavigator
import org.gabrieal.gymtracker.data.sqldelight.getFirebaseInfoFromDB
import org.gabrieal.gymtracker.data.sqldelight.setFirebaseInfoToDB

class LoginRegisterViewModel(private val loginRegisterRepo: LoginRegisterRepo) {
    private val viewModelScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    private val _uiState = MutableStateFlow(LoginRegisterUiState())
    val uiState: StateFlow<LoginRegisterUiState> = _uiState.asStateFlow()

    private var callback: ((Profile?) -> Unit)? = null

    fun setProfile(profile: Profile) = _uiState.update { it.copy(profile = profile) }

    fun setCallback(callback: (Profile?) -> Unit) {
        this.callback = callback
    }

    fun changeRegisterMode() = _uiState.update { it.copy(isRegisterMode = !it.isRegisterMode) }

    fun loginExistingUser(email: String, password: String) = performAuth(email, password, isRegister = false)

    fun registerNewUser(email: String, password: String) = performAuth(email, password, isRegister = true)

    private fun performAuth(email: String, password: String, isRegister: Boolean) {

        viewModelScope.launch {
            AppNavigator.showLoading()
            try {

                val authResponse = if (isRegister) {
                    loginRegisterRepo.registerUser(email, password)
                } else {
                    loginRegisterRepo.loginUser(email, password)
                }

                setFirebaseInfoToDB(
                    FirebaseInfo(
                        uid = authResponse.localId,
                        token = authResponse.idToken
                    )
                )

                if (isRegister) registerUser() else fetchUser()
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            } finally {
                AppNavigator.hideLoading()
            }
        }
    }

    private fun fetchUser() {
        val firebaseInfo = getFirebaseInfoFromDB()
        if (firebaseInfo.uid.isNullOrBlank() || firebaseInfo.token.isNullOrBlank()) {
            _uiState.update { it.copy(error = "Something went wrong") }
            return
        }

        viewModelScope.launch {
            AppNavigator.showLoading()
            try {
                val profile = loginRegisterRepo.fetchUser(firebaseInfo.uid!!, firebaseInfo.token!!)
                _uiState.update { it.copy(profile = profile) }
                returnUser()
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            } finally {
                AppNavigator.hideLoading()
            }
        }
    }

    private fun registerUser() {
        val firebaseInfo = getFirebaseInfoFromDB()
        if (firebaseInfo.uid.isNullOrBlank() || firebaseInfo.token.isNullOrBlank()) {
            _uiState.update { it.copy(error = "Something went wrong") }
            return
        }

        viewModelScope.launch {
            AppNavigator.showLoading()
            try {
                updateProfile(uiState.value.email, uiState.value.userName)

                val isSuccess = loginRegisterRepo.saveUser(
                    firebaseInfo.uid!!,
                    firebaseInfo.token!!,
                    uiState.value.profile
                )

                if (isSuccess) returnUser()
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            } finally {
                AppNavigator.hideLoading()
            }
        }
    }

    private fun returnUser() {
        AppNavigator.hideLoading()
        clearUserInput()
        callback?.invoke(uiState.value.profile)
        AppNavigator.dismissBottomSheet()
    }

    private fun clearUserInput() = _uiState.update { it.copy(userName = "", email = "", password = "") }
    

    private fun updateProfile(email: String? = null, userName: String? = null) {
        val currentProfile = _uiState.value.profile
        val updatedProfile = currentProfile.copy(
            email = email ?: currentProfile.email,
            userName = userName ?: currentProfile.userName
        )
        _uiState.update { it.copy(profile = updatedProfile) }
    }

    
    fun updateError() = _uiState.update { it.copy(error = null) }

    fun updateName(userName: String) = _uiState.update { it.copy(userName = userName) }

    fun updateEmail(email: String) = _uiState.update { it.copy(email = email) }

    fun updatePassword(password: String) = _uiState.update { it.copy(password = password) }
}