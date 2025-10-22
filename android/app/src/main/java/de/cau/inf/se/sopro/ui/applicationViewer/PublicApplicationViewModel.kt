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
class PublicApplicationViewModel @Inject constructor(
    repository: Repository,
    tokenManager: TokenManager
) : BaseApplicationViewModel(repository, tokenManager) {
    private val _publicApplications = MutableStateFlow<List<Application>>(emptyList())
    override val applications: StateFlow<List<Application>> = _publicApplications


    @RequiresApi(Build.VERSION_CODES.O)
    override fun loadApplications() {
        viewModelScope.launch {
            val userId = tokenManager.getUserId()

            if (userId != null) {
                repository.getPublicApplicationsAsFlow().collect { appsFromDb ->
                    _publicApplications.value = appsFromDb
                }
            }
        }
        loadFormsMap()
    }
}