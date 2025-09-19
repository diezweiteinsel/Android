package de.cau.inf.se.sopro

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import de.cau.inf.se.sopro.ui.navigation.AppDestination
import de.cau.inf.se.sopro.ui.navigation.AppNavHost
import de.cau.inf.se.sopro.ui.navigation.navigateTopLevel
import de.cau.inf.se.sopro.ui.theme.CivitasAppTheme
import de.cau.inf.se.sopro.ui.utils.AppNavigationType

@Composable
fun CivitasApp(
    windowSize: WindowWidthSizeClass,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
    ) {

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination

    val selectedRoute: String = currentDestination
        ?.hierarchy
        ?.firstOrNull { it.route != null }
        ?.route
        ?: AppDestination.YourApplicationDestination.route


    val navigationType = when(windowSize) {
        WindowWidthSizeClass.Compact -> {
            AppNavigationType.BOTTOM_NAVIGATION
        }
        WindowWidthSizeClass.Medium -> {
            AppNavigationType.NAVIGATION_RAIL
        }
        WindowWidthSizeClass.Expanded -> {
            AppNavigationType.PERMANENT_NAVIGATION_DRAWER
        }
        else -> {
            AppNavigationType.BOTTOM_NAVIGATION
        }
    }


    when(navigationType) {
        AppNavigationType.PERMANENT_NAVIGATION_DRAWER -> {
            PermanentNavigationDrawer(
                drawerContent = {
                    PermanentDrawerSheet(
                        modifier = Modifier.width(dimensionResource(R.dimen.drawer_width)),
                        drawerContainerColor = MaterialTheme.colorScheme.inverseOnSurface
                    ) {
                        NavigationDrawerContent(
                            selectedRoute = selectedRoute,
                            onTabPressed = {
                                    dest -> navController.navigateTopLevel(dest)
                            },
                            modifier = Modifier
                                .wrapContentWidth()
                                .fillMaxHeight()
                                .background(MaterialTheme.colorScheme.inverseOnSurface)
                                .padding(dimensionResource(R.dimen.drawer_padding_content))
                        )
                    }
                },
            ) {
                AppNavHost(navigationType, navController)
            }
        }

        AppNavigationType.NAVIGATION_RAIL -> {
            // TODO
        }

        else -> {
            AppNavHost(navigationType, navController)
        }

    }
}

/**
 * Top app bar to display title and conditionally display the back navigation.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CivitasTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    onNavigateBack: (() -> Unit)? = null
) {
    CenterAlignedTopAppBar(
        title = { Text(title) },
        modifier = modifier,
        scrollBehavior = scrollBehavior

    )
}

/**
 * Navigation drawer content for big sized screens to display the navigation options
 */

@Composable
fun NavigationDrawerContent(
    selectedRoute: String,
    onTabPressed: (AppDestination.NavMenuDestination) -> Unit,
    modifier: Modifier = Modifier,
    navigationItemContentList: List<NavigationItemContent> = drawerItems
) {

    Column(modifier = modifier) {
        for (navItem in navigationItemContentList) {
            val isSelected = selectedRoute == navItem.destinationType.route
            NavigationDrawerItem(
                selected = isSelected,
                label = {
                    Text(
                        text = stringResource(navItem.labelRes),
                        modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.drawer_padding_header))
                    )
                },
                icon = {
                    Icon(
                        imageVector = navItem.icon,
                        contentDescription = stringResource(navItem.labelRes)
                    )
                },
                colors = NavigationDrawerItemDefaults.colors(
                    unselectedContainerColor = Color.Transparent
                ),
                onClick = { onTabPressed(navItem.destinationType) }
            )
        }
    }
}

/**
 * Navigation rail for medium sized screens to display the navigation options
 */
@Composable
private fun CivitasNavigationRail(
    currentTab: AppDestination.NavMenuDestination,
    onTabPressed: ((AppDestination.NavMenuDestination) -> Unit),
    modifier: Modifier = Modifier,
    navigationItemContentList: List<NavigationItemContent> = drawerItems
) {
    NavigationRail(modifier = modifier) {
        for (navItem in navigationItemContentList) {
            NavigationRailItem(
                selected = currentTab == navItem.destinationType,
                onClick = { onTabPressed(navItem.destinationType) },
                icon = {
                    Icon(
                        imageVector = navItem.icon,
                        contentDescription = stringResource(navItem.labelRes)
                    )
                }
            )
        }
    }
}


/**
 * Navigation bottom bar for small sized screens (normally smartphones) to display the navigation options
 */
@Composable
fun CivitasBottomNavigationBar(
    currentTab: AppDestination.NavMenuDestination,
    onTabPressed: ((AppDestination.NavMenuDestination) -> Unit),
    modifier: Modifier = Modifier,
    navigationItemContentList: List<NavigationItemContent> = drawerItems
) {
    NavigationBar(modifier = modifier) {
        for (navItem in navigationItemContentList) {
            NavigationBarItem(
                selected = currentTab == navItem.destinationType,
                onClick = { onTabPressed(navItem.destinationType) },
                icon = {
                    Icon(
                        imageVector = navItem.icon,
                        contentDescription = stringResource(navItem.labelRes)
                    )
                }
            )
        }
    }
}


data class NavigationItemContent(
    val destinationType: AppDestination.NavMenuDestination,
    val icon: ImageVector,
    @androidx.annotation.StringRes val labelRes: Int
)

private val drawerItems = listOf(
    NavigationItemContent(AppDestination.YourApplicationDestination,  Icons.Default.MailOutline,    R.string.your_application_title),
    NavigationItemContent(AppDestination.SubmitApplicationDestination,  Icons.Default.Edit,    R.string.submit_application_title),
    NavigationItemContent(AppDestination.PublicApplicationDestination, Icons.Default.LocationOn, R.string.public_application_title),
    NavigationItemContent(AppDestination.LoginDestination, Icons.Default.Lock, R.string.login_title)
)



// Previews
@Preview(
    name = "Permanent Navigation Drawer",
    showBackground = true,
    widthDp = 1200, heightDp = 800
)
@Composable
fun CivitasAppPermanentDrawerPreview() {
    CivitasAppTheme {
        CivitasApp(windowSize = WindowWidthSizeClass.Expanded)
    }
}

@Preview(
    name = "Bottom Navigation Bar",
    showBackground = true,
    widthDp = 360,
    heightDp = 640
)
@Composable
fun CivitasAppBottomNavigationBarPreview() {
    CivitasAppTheme {
        CivitasApp(windowSize = WindowWidthSizeClass.Compact)
    }
}
