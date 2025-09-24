package de.cau.inf.se.sopro.ui.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import de.cau.inf.se.sopro.R
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

    ScreenScaffold(
        titleRes = R.string.registration_title,
        bottomBar = BottomBarSpec.Hidden
    ) { innerPadding ->
        RegistrationContent(
            modifier = modifier.padding(innerPadding),
            onRegistrationClick = {
                navController.navigateTopLevel(AppDestination.YourApplicationDestination)
            },
            navController = navController
        )
    }
}


//Unites all the components to be displayed on the RegistrationScreen
@Composable
fun RegistrationContent(
    modifier: Modifier = Modifier,
    onRegistrationClick: () -> Unit,
    navController: NavHostController
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordsDoNotMatch by remember { mutableStateOf(false) }

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
            modifier = Modifier.weight(2f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            NewUserNameTextField(
                value = username,
                onValueChange = { username = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                label = { Text(stringResource(R.string.user_name_text_field)) }
            )

            NewPasswordTextField(
                value = password,
                onValueChange = {
                    password = it
                    passwordsDoNotMatch = false
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                label = { Text(stringResource(R.string.password_text_field)) },
                isError = passwordsDoNotMatch
            )

            ConfirmPasswordTextField(
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                    passwordsDoNotMatch = false
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                label = { Text(stringResource(R.string.confirm_password_text_field)) },
                isError = passwordsDoNotMatch,
                supportingText = {
                    if (passwordsDoNotMatch) {
                        Text(
                            stringResource(R.string.passwords_do_not_match),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            )

            RegistrationButton(
                onClick = {
                    if (password == confirmPassword && password.isNotEmpty()) {
                        passwordsDoNotMatch = false
                        onRegistrationClick()
                        password = ""
                        confirmPassword = ""

                    } else {
                        passwordsDoNotMatch = true
                        password = ""
                        confirmPassword = ""
                    }
                }
            )

            GoToLoginScreen(
                navController = navController,
                destinationRoute = AppDestination.LoginDestination.route
            )
        }
    }
}
