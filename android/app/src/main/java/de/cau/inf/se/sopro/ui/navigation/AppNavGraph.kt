package de.cau.inf.se.sopro.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import de.cau.inf.se.sopro.R

import de.cau.inf.se.sopro.ui.login.LoginScreen
import de.cau.inf.se.sopro.ui.login.RegistrationScreen
import de.cau.inf.se.sopro.ui.options.OptionsScreen
import de.cau.inf.se.sopro.ui.applicationViewer.PublicApplicationScreen
import de.cau.inf.se.sopro.ui.submitApplication.SubmitApplicationScreen
import de.cau.inf.se.sopro.ui.utils.AppNavigationType
import de.cau.inf.se.sopro.ui.applicationViewer.YourApplicationScreen


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

                YourApplicationScreen(
                    navigationType = navigationType,
                    navController = navController
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
                PublicApplicationScreen(navigationType, navController)
            }
        }
        navigation(
            route = RootGraph.Login.route,
            startDestination = RootGraph.Login.startDestination.route
        ) {
            composable(AppDestination.LoginDestination.route) { LoginScreen(navigationType, navController) }
            }
        navigation(route = RootGraph.Registration.route,
            startDestination = RootGraph.Registration.startDestination.route){
            composable(AppDestination.RegistrationDestination.route) { RegistrationScreen(navigationType,navController) }
        }
        navigation(route = RootGraph.Options.route,
            startDestination = RootGraph.Options.startDestination.route){
            composable(AppDestination.OptionsDestination.route) { OptionsScreen(navigationType, navController) }
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

