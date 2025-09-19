package de.cau.inf.se.sopro.ui.publicApplication


import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import de.cau.inf.se.sopro.R
import de.cau.inf.se.sopro.ui.core.ScreenScaffold
import de.cau.inf.se.sopro.ui.core.createBottomBar
import de.cau.inf.se.sopro.ui.navigation.AppDestination
import de.cau.inf.se.sopro.ui.theme.CivitasAppTheme
import de.cau.inf.se.sopro.ui.utils.AppNavigationType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PublicApplicationScreen(
    navigationType: AppNavigationType, // TODO: navigation drawer or navigation rail
    navController: NavHostController,
    modifier: Modifier = Modifier
) {



    // Reuse the same BottomBar instance across recompositions (hence remember) as long as
    // navigationType and navController stay the same. This avoids unnecessary work.
    val bottomBar = remember(navigationType, navController) {
        createBottomBar(navigationType, currentTab = AppDestination.PublicApplicationDestination, navController)
    }
    ScreenScaffold(
        titleRes = R.string.public_application_title,
        bottomBar = bottomBar
    ) { innerPadding ->
        PublicApplicationContent(
            modifier.padding(innerPadding)
        )
    }
}


@Composable
fun PublicApplicationContent(
    modifier: Modifier = Modifier
) {
    Text(
        text = stringResource(R.string.your_application_msg),
        modifier = modifier
    )
}


// Preview
@Preview(showBackground = true, name = "PublicApplicationScreen")
@Composable
private fun PublicApplicationScreenPreview() {
    val navController = rememberNavController()
    CivitasAppTheme {
        PublicApplicationScreen(
            navigationType = AppNavigationType.BOTTOM_NAVIGATION,
            navController = navController
        )
    }
}
