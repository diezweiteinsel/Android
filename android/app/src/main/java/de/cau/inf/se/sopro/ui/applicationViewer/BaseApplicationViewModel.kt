package de.cau.inf.se.sopro.ui.applicationViewer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.cau.inf.se.sopro.data.Repository
import de.cau.inf.se.sopro.data.TokenManager
import de.cau.inf.se.sopro.model.application.Application
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

abstract class BaseApplicationViewModel(
    protected val repository: Repository,
    protected val tokenManager: TokenManager
) : ViewModel() {

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

    protected fun loadFormsMap() {
        viewModelScope.launch {
            val allForms = repository.getForms()
            if (allForms != null) {
                val formMap = allForms.filter { it.id != null && it.formName != null }
                    .associateBy({ it.id!! }, { it.formName!! })
                _formNamesMap.value = formMap
            }
        }
    }

    abstract val applications: StateFlow<List<Application>>
    abstract fun loadApplications()
}