package de.cau.inf.se.sopro.ui.editApplication

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import de.cau.inf.se.sopro.R
import de.cau.inf.se.sopro.ui.core.ScreenScaffold
import de.cau.inf.se.sopro.ui.core.createBottomBar
import de.cau.inf.se.sopro.ui.navigation.AppDestination
import de.cau.inf.se.sopro.ui.submitApplication.DynamicForm
import de.cau.inf.se.sopro.ui.utils.AppNavigationType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditApplicationScreen(
    navigationType: AppNavigationType,
    navController: NavHostController,
    viewModel: EditApplicationViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    val bottomBar = remember(navigationType, navController) {
        createBottomBar(navigationType, currentTab = AppDestination.SubmitApplicationDestination, navController)
    }

    ScreenScaffold(
        titleRes = R.string.edit_application_title,
        bottomBar = bottomBar
    ) { innerPadding ->

        DynamicForm(
            modifier = Modifier.padding(innerPadding).fillMaxSize(),
            blocks = uiState.value.blocks,
            values = uiState.value.values,
            onValueChange = viewModel::onValueChange,
            onCancelClicked = { /* ... */ },
            onSubmit = viewModel::onSubmit
        )
    }
}