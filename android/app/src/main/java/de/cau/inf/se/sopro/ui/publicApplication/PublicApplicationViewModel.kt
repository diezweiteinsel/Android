package de.cau.inf.se.sopro.ui.publicApplication

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.cau.inf.se.sopro.model.application.Application
import de.cau.inf.se.sopro.model.application.Status
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
class PublicApplicationViewModel : ViewModel() {
    private val _applications = MutableStateFlow<List<Application>>(emptyList())

    val applications: StateFlow<List<Application>> = _applications

    init {
        loadApplications()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadApplications() {

        viewModelScope.launch {
            _applications.value = listOf(
                Application(1, 4, 1, 1, Status.APPROVED, LocalDateTime.now(), 2, 2, false),
                Application(2, 6, 2, 2, Status.APPROVED, LocalDateTime.now().minusDays(3), 1, 1, false),
                Application(3, 9, 3, 3, Status.APPROVED, LocalDateTime.now().minusWeeks(2), 1, 1, true)
            )
        }
    }
}