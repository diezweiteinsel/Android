package de.cau.inf.se.sopro.ui.yourApplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.cau.inf.se.sopro.model.application.Application
import de.cau.inf.se.sopro.persistence.dao.ApplicationDao
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class YourApplicationViewModel(private val applicationDao: ApplicationDao): ViewModel() {
    val applicantApplications: StateFlow<List<Application>> =
        applicationDao.getApplicantApplications(applicantId = 123)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000L),
                initialValue = emptyList()
            )
}