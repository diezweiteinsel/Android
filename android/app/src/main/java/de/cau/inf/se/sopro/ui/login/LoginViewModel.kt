package de.cau.inf.se.sopro.ui.login

import android.util.Log
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavHostController
import de.cau.inf.se.sopro.CivitasApplication
import de.cau.inf.se.sopro.R
import de.cau.inf.se.sopro.data.LoginResult
import de.cau.inf.se.sopro.data.Repository
import de.cau.inf.se.sopro.di.UrlManager
import de.cau.inf.se.sopro.ui.navigation.AppDestination
import de.cau.inf.se.sopro.ui.navigation.navigateTopLevel
import de.cau.inf.se.sopro.ui.utils.HealthStatus
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class LoginViewModel(
    private val repository: Repository,
    private val urlManager: UrlManager
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _loginSuccess = MutableSharedFlow<Boolean>()
    val loginSuccess: SharedFlow<Boolean> = _loginSuccess

    var username by mutableStateOf("")
    var password by mutableStateOf("")

    fun onUsernameChange(username: String) {
        _uiState.update { currentState ->
            currentState.copy(
                username = currentState.username.copy(value = username, errorMessageResId = null),
            )
        }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { currentState ->
            currentState.copy(
                password = currentState.password.copy(value = password, errorMessageResId = null),
            )
        }
    }

    fun onLoginClick() {
        val username = _uiState.value.username
        val password = _uiState.value.password

        val usernameError = when {
            username.value.isBlank() -> R.string.username_is_empty
            else -> null
        }

        val passwordError = when {
            password.value.isBlank() -> R.string.password_is_empty
            else -> null
        }

        _uiState.update {
            it.copy(
                username = it.username.copy(errorMessageResId = usernameError),
                password = it.password.copy(errorMessageResId = passwordError)
            )
        }

        val isValidationSuccessful = usernameError == null && passwordError == null

        if (!isValidationSuccessful) {
            return
        }

        viewModelScope.launch {
            val result = repository.loginAndSync(
                username.value,
                password.value
            )
            when (result) {
                is LoginResult.Success -> {
                    _loginSuccess.emit(true)
                }
                is LoginResult.UserNotFound -> {
                    _uiState.update {
                        it.copy(
                            username = it.username.copy(errorMessageResId = R.string.user_not_found)
                        )
                    }
                }
                is LoginResult.WrongPassword -> {
                    _uiState.update {
                        it.copy(
                            password = it.password.copy(errorMessageResId = R.string.wrong_password)
                        )
                    }
                }
                is LoginResult.GenericError -> {
                    _uiState.update {
                        it.copy(loginErrorResId = R.string.unexpected_error)
                    }
                }
            }
        }
    }

    fun onSaveUrl() {
        val currentUrl = _uiState.value.url
        if (currentUrl.isNotBlank() && (currentUrl.startsWith("http://") || currentUrl.startsWith("https://"))) {
            urlManager.saveUrl(currentUrl)
            _uiState.update { it.copy(showRestartMessage = true) }
        } else {
            _uiState.update { it.copy(urlErrorResId = R.string.wrong_url) }
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

    fun onDismissRestartMessage() {
        _uiState.update { it.copy(showRestartMessage = false) }
    }

    fun onUrlChange(newUrl: String) {
        _uiState.update { it.copy(url = newUrl, urlError = null) }
    }


    companion object {

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    this[AndroidViewModelFactory.APPLICATION_KEY] as CivitasApplication
                val repository = application.container.repository
                val urlManager = application.container.urlManager
                //val savedStateHandle = this.createSavedStateHandle()
                LoginViewModel(repository, urlManager)
            }
        }
    }
}

