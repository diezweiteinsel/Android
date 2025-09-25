package de.cau.inf.se.sopro.ui.options

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import de.cau.inf.se.sopro.R
import de.cau.inf.se.sopro.ui.core.ScreenScaffold
import de.cau.inf.se.sopro.ui.navigation.AppDestination
import de.cau.inf.se.sopro.ui.navigation.navigateTopLevel
import de.cau.inf.se.sopro.ui.utils.AppNavigationType


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OptionsScreen(navigationType: AppNavigationType,
                  navController: NavHostController,
                  modifier: Modifier = Modifier,
                  viewModel: OptionsViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.logoutEvent.collect {
            navController.navigateTopLevel(AppDestination.LoginDestination)
        }
    }

    ScreenScaffold(
        titleRes = R.string.options_title,
        onNavigateBack = { navController.popBackStack() }
    ) { innerPadding ->
        OptionsContent(
            modifier = modifier.padding(innerPadding),
            onSave = { navController.navigateTopLevel(AppDestination.YourApplicationDestination) },
            uiState = uiState,
            onLogoutClick = viewModel::onLogoutClick,
            onConfirmLogout = viewModel::onConfirmLogout,
            onDismissLogoutDialog = viewModel::onDismissLogoutDialog
        )
    }
}

@Composable
fun OptionsContent(
    modifier: Modifier = Modifier,
    onSave: () -> Unit,
    uiState: OptionsUiState,
    onLogoutClick: () -> Unit,
    onConfirmLogout: () -> Unit,
    onDismissLogoutDialog: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val defUrl = "http://localhost:8080"
            var newUrl by remember { mutableStateOf("") }

            //Choose value depending on what looks good. Higher value means more space at the top
            Spacer(modifier = Modifier.height(48.dp))

            ChangeURLTextField(
                value = newUrl,
                onValueChange = { newUrl = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(defUrl) }
            )

            Spacer(modifier = Modifier.height(8.dp))

            SaveButton(onClick = onSave)
        }

        Spacer(Modifier.weight(1f))

        LogoutButton(
            onShowDialog = onLogoutClick,
            modifier = Modifier.fillMaxWidth()
        )
    }

    if (uiState.showLogoutDialog) {
        LogoutConfirmationDialog(
            onConfirm = onConfirmLogout,
            onDismiss = onDismissLogoutDialog
        )
    }
}