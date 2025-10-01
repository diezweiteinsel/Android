package de.cau.inf.se.sopro.ui.options

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import de.cau.inf.se.sopro.R
import de.cau.inf.se.sopro.ui.core.ScreenScaffold
import de.cau.inf.se.sopro.ui.navigation.AppDestination
import de.cau.inf.se.sopro.ui.navigation.navigateTopLevel
import de.cau.inf.se.sopro.ui.utils.AppNavigationType
import de.cau.inf.se.sopro.ui.utils.UrlEditor


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
            onSave = viewModel::onSaveUrl,
            uiState = uiState,
            onLogoutClick = viewModel::onLogoutClick,
            onConfirmLogout = viewModel::onConfirmLogout,
            onUrlChange = viewModel::onUrlChange,
            toDefault = viewModel::toDefaultUrl,
            onDismissLogoutDialog = viewModel::onDismissLogoutDialog,
            onDismissRestartMessage = { viewModel.onDismissRestartMessage() },
            checkHealth = viewModel::checkHealth
        )
    }
}

@Composable
fun OptionsContent(
    modifier: Modifier = Modifier,
    onSave: () -> Unit,
    uiState: OptionsViewModel.OptionsUiState,
    onLogoutClick: () -> Unit,
    onConfirmLogout: () -> Unit,
    toDefault: () -> Unit,
    checkHealth: () -> Unit,
    onUrlChange: (String) -> Unit,
    onDismissLogoutDialog: () -> Unit,
    onDismissRestartMessage: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                focusManager.clearFocus()
                keyboardController?.hide()
            }

    ) {

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            val isError = uiState.urlError != null

            //Choose value depending on what looks good. Higher value means more space at the top
            Spacer(modifier = Modifier.height(48.dp))

            UrlEditor(
                currentUrl = uiState.url,
                onUrlChange = onUrlChange,
                onSave = onSave,
                isError = uiState.urlError != null,
                supportingText = {
                    if (uiState.urlError != null) {
                        Text(text = uiState.urlError, color = MaterialTheme.colorScheme.error)
                    }
                },
                toDefault = toDefault,
                checkHealth = checkHealth,
                healthStatus = uiState.healthStatus
            )

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

    if (uiState.showRestartMessage) {
        AlertDialog(
            onDismissRequest = onDismissRestartMessage,
            title = { Text("Gespeichert") },
            text = { Text("Die neue URL wird nach einem Neustart der App verwendet.") },
            confirmButton = {
                TextButton(
                    onClick = onDismissRestartMessage
                ) {
                    Text("OK")
                }
            }
        )
    }
}
