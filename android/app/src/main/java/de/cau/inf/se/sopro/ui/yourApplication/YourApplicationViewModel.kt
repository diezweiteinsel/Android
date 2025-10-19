package de.cau.inf.se.sopro.ui.yourApplication

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.cau.inf.se.sopro.data.Repository
import de.cau.inf.se.sopro.data.TokenManager
import de.cau.inf.se.sopro.di.UrlManager
import de.cau.inf.se.sopro.model.application.Application
import de.cau.inf.se.sopro.ui.login.LoginViewModel
import de.cau.inf.se.sopro.ui.options.OptionsViewModel
import de.cau.inf.se.sopro.ui.publicApplication.PublicApplicationViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class YourApplicationViewModel @Inject constructor(
    private val repository: Repository,
    private val tokenManager: TokenManager
) : ViewModel() {
    private val _applications = MutableStateFlow<List<Application>>(emptyList())
    val applications: StateFlow<List<Application>> = _applications

    private val _formNamesMap = MutableStateFlow<Map<Int, String>>(emptyMap())
    val formNamesMap: StateFlow<Map<Int, String>> = _formNamesMap

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    val userId = tokenManager.getUserId()

    fun refreshApplications() {
        viewModelScope.launch {
            try {
                _isRefreshing.value = true
                repository.refreshApplicationsAndForms()
                loadFormsMap()
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
        loadFormsMap()
    }

    private fun loadFormsMap() {
        viewModelScope.launch {
            val allForms = repository.getForms()
            if (allForms != null) {
                val formMap = allForms.filter { it.id != null && it.formName != null }
                    .associateBy({ it.id!! }, { it.formName!! })
                _formNamesMap.value = formMap
            }
        }
    }
}