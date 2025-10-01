package de.cau.inf.se.sopro.ui.login

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
import de.cau.inf.se.sopro.ui.navigation.AppDestination
import de.cau.inf.se.sopro.ui.navigation.navigateTopLevel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class LoginViewModel(private val repository: Repository) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState()) //unser uistate
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _loginSuccess = MutableSharedFlow<Boolean>() //sharedflow for login success
    val loginSuccess: SharedFlow<Boolean> = _loginSuccess

    var username by mutableStateOf("") //setting username and password as mutableStates, so the user can change them
    var password by mutableStateOf("")

    fun checkHealth() {

        fun onUsernameChange(username: String) { //
            _uiState.update { currentState ->
                currentState.copy(
                    username = currentState.username.copy(
                        value = username,
                        errorMessageResId = null
                    ),
                )
            }
        }

        fun onPasswordChange(password: String) {
            _uiState.update { currentState ->
                currentState.copy(
                    password = currentState.password.copy( //we update the uistate with the currentstate of the password
                        value = password,
                        errorMessageResId = null
                    ),
                )
            }
        }

        fun onLoginClick() {
            val username = _uiState.value.username
            val password = _uiState.value.password

            val usernameError = when { //error for when the field username is empty
                username.value.isBlank() -> R.string.username_is_empty
                else -> null
            }

            val passwordError = when { //error for when the field password is empty
                password.value.isBlank() -> R.string.password_is_empty
                else -> null
            }

            _uiState.update { //we update our ui state
                it.copy(
                    username = it.username.copy(errorMessageResId = usernameError),
                    password = it.password.copy(errorMessageResId = passwordError)
                )
            }

            val isValidationSuccessful = usernameError == null && passwordError == null

            if (!isValidationSuccessful) {
                return //if the validation is not successful, we return nothing
            }

            viewModelScope.launch { //because we are not in a suspend function we have to use this
                val result = repository.authenticateLogin(  //calling the authenticateLogin function from our repository
                    username.value,
                    password.value
                )
                when (result) { //result checking
                    is LoginResult.Success -> {
                        _loginSuccess.emit(true) //our loginvariable is set to true
                    }

                    is LoginResult.UserNotFound -> { //throw errors
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

                    is LoginResult.GenericError -> { //something went wrong in general
                        _uiState.update {
                            it.copy(loginError = "An unexpected error occurred.")
                        }
                    }
                }
            }
        }
    }

        companion object { //we create a companion object to be able to use our viewmodel in the LoginScreen file

            val Factory: ViewModelProvider.Factory = viewModelFactory {
                initializer {
                    val application =
                        this[AndroidViewModelFactory.APPLICATION_KEY] as CivitasApplication
                    val repository = application.container.repository //this is our repository
                    LoginViewModel(repository) //defining our viewmodel
                }
            }
        }
    }

