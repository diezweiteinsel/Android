package de.cau.inf.se.sopro.ui.publicApplication

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
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
    private val repository: Repository,
    private val tokenManager: TokenManager
) : ViewModel() {
    private val _publicApplications = MutableStateFlow<List<Application>>(emptyList())
    val publicApplications: StateFlow<List<Application>> = _publicApplications

    private val _formNamesMap = MutableStateFlow<Map<Int, String>>(emptyMap())
    val formNamesMap: StateFlow<Map<Int, String>> = _formNamesMap

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

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
    fun loadPublicApplications() {
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