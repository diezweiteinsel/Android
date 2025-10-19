package de.cau.inf.se.sopro.ui.applicationViewer

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.cau.inf.se.sopro.data.Repository
import de.cau.inf.se.sopro.data.TokenManager
import de.cau.inf.se.sopro.model.application.Application
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class YourApplicationViewModel @Inject constructor(
    repository: Repository,
    tokenManager: TokenManager
) : BaseApplicationViewModel(repository, tokenManager) {
    private val _applications = MutableStateFlow<List<Application>>(emptyList())
    override val applications: StateFlow<List<Application>> = _applications

    @RequiresApi(Build.VERSION_CODES.O)
    override fun loadApplications() {
        viewModelScope.launch {
            val userId = tokenManager.getUserId()

            if (userId != null) {
                repository.getApplicationsAsFlow(userId).collect { appsFromDb ->
                    _applications.value = appsFromDb
                }
            }
        }
        loadFormsMap()
    }
}