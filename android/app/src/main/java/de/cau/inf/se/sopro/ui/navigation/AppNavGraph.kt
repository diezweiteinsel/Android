package de.cau.inf.se.sopro.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import de.cau.inf.se.sopro.R
import de.cau.inf.se.sopro.ui.applicationViewer.ApplicationListScreen
import de.cau.inf.se.sopro.ui.applicationViewer.PublicApplicationViewModel
import de.cau.inf.se.sopro.ui.applicationViewer.YourApplicationViewModel
import de.cau.inf.se.sopro.ui.core.createBottomBar
import de.cau.inf.se.sopro.ui.editApplication.EditApplicationScreen
import de.cau.inf.se.sopro.ui.login.LoginScreen
import de.cau.inf.se.sopro.ui.login.RegistrationScreen
import de.cau.inf.se.sopro.ui.options.OptionsScreen
import de.cau.inf.se.sopro.ui.submitApplication.SubmitApplicationScreen
import de.cau.inf.se.sopro.ui.utils.AppNavigationType
import de.cau.inf.se.sopro.ui.utils.components.CardDisplayMode

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavHost(
    navigationType: AppNavigationType,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {

    NavHost(
        navController = navController,
        startDestination = RootGraph.Login.route
    ) {
        navigation(
            route = RootGraph.YourApplication.route,
            startDestination = RootGraph.YourApplication.startDestination.route
        ) {
            composable(AppDestination.YourApplicationDestination.route) {

                val viewModel: YourApplicationViewModel = hiltViewModel()

                val applications by viewModel.applications.collectAsStateWithLifecycle()
                val formNamesMap by viewModel.formNamesMap.collectAsStateWithLifecycle()
                val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()

                val bottomBar = remember(navigationType, navController) {
                    createBottomBar(
                        navigationType = navigationType,
                        currentTab = AppDestination.YourApplicationDestination,
                        navController = navController
                    )
                }

                ApplicationListScreen(
                    titleRes = R.string.your_application_title,
                    displayMode = CardDisplayMode.StatusColor,
                    showPublicStatusIndicator = true,
                    applications = applications,
                    formNamesMap = formNamesMap,
                    isRefreshing = isRefreshing,
                    onRefresh = viewModel::refreshApplications,
                    onLoad = viewModel::loadApplications,
                    bottomBar = bottomBar,
                    onEditClicked = { applicationId ->
                        navController.navigate(
                            AppDestination.EditApplicationDestination.route
                        )
                    }
                )
            }
        }
        navigation(
            route = RootGraph.SubmitApplication.route,
            startDestination = RootGraph.SubmitApplication.startDestination.route
        ) {
            composable(AppDestination.SubmitApplicationDestination.route) {
                SubmitApplicationScreen(navigationType, navController)
            }
        }
        navigation(
            route = RootGraph.PublicApplication.route,
            startDestination = RootGraph.PublicApplication.startDestination.route
        ) {
            composable(AppDestination.PublicApplicationDestination.route) {
                val viewModel: PublicApplicationViewModel = hiltViewModel()

                val applications by viewModel.applications.collectAsStateWithLifecycle()
                val formNamesMap by viewModel.formNamesMap.collectAsStateWithLifecycle()
                val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()

                val bottomBar = remember(navigationType, navController) {
                    createBottomBar(
                        navigationType = navigationType,
                        currentTab = AppDestination.PublicApplicationDestination,
                        navController = navController
                    )
                }

                ApplicationListScreen(
                    titleRes = R.string.public_application_title,
                    displayMode = CardDisplayMode.Public,
                    applications = applications,
                    formNamesMap = formNamesMap,
                    isRefreshing = isRefreshing,
                    onRefresh = viewModel::refreshApplications,
                    onLoad = viewModel::loadApplications,
                    bottomBar = bottomBar
                )
            }
        }
        navigation(
            route = RootGraph.Login.route,
            startDestination = RootGraph.Login.startDestination.route){
            composable(AppDestination.LoginDestination.route) { LoginScreen(navigationType, navController) }
        }
        navigation(
            route = RootGraph.Registration.route,
            startDestination = RootGraph.Registration.startDestination.route){
            composable(AppDestination.RegistrationDestination.route) { RegistrationScreen(navigationType,navController) }
        }
        navigation(
            route = RootGraph.Options.route,
            startDestination = RootGraph.Options.startDestination.route){
            composable(AppDestination.OptionsDestination.route) { OptionsScreen(navigationType, navController) }
        }
        navigation(
            route = RootGraph.EditApplication.route,
            startDestination = RootGraph.EditApplication.startDestination.route
        ) {
            composable(AppDestination.EditApplicationDestination.route) {
                EditApplicationScreen(navigationType, navController)
            }
        }
    }
}



// Navigation types (adapt this for your own needs)
sealed class RootGraph(
    val route: String,
    val startDestination: AppDestination.NavMenuDestination
) {
    data object YourApplication: RootGraph("your_application_graph", AppDestination.YourApplicationDestination)
    data object SubmitApplication : RootGraph("submit_application_graph", AppDestination.SubmitApplicationDestination)
    data object PublicApplication : RootGraph("public_application_graph", AppDestination.PublicApplicationDestination)
    data object Login : RootGraph("login_graph", AppDestination.LoginDestination)
    data object Registration : RootGraph("registration_graph", AppDestination.RegistrationDestination)
    data object Options : RootGraph("options_graph", AppDestination.OptionsDestination)
    data object EditApplication : RootGraph("edit_application_graph", AppDestination.EditApplicationDestination)
}


sealed class AppDestination(
    val route: String,
    @StringRes val titleRes: Int,
    val arguments: List<NamedNavArgument> = emptyList()
) {
    sealed class NavMenuDestination(route: String, @StringRes titleRes: Int) :
        AppDestination(route, titleRes)

    data object YourApplicationDestination : NavMenuDestination("your_application", R.string.your_application_title)
    data object SubmitApplicationDestination : NavMenuDestination("submit_application", R.string.submit_application_title)
    data object PublicApplicationDestination : NavMenuDestination("public_application", R.string.public_application_title)
    data object LoginDestination : NavMenuDestination("login", R.string.login_title)
    data object RegistrationDestination : NavMenuDestination("registration", R.string.registration_title)
    data object OptionsDestination : NavMenuDestination("options",R.string.options_title)
    data object EditApplicationDestination : NavMenuDestination("edit_application",R.string.edit_application_title)
}




// Utility functions
private fun NavDestination?.isInGraph(graphRoute: String): Boolean =
    this?.hierarchy?.any { it.route == graphRoute } == true

fun AppDestination.NavMenuDestination.toGraphRoute(): String = when (this) {
    AppDestination.YourApplicationDestination -> RootGraph.YourApplication.route
    AppDestination.SubmitApplicationDestination -> RootGraph.SubmitApplication.route
    AppDestination.PublicApplicationDestination -> RootGraph.PublicApplication.route
    AppDestination.LoginDestination -> RootGraph.Login.route
    AppDestination.RegistrationDestination -> RootGraph.Registration.route
    AppDestination.OptionsDestination -> RootGraph.Options.route
    AppDestination.EditApplicationDestination -> RootGraph.EditApplication.route
}

fun NavHostController.navigateTopLevel(dest: AppDestination.NavMenuDestination) =
    navigateTopLevel(dest.toGraphRoute())

fun NavHostController.navigateTopLevel(graphRoute: String) {

    if (currentBackStackEntry?.destination.isInGraph(graphRoute)) return

    navigate(graphRoute) {
        launchSingleTop = true
        restoreState = true
        popUpTo(graph.findStartDestination().id) { saveState = true }
    }
}

