package de.cau.inf.se.sopro.ui.core

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import de.cau.inf.se.sopro.CivitasBottomNavigationBar
import de.cau.inf.se.sopro.CivitasTopAppBar
import de.cau.inf.se.sopro.ui.navigation.AppDestination
import de.cau.inf.se.sopro.ui.navigation.navigateTopLevel
import de.cau.inf.se.sopro.ui.utils.AppNavigationType

// This composable will act as a wrapper around our content composable to give each
// screen the same navigation elements
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenScaffold(
    @StringRes titleRes: Int,
    modifier: Modifier = Modifier,
    showTopBar: Boolean = true,
    bottomBar: BottomBarSpec = BottomBarSpec.Hidden,
    onNavigateBack: (() -> Unit)? = null,
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
    //snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    floatingActionButton: (@Composable () -> Unit) = { },
    content: @Composable (PaddingValues) -> Unit // Compose convention: content parameter as
                                                 // last parameter for trailing lambda
) {

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            if (showTopBar) {
                CivitasTopAppBar(
                    title = stringResource(titleRes),
                    onNavigateBack = onNavigateBack,
                    scrollBehavior = scrollBehavior
                )
            }
        },
        bottomBar = {
            if (bottomBar is BottomBarSpec.Visible) {
                CivitasBottomNavigationBar(
                    currentTab = bottomBar.currentTab,
                    onTabPressed = bottomBar.onTabPressed
                )
            }
        },
        //snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = floatingActionButton
    ) { innerPadding ->
        content(innerPadding) // Screen-Content gets padding from Scaffold
    }
}

/*
* A visible bottom bar must always provide both the active tab and its action.
* Choose [Hidden] to omit the bar, or [Visible] to render it with the required configuration.
*/
@Immutable
sealed interface BottomBarSpec {
    data object Hidden : BottomBarSpec
    data class Visible(
        val currentTab: AppDestination.NavMenuDestination,
        val onTabPressed: (AppDestination.NavMenuDestination) -> Unit
    ) : BottomBarSpec
}


fun createBottomBar(
    navigationType: AppNavigationType,
    currentTab: AppDestination.NavMenuDestination,
    navController: NavHostController
): BottomBarSpec {
    if (navigationType != AppNavigationType.BOTTOM_NAVIGATION) return BottomBarSpec.Hidden

    return BottomBarSpec.Visible(
        currentTab = currentTab,
        onTabPressed = { dest -> navController.navigateTopLevel(dest)
        }
    )
}

