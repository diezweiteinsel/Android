package de.cau.inf.se.sopro.ui.submitApplication


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
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
fun SubmitApplicationScreen(
    navigationType: AppNavigationType, // TODO: navigation drawer or navigation rail
    navController: NavHostController,
    modifier: Modifier = Modifier
) {



    // Reuse the same BottomBar instance across recompositions (hence remember) as long as
    // navigationType and navController stay the same. This avoids unnecessary work.
    val bottomBar = remember(navigationType, navController) {
        createBottomBar(navigationType, currentTab = AppDestination.SubmitApplicationDestination, navController)
    }
    ScreenScaffold(
        titleRes = R.string.submit_application_title,
        bottomBar = bottomBar
    ) { innerPadding ->
        SubmitApplicationContent(
            modifier.padding(innerPadding)
        )
    }
}


@Composable
fun SubmitApplicationContent(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {
        AutoCompleteTextFieldCategories()
    }
}





// Preview
@Preview(showBackground = true, name = "SubmitApplicationScreen")
@Composable
private fun SubmitApplicationScreenPreview() {
    val navController = rememberNavController()
    CivitasAppTheme {
        SubmitApplicationScreen(
            navigationType = AppNavigationType.BOTTOM_NAVIGATION,
            navController = navController
        )
    }
}
