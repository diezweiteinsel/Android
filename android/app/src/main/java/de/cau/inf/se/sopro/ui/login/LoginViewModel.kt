package de.cau.inf.se.sopro.ui.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavHostController
import de.cau.inf.se.sopro.CivitasApplication
import de.cau.inf.se.sopro.data.Repository
import de.cau.inf.se.sopro.ui.navigation.AppDestination
import de.cau.inf.se.sopro.ui.navigation.navigateTopLevel
import de.cau.inf.se.sopro.ui.utils.AppNavigationType
import kotlinx.coroutines.launch


class LoginViewModel(private val repository: Repository) : ViewModel() {
    var username by mutableStateOf("")
    var password by mutableStateOf("")

    //var loginResult by mutableStateOf<LoginResponse?>(null)
    var error by mutableStateOf<String?>(null)
    fun onUsernameChange(newUsername: String) {
        username = newUsername
    }

    fun onPasswordChange(newPassword: String) {
        password = newPassword
    }

    fun login(navController: NavHostController) {
        viewModelScope.launch {
            if(repository.authenticateLogin(username, password)){
                navController.navigateTopLevel(AppDestination.YourApplicationDestination)
        }else{
                error = "Login failed"
            }
        }
    }


    companion object {

        val Factory = viewModelFactory {
            initializer {
                val application =
                    this[AndroidViewModelFactory.APPLICATION_KEY] as CivitasApplication
                val repository = application.container.repository
                //val savedStateHandle = this.createSavedStateHandle()
                LoginViewModel(repository)
            }
        }
    }
}
