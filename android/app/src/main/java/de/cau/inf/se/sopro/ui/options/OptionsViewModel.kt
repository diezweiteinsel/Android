package de.cau.inf.se.sopro.ui.options

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.cau.inf.se.sopro.data.Repository
import de.cau.inf.se.sopro.di.UrlManager
import de.cau.inf.se.sopro.ui.utils.HealthStatus
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class OptionsViewModel @Inject constructor(
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

    fun toDefaultUrl() {
        val defaultUrl = UrlManager.DEFAULT_URL

        urlManager.saveUrl(defaultUrl)

        _uiState.update {
            it.copy(
                url = defaultUrl,
                showRestartMessage = true,
                urlError = null
            )
        }
    }

    fun checkHealth() {
        val urlToCheck = _uiState.value.url

        viewModelScope.launch {
            _uiState.update { it.copy(healthStatus = HealthStatus.LOADING) }

            val isSuccess = repository.checkHealth(urlToCheck)

            val newStatus = if (isSuccess) HealthStatus.SUCCESS else HealthStatus.FAILURE
            _uiState.update { it.copy(healthStatus = newStatus) }
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
        viewModelScope.launch {
            repository.logout()
            onDismissLogoutDialog()
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


    fun onDismissRestartMessage() {
        _uiState.update { currentState ->
            currentState.copy(showRestartMessage = false)
        }
    }

    data class OptionsUiState(
        val showLogoutDialog: Boolean = false,
        val showRestartMessage: Boolean = false,
        val url: String = "",
        val urlError: String? = null,
        val healthStatus: HealthStatus = HealthStatus.IDLE
    )
}

