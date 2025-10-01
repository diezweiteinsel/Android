package de.cau.inf.se.sopro.ui.login


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import de.cau.inf.se.sopro.R
import de.cau.inf.se.sopro.ui.core.BottomBarSpec
import de.cau.inf.se.sopro.ui.core.ScreenScaffold
import de.cau.inf.se.sopro.ui.navigation.AppDestination
import de.cau.inf.se.sopro.ui.utils.AppNavigationType
import de.cau.inf.se.sopro.ui.utils.UrlEditor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navigationType: AppNavigationType, // TODO: navigation drawer or navigation rail
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val viewModel: LoginViewModel = viewModel(factory = LoginViewModel.Factory)

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = navController, key2 = viewModel) {
        viewModel.loginSuccess.collect { success ->
            if (success) {
                navController.navigate(AppDestination.YourApplicationDestination.route) {
                    popUpTo(AppDestination.LoginDestination.route) {
                        inclusive = true
                    }
                }
            }
        }
    }

    ScreenScaffold(
        titleRes = R.string.login_title,
        bottomBar = BottomBarSpec.Hidden
    ) { innerPadding ->
        LoginContent(
            modifier = modifier.padding(innerPadding),
            uiState = uiState,
            onUsernameChange = viewModel::onUsernameChange,
            onPasswordChange = viewModel::onPasswordChange,
            onLoginClick = viewModel::onLoginClick,
            onUrlChange = viewModel::onUrlChange,
            onSaveUrl = viewModel::onSaveUrl,
            toDefault = viewModel::toDefaultUrl,
            onDismissRestartMessage = viewModel::onDismissRestartMessage,
            checkHealth = viewModel::checkHealth,
            navController = navController
        )
    }
}


//Unites all the components to be displayed on the LoginScreen
@Composable
fun LoginContent(
    modifier: Modifier = Modifier,
    uiState: LoginUiState,
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onUrlChange: (String) -> Unit,
    onSaveUrl: () -> Unit,
    toDefault: () -> Unit,
    checkHealth: () -> Unit,
    onDismissRestartMessage: () -> Unit,
    navController: NavHostController
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    //val vm: LoginViewModel = viewModel(factory = LoginViewModel.Factory)

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                keyboardController?.hide()
                focusManager.clearFocus()
            },

        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Choose value depending on what looks better. Higher value means more space at the top
        Spacer(modifier = Modifier.weight(0.3f))

        Column(
            modifier = Modifier
                .weight(2f)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            UsernameTextField(
                value = uiState.username.value,
                onValueChange = onUsernameChange,
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .padding(bottom = 16.dp),
                label = { Text(stringResource(R.string.user_name_text_field)) },
                isError = uiState.username.isError,
                supportingText = {
                    uiState.username.errorMessageResId?.let {
                        Text(
                            stringResource(it),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            )

            PasswordTextField(
                value = uiState.password.value,
                onValueChange = onPasswordChange,
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .padding(bottom = 16.dp),
                label = { Text(stringResource(R.string.password_text_field)) },
                isError = uiState.password.isError,
                supportingText = {
                    uiState.password.errorMessageResId?.let {
                        Text(
                            stringResource(it),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            )

            LoginButton(
                onClick = onLoginClick
            )

            GoToRegistrationScreen(navController = navController)

            GoToYourApplicationScreen(navController = navController)

            var isExpanded by remember { mutableStateOf(false) }

            val rotationAngle by animateFloatAsState(
                targetValue = if (isExpanded) 180f else 0f,
                label = "iconRotation"
            )

            Row(
                modifier = Modifier
                    .padding(top = 24.dp)
                    .clickable { isExpanded = !isExpanded },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.change_server_options),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    modifier = Modifier.rotate(rotationAngle)
                )
            }

            AnimatedVisibility(visible = isExpanded) {
                UrlEditor(
                    currentUrl = uiState.url,
                    onUrlChange = onUrlChange,
                    onSave = onSaveUrl,
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
            }
        }

        if (uiState.showRestartMessage) {
            AlertDialog(
                onDismissRequest = onDismissRestartMessage,
                title = { Text(stringResource(R.string.options_save_button)) },
                text = { Text(stringResource(R.string.new_url_msg)) },
                confirmButton = {
                    TextButton(
                        onClick = onDismissRestartMessage
                    ) {
                        Text(stringResource(R.string.ok))
                    }
                }
            )
        }
    }
}
