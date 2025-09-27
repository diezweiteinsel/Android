package de.cau.inf.se.sopro.ui.yourApplication


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import de.cau.inf.se.sopro.R
import de.cau.inf.se.sopro.model.application.Application
import de.cau.inf.se.sopro.model.application.Status
import de.cau.inf.se.sopro.ui.core.ScreenScaffold
import de.cau.inf.se.sopro.ui.core.createBottomBar
import de.cau.inf.se.sopro.ui.navigation.AppDestination
import de.cau.inf.se.sopro.ui.theme.CivitasAppTheme
import de.cau.inf.se.sopro.ui.utils.AppNavigationType
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YourApplicationScreen(
    navigationType: AppNavigationType, // TODO: navigation drawer or navigation rail
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: YourApplicationViewModel = viewModel()
) {
    val applications by viewModel.applicantApplications.collectAsStateWithLifecycle()



    // Reuse the same BottomBar instance across recompositions (hence remember) as long as
    // navigationType and navController stay the same. This avoids unnecessary work.
    val bottomBar = remember(navigationType, navController) {
        createBottomBar(navigationType, currentTab = AppDestination.YourApplicationDestination, navController)
    }
    ScreenScaffold(
        titleRes = R.string.your_application_title,
        bottomBar = bottomBar
    ) { innerPadding ->
        YourApplicationContent(
            applications = applications,
            modifier.padding(innerPadding)
        )
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun YourApplicationContent(
    applications: List<Application>,
    modifier: Modifier = Modifier
) {

    if (applications.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Keine Anträge gefunden.")
        }
        return
    }

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(applications) { application ->
            ApplicationCard(application = application)
        }
    }
}



// Preview
@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, name = "YourApplicationContent Preview")
@Composable
private fun YourApplicationContentPreview() {

    val fakeApplications = listOf(
        Application(id = 1, applicantId = 1, applicantName = "Max Mustermann", formId = 69, createdAt = LocalDateTime.now(), status = Status.PENDING, isPublic = false, isEdited = false, dynamicAttributes = mapOf("Grund" to "Antrag auf Sonderurlaub")),
        Application(id = 2, applicantId = 1, applicantName = "Max Mustermann", formId = 161, createdAt = LocalDateTime.now(), status = Status.APPROVED, isPublic = false, isEdited = false)
    )

    CivitasAppTheme {
        YourApplicationContent(applications = fakeApplications)
    }
}