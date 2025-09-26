package de.cau.inf.se.sopro.ui.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import de.cau.inf.se.sopro.R
import de.cau.inf.se.sopro.data.ApplicantRepository
import de.cau.inf.se.sopro.ui.core.BottomBarSpec
import de.cau.inf.se.sopro.ui.core.ScreenScaffold
import de.cau.inf.se.sopro.ui.navigation.AppDestination
import de.cau.inf.se.sopro.ui.navigation.navigateTopLevel
import de.cau.inf.se.sopro.ui.utils.AppNavigationType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen(
    navigationType: AppNavigationType, // TODO: navigation drawer or navigation rail
    navController: NavHostController,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current
    val repository = ApplicantRepository(context.applicationContext)

    val viewModel: RegistrationViewModel = viewModel(
        factory = RegistrationViewModel.Factory(repository)
    )

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ScreenScaffold(
        titleRes = R.string.registration_title,
        bottomBar = BottomBarSpec.Hidden
    ) { innerPadding ->
        RegistrationContent(
            modifier = modifier.padding(innerPadding),
            uiState = uiState,
            onUserNameChanged = viewModel::onUsernameChanged,
            onPasswordChanged = viewModel::onPasswordChanged,
            onConfirmPasswordChanged = viewModel::onConfirmPasswordChanged,
            onRegistrationClick = {
                if (viewModel.validateAndRegister()) {
                    navController.navigateTopLevel(AppDestination.LoginDestination)
                }
            },
            navController = navController
        )
    }
}


//Unites all the components to be displayed on the RegistrationScreen
@Composable
fun RegistrationContent(
    modifier: Modifier = Modifier,
    uiState: RegistrationUiState,
    onUserNameChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onConfirmPasswordChanged: (String) -> Unit,
    onRegistrationClick: () -> Unit,
    navController: NavHostController
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

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
        Spacer(modifier = Modifier.weight(0.2f))

        Column(
            modifier = Modifier
                .weight(2f)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            NewUsernameTextField(
                value = uiState.username.value,
                onValueChange = onUserNameChanged,
                modifier = Modifier
                    .fillMaxWidth()
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

            NewPasswordTextField(
                value = uiState.password.value,
                onValueChange = onPasswordChanged,
                modifier = Modifier
                    .fillMaxWidth()
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

            ConfirmPasswordTextField(
                value = uiState.confirmPassword.value,
                onValueChange = onConfirmPasswordChanged,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                label = { Text(stringResource(R.string.confirm_password_text_field)) },
                isError = uiState.confirmPassword.isError,
                supportingText = {
                    uiState.confirmPassword.errorMessageResId?.let {
                        Text(
                            stringResource(it),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            )

            RegistrationButton(
                onClick = onRegistrationClick
            )

            GoToLoginScreen(navController = navController)
        }
    }
}
