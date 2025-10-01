package de.cau.inf.se.sopro.ui.options

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.cau.inf.se.sopro.data.Repository
import de.cau.inf.se.sopro.di.UrlManager
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OptionsViewModel(
    private val repository: Repository,
    private val urlManager: UrlManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(OptionsUiState())
    val uiState = _uiState.asStateFlow()

    private val _logoutEvent = MutableSharedFlow<Unit>()
    val logoutEvent = _logoutEvent.asSharedFlow()

    fun onUrlChange(newUrl: String) {
        _uiState.update {
            it.copy(url = newUrl, urlError = null)
        }
    }

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
        repository.logout()
        onDismissLogoutDialog()

        viewModelScope.launch {
            _logoutEvent.emit(Unit)
        }
    }

    fun onSaveUrl() {
        val currentUrl = _uiState.value.url

        if (currentUrl.isNotBlank() && (currentUrl.startsWith("http://") || currentUrl.startsWith("https://"))) {
            urlManager.saveUrl(currentUrl)

            _uiState.update { it.copy(showRestartMessage = true) }
        } else {
            _uiState.update { it.copy(urlError = "Ungültige URL") }
        }
    }

    fun onRestartMessageDismissed() {
        _uiState.update { it.copy(showRestartMessage = false) }
    }

    fun onDismissRestartMessage() {
        _uiState.update { currentState ->
            currentState.copy(showRestartMessage = false)
        }
    }
}

data class OptionsUiState(
    val showLogoutDialog: Boolean = false,
    val showRestartMessage: Boolean = false,
    val url: String = "",
    val urlError: String? = null
)