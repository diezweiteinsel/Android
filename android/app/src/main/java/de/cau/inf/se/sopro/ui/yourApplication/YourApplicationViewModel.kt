package de.cau.inf.se.sopro.ui.yourApplication

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import de.cau.inf.se.sopro.data.Repository
import de.cau.inf.se.sopro.data.TokenManager
import de.cau.inf.se.sopro.di.UrlManager
import de.cau.inf.se.sopro.model.application.Application
import de.cau.inf.se.sopro.ui.login.LoginViewModel
import de.cau.inf.se.sopro.ui.options.OptionsViewModel
import de.cau.inf.se.sopro.ui.publicApplication.PublicApplicationViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
class YourApplicationViewModel(
    private val repository: Repository,
    private val tokenManager: TokenManager
) : ViewModel() {
    private val _applications = MutableStateFlow<List<Application>>(emptyList())
    val applications: StateFlow<List<Application>> = _applications

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    val userId = tokenManager.getUserId()

    fun refreshApplications() {
        viewModelScope.launch {
            try {
                _isRefreshing.value = true
                repository.refreshApplications()
            } finally {
                _isRefreshing.value = false
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadApplications() {
        viewModelScope.launch {
            val userId = tokenManager.getUserId()

            if (userId != null) {
                repository.getApplicationsAsFlow(userId).collect { appsFromDb ->
                    _applications.value = appsFromDb
                }
            }
        }
    }
}

class ViewModelFactory(
    private val repository: Repository,
    private val tokenManager: TokenManager,
    private val urlManager: UrlManager
) : ViewModelProvider.Factory {
    @RequiresApi(Build.VERSION_CODES.O)

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(YourApplicationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return YourApplicationViewModel(repository, tokenManager) as T
        }

        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(repository, urlManager) as T
        }

        if (modelClass.isAssignableFrom(OptionsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return OptionsViewModel(repository, urlManager) as T
        }

        if (modelClass.isAssignableFrom(PublicApplicationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PublicApplicationViewModel(repository, tokenManager) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}