package de.cau.inf.se.sopro.ui.login

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import de.cau.inf.se.sopro.CivitasApplication
import de.cau.inf.se.sopro.R
import de.cau.inf.se.sopro.data.Repository
import de.cau.inf.se.sopro.model.applicant.Usertype
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegistrationViewModel(private val repository: Repository) : ViewModel() {
    private val _uiState = MutableStateFlow(RegistrationUiState())
    val uiState: StateFlow<RegistrationUiState> = _uiState.asStateFlow()

    private val _registrationSuccess = MutableSharedFlow<Boolean>()
    val registrationSuccess: SharedFlow<Boolean> = _registrationSuccess

    fun onUsernameChanged(username: String) {
        _uiState.update { currentState ->
            currentState.copy(
                username = currentState.username.copy(value = username, errorMessageResId = null),
            )
        }
    }

    fun onPasswordChanged(password: String) {
        _uiState.update { currentState ->
            currentState.copy(
                password = currentState.password.copy(value = password, errorMessageResId = null),
            )
        }
    }

    fun onConfirmPasswordChanged(confirmPassword: String) {
        _uiState.update { currentState ->
            currentState.copy(
                confirmPassword = currentState.confirmPassword.copy(value = confirmPassword, errorMessageResId = null),
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onRegisterClick() {
        val username = _uiState.value.username
        val password = _uiState.value.password
        val confirmPassword = _uiState.value.confirmPassword

        val usernameError = when {
            (username.value.length < 4) -> R.string.username_too_short
            else -> null
        }

        Log.d("nachts", password.value)
        val passwordError = when {
            (password.value.length < 6) -> R.string.password_too_short
            (password.value.isBlank()) -> R.string.password_is_empty
            else -> null
        }

        val confirmPasswordError = when {
            (password.value != confirmPassword.value) -> R.string.passwords_do_not_match
            else -> null
        }

        _uiState.update {
            it.copy(
                username = it.username.copy(errorMessageResId = usernameError),
                password = it.password.copy(errorMessageResId = passwordError),
                confirmPassword = it.confirmPassword.copy(errorMessageResId = confirmPasswordError),
            )
        }

        val isValidationSuccessful = usernameError == null && passwordError == null && confirmPasswordError == null

        if (!isValidationSuccessful) {
            return
        }

        viewModelScope.launch {
            val success = repository.createApplicant(
                username = username.value,
                email = "random@place.com",
                password = password.value,
                role = Usertype.APPLICANT
            )

            if (success) {
                _registrationSuccess.emit(true)
            } else {
                _uiState.update {
                    it.copy(
                        registrationError = "Registration failed."

                    )

                }
                Log.d("nachts", "Registration failed.")
            }


        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as CivitasApplication
                val repository = application.container.repository

                RegistrationViewModel(repository)
            }
        }
    }
}