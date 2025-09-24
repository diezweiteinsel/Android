package de.cau.inf.se.sopro.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import de.cau.inf.se.sopro.R
import de.cau.inf.se.sopro.data.ApplicantRepository
import de.cau.inf.se.sopro.model.applicant.Applicant
import de.cau.inf.se.sopro.model.applicant.Usertype
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class RegistrationViewModel(private val applicantRepository: ApplicantRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(RegistrationUiState())
    val uiState: StateFlow<RegistrationUiState> = _uiState.asStateFlow()

    fun onUsernameChanged(username: String) {
        _uiState.update { currentState ->
            currentState.copy(
                username = currentState.username.copy(value = username, errorMessageResId = null)
            )
        }
    }

    fun onPasswordChanged(password: String) {
        _uiState.update { currentState ->
            currentState.copy(
                password = currentState.password.copy(value = password, errorMessageResId = null)
            )
        }
    }

    fun onConfirmPasswordChanged(confirmPassword: String) {
        _uiState.update { currentState ->
            currentState.copy(
                confirmPassword = currentState.confirmPassword.copy(value = confirmPassword, errorMessageResId = null)
            )
        }
    }

    fun validateAndRegister(): Boolean {
        val username = _uiState.value.username
        val password = _uiState.value.password
        val confirmPassword = _uiState.value.confirmPassword

        val usernameError = when {
            (username.value.length < 4) -> R.string.username_too_short
            else -> null
        }

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
                confirmPassword = it.confirmPassword.copy(errorMessageResId = confirmPasswordError)
            )
        }

        val isValidationSuccessful = usernameError == null && passwordError == null && confirmPasswordError == null

        if (isValidationSuccessful) {

            val newApplicant = Applicant(
                userid = 1,
                username = _uiState.value.username.value,
                password = _uiState.value.password.value,
                usertype = Usertype.APPLICANT
            )
            applicantRepository.saveApplicant(newApplicant)
        }

        return isValidationSuccessful
    }


    companion object {
        fun Factory(applicantRepository: ApplicantRepository): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(RegistrationViewModel::class.java)) {
                        return RegistrationViewModel(applicantRepository) as T
                    }

                    throw IllegalArgumentException("Unknown ViewModel class")

                }
            }
        }
    }
}