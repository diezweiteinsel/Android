package de.cau.inf.se.sopro.ui.yourApplication

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import de.cau.inf.se.sopro.data.Repository
import de.cau.inf.se.sopro.data.TokenManager
import de.cau.inf.se.sopro.model.application.Application
import de.cau.inf.se.sopro.ui.login.LoginViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
class YourApplicationViewModel(
    private val repository: Repository,
    tokenManager: TokenManager? = null
) : ViewModel() {
    private val _applications = MutableStateFlow<List<Application>>(emptyList())

    val applications: StateFlow<List<Application>> = _applications

    val userId = tokenManager?.getUserId()

    init {
        loadApplications(userId = userId)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadApplications(
        userId: Int?
    ) {

        viewModelScope.launch {
            val fetchedApplications = repository.getApplications(
                userId = userId
            )
            _applications.value = fetchedApplications
        }
    }
}

class ViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(YourApplicationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return YourApplicationViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(repository) as T
        }
        // Füge hier weitere ViewModels hinzu, falls nötig
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}