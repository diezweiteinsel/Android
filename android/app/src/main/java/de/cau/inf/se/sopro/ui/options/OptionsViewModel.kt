package de.cau.inf.se.sopro.ui.options

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OptionsViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(OptionsUiState())

    val uiState = _uiState.asStateFlow()

    private val _logoutEvent = MutableSharedFlow<Unit>()
    val logoutEvent = _logoutEvent.asSharedFlow()

    fun onLogoutClick() {
        _uiState.update { currentState ->
            currentState.copy(showLogoutDialog = true)
        }
    }

    fun onDismissLogoutDialog() {
        _uiState.update { currentState ->
            currentState.copy(showLogoutDialog = false)
        }
    }

    fun onConfirmLogout() {

        onDismissLogoutDialog()

        viewModelScope.launch {
            _logoutEvent.emit(Unit)
        }
    }
}

data class OptionsUiState(
    val showLogoutDialog: Boolean = false
)