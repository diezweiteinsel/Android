package de.cau.inf.se.sopro.ui.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import de.cau.inf.se.sopro.CivitasApplication
import de.cau.inf.se.sopro.data.Repository
import kotlinx.coroutines.launch


class LoginViewModel(private val repository: Repository) : ViewModel(){
    var username by mutableStateOf("")
    var password by mutableStateOf("")
    //var loginResult by mutableStateOf<LoginResponse?>(null)
    var error by mutableStateOf<String?>(null)
    fun onUsernameChange(new_username: String){ username = new_username }
    fun onPasswordChange(new_password: String){ password = new_password }

    fun Login(){
        viewModelScope.launch { repository.authenticateLogin(username,password) }
    }



companion object {

    val Factory = viewModelFactory {
        initializer {
            val application = this[AndroidViewModelFactory.APPLICATION_KEY] as CivitasApplication
            val repository = application.container.repository
            //val savedStateHandle = this.createSavedStateHandle()
            LoginViewModel(repository)
        }
    }
}
}


