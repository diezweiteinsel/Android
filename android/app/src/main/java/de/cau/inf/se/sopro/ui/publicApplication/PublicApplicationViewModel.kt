package de.cau.inf.se.sopro.ui.publicApplication

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.cau.inf.se.sopro.data.Repository
import de.cau.inf.se.sopro.data.TokenManager
import de.cau.inf.se.sopro.model.application.Application
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
class PublicApplicationViewModel(
    private val repository: Repository,
    private val tokenManager: TokenManager
) : ViewModel() {
    private val _publicApplications = MutableStateFlow<List<Application>>(emptyList())
    val publicApplications: StateFlow<List<Application>> = _publicApplications

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

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
    fun loadPublicApplications() {

        viewModelScope.launch {
            viewModelScope.launch {
                val userId = tokenManager.getUserId()

                if (userId != null) {
                    repository.getPublicApplicationsAsFlow().collect { appsFromDb ->
                        _publicApplications.value = appsFromDb
                    }
                }
            }
        }
    }
}