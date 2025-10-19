package de.cau.inf.se.sopro.ui.publicApplication


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import de.cau.inf.se.sopro.R
import de.cau.inf.se.sopro.model.application.Application
import de.cau.inf.se.sopro.ui.core.ScreenScaffold
import de.cau.inf.se.sopro.ui.core.createBottomBar
import de.cau.inf.se.sopro.ui.navigation.AppDestination
import de.cau.inf.se.sopro.ui.utils.AppNavigationType
import de.cau.inf.se.sopro.ui.yourApplication.CardDisplayMode

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun PublicApplicationScreen(
    navigationType: AppNavigationType,
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: PublicApplicationViewModel = viewModel()
) {
    val applicationsState by viewModel.publicApplications.collectAsStateWithLifecycle()
    val formNamesMapState by viewModel.formNamesMap.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()

    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = { viewModel.refreshApplications() }
    )

    // Reuse the same BottomBar instance across recompositions (hence remember) as long as
    // navigationType and navController stay the same. This avoids unnecessary work.
    val bottomBar = remember(navigationType, navController) {
        createBottomBar(navigationType, currentTab = AppDestination.PublicApplicationDestination, navController)
    }

    LaunchedEffect(Unit) {
        viewModel.loadPublicApplications()
    }

    ScreenScaffold(
        titleRes = R.string.public_application_title,
        bottomBar = bottomBar
    ) { innerPadding ->
        Box(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
                .pullRefresh(pullRefreshState)
        ) {
            PublicApplicationContent(
                applications = applicationsState,
                formNamesMap = formNamesMapState,
                modifier = Modifier.fillMaxSize()
            )

            PullRefreshIndicator(
                refreshing = isRefreshing,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PublicApplicationContent(
    applications: List<Application>,
    formNamesMap: Map<Int, String>,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(
            items = applications,
            key = { application -> "${application.id}-${application.formId}" }
        ) { application ->
            val formName = formNamesMap[application.formId] ?: stringResource(id = R.string.unknown_form)

            PublicApplicationCard(
                application = application,
                formName = formName,
                CardDisplayMode.Public
            )
        }
    }
}
