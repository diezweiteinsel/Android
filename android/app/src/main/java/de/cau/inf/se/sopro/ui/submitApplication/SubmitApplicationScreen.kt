package de.cau.inf.se.sopro.ui.submitApplication


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.values
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import de.cau.inf.se.sopro.R
import de.cau.inf.se.sopro.ui.core.ScreenScaffold
import de.cau.inf.se.sopro.ui.core.createBottomBar
import de.cau.inf.se.sopro.ui.login.GoToYourApplicationScreen
import de.cau.inf.se.sopro.ui.login.LoginViewModel
import de.cau.inf.se.sopro.ui.login.RegistrationViewModel
import de.cau.inf.se.sopro.ui.navigation.AppDestination
import de.cau.inf.se.sopro.ui.theme.CivitasAppTheme
import de.cau.inf.se.sopro.ui.utils.AppNavigationType
import kotlin.collections.getValue


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubmitApplicationScreen(
    navigationType: AppNavigationType, // TODO: navigation drawer or navigation rail
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val vm: SubmitApplicationViewModel = viewModel(factory = SubmitApplicationViewModel.Factory)
    val uiState = vm.uiState.collectAsStateWithLifecycle()



    //load Forms on init
    LaunchedEffect(Unit) {
        vm.onInit()
    }
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
            modifier = Modifier.padding(innerPadding), // Pass only the padding modifier
            navController = navController,
            uiState = uiState, // Pass the collected uiState
            dynamicFormValues = uiState.values,
            dynamicFormFields = uiState.fields,
            onDynamicValueChange = vm::onValueChange,
            onCancelClicked = { vm.onCancelClicked(navController)
            }
    }
}

@Composable
fun SubmitApplicationContent(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    uiState: SubmitApplicationUiState,
    values: Map<String, String>,
    Blocks: List<Block>,
    onValueChange: (String, String) -> Unit,
    onCancelClicked: () -> Unit
) {

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SubmitApplicationForm()

        DynamicForm(modifier = modifier,
            blocks = uiState.value.blocks,
            values = uiState.value.values, onValueChange = vm::onValueChange)

        CancelButton(modifier = Modifier.fillMaxWidth().padding(16.dp),
            onClick = onCancelClicked
        )
        GoToYourApplicationScreen(navController = navController)
    }
}


