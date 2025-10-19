package de.cau.inf.se.sopro.ui.applicationViewer

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import de.cau.inf.se.sopro.R
import de.cau.inf.se.sopro.model.application.Application
import de.cau.inf.se.sopro.ui.core.BottomBarSpec
import de.cau.inf.se.sopro.ui.core.ScreenScaffold
import de.cau.inf.se.sopro.ui.core.createBottomBar
import de.cau.inf.se.sopro.ui.navigation.AppDestination
import de.cau.inf.se.sopro.ui.utils.AppNavigationType
import de.cau.inf.se.sopro.ui.utils.components.ApplicationCard
import de.cau.inf.se.sopro.ui.utils.components.CardDisplayMode

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun ApplicationListScreen(
    @StringRes titleRes: Int,
    bottomBar: BottomBarSpec,
    displayMode: CardDisplayMode,
    showPublicStatusIndicator: Boolean = false,
    applications: List<Application>,
    formNamesMap: Map<Int, String>,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    onLoad: () -> Unit,
    modifier: Modifier = Modifier
) {
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = onRefresh
    )

    LaunchedEffect(Unit) {
        onLoad()
    }

    ScreenScaffold(
        titleRes = titleRes,
        bottomBar = bottomBar
    ) { innerPadding ->
        Box(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
                .pullRefresh(pullRefreshState)
        ) {
            ApplicationListContent(
                applications = applications,
                formNamesMap = formNamesMap,
                displayMode = displayMode,
                showPublicStatusIndicator = showPublicStatusIndicator,
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
fun ApplicationListContent(
    applications: List<Application>,
    formNamesMap: Map<Int, String>,
    modifier: Modifier = Modifier,
    displayMode: CardDisplayMode,
    showPublicStatusIndicator: Boolean
) {
    LazyColumn(modifier = modifier) {
        items(
            items = applications,
            key = { application -> "${application.id}-${application.formId}" }
        ) { application ->
            val formName = formNamesMap[application.formId] ?: stringResource(id = R.string.unknown_form)

            ApplicationCard(
                application = application,
                formName = formName,
                displayMode = displayMode,
                showPublicStatusIndicator = showPublicStatusIndicator
            )
        }
    }
}