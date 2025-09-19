package de.cau.inf.se.sopro.ui.yourApplication


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import de.cau.inf.se.sopro.ui.core.ScreenScaffold
import de.cau.inf.se.sopro.ui.utils.AppNavigationType
import de.cau.inf.se.sopro.R
import de.cau.inf.se.sopro.ui.core.createBottomBar
import de.cau.inf.se.sopro.ui.navigation.AppDestination
import de.cau.inf.se.sopro.ui.theme.CivitasAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YourApplicationScreen(
    navigationType: AppNavigationType, // TODO: navigation drawer or navigation rail
    navController: NavHostController,
    modifier: Modifier = Modifier
) {






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
            modifier.padding(innerPadding)
        )
    }
}


@Composable
fun YourApplicationContent(
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        ApplicationCard()
        ApplicationCard()
        ApplicationCard()
        ApplicationCard()
        ApplicationCard()
        ApplicationCard()
        ApplicationCard()
        ApplicationCard()
        ApplicationCard()
        ApplicationCard()
        ApplicationCard()
        ApplicationCard()
        ApplicationCard()
        ApplicationCard()
        ApplicationCard()
        ApplicationCard()
        ApplicationCard()
        ApplicationCard()
        ApplicationCard()
        ApplicationCard()
        ApplicationCard()
        ApplicationCard()
        ApplicationCard()
        ApplicationCard()
        ApplicationCard()
        ApplicationCard()
        ApplicationCard()
        ApplicationCard()
        ApplicationCard()
        ApplicationCard()
        ApplicationCard()
        ApplicationCard()
    }
}



// Preview
@Preview(showBackground = true, name = "YourApplicationScreen")
@Composable
private fun YourApplicationScreenPreview() {
    val navController = rememberNavController()
    CivitasAppTheme {
        YourApplicationScreen(
            navigationType = AppNavigationType.BOTTOM_NAVIGATION,
            navController = navController
        )
    }
}
