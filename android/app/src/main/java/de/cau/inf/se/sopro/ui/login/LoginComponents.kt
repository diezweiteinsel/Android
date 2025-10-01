package de.cau.inf.se.sopro.ui.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import de.cau.inf.se.sopro.R
import de.cau.inf.se.sopro.ui.navigation.AppDestination
import de.cau.inf.se.sopro.ui.theme.CivitasAppTheme
import de.cau.inf.se.sopro.ui.utils.AppNavigationType


//All the parts that get displayed on the LoginScreen
@Composable
fun LoginButton(onClick: () -> Unit) {
    ElevatedButton(onClick = { onClick() }) {
        Text(stringResource(R.string.login_button))
    }
}

@Composable
fun UsernameTextField(
    value : String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    supportingText: @Composable (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = label,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        singleLine = true,
        isError = isError,
        supportingText = supportingText
    )
}

@Composable
fun PasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    supportingText: @Composable (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = label,
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        singleLine = true,
        isError = isError,
        supportingText = supportingText
    )
}

@Composable
fun GoToRegistrationScreen(navController: NavController) {
    Text(
        text = (stringResource(R.string.go_to_registration_screen)),
        modifier = Modifier
            .padding(top = 8.dp)
            .clickable {
                navController.navigate(AppDestination.RegistrationDestination.route) {

                    popUpTo(AppDestination.LoginDestination.route) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
        },
        color = MaterialTheme.colorScheme.primary,
        textDecoration = TextDecoration.Underline
    )
}

@Composable
fun GoToYourApplicationScreen(navController: NavController) {
    Text(
        text = (stringResource(R.string.go_to_your_application_screen)),
        modifier = Modifier
            .padding(top = 8.dp)
            .clickable {
                navController.navigate(AppDestination.YourApplicationDestination.route) {

                    popUpTo(AppDestination.LoginDestination.route) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            },
        color = MaterialTheme.colorScheme.primary,
        textDecoration = TextDecoration.Underline
    )
}



// Preview
@Preview(showBackground = true, name = "LoginScreen")
@Composable
private fun LoginScreenPreview() {
    val navController = rememberNavController()
    CivitasAppTheme {
        LoginScreen(
            navigationType = AppNavigationType.BOTTOM_NAVIGATION,
            navController = navController
        )
    }
}

data class LoginUiState(
    val username: FormFieldState = FormFieldState(),
    val password: FormFieldState = FormFieldState(),
    val loginError: String? = null,
    val url: String = "",
    val urlError: String? = null,
    val showRestartMessage: Boolean = false
)

data class LoginFormFieldState(
    val value: String = "",
    val errorMessageResId: Int? = null
) {

    val isError: Boolean
        get() = errorMessageResId != null
}