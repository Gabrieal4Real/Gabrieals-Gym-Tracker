package org.gabrieal.gymtracker.features.app.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AppStateViewModel {
    private val _uiState = MutableStateFlow(AppStateUiState())
    val uiState: StateFlow<AppStateUiState> = _uiState.asStateFlow()

    fun showLoading() = _uiState.update { it.copy(isLoading = true) }
    fun hideLoading() = _uiState.update { it.copy(isLoading = false) }
}