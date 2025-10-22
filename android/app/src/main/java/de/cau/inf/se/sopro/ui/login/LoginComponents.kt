package de.cau.inf.se.sopro.ui.login

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import de.cau.inf.se.sopro.R
import de.cau.inf.se.sopro.ui.navigation.AppDestination
import de.cau.inf.se.sopro.ui.utils.HealthStatus
import de.cau.inf.se.sopro.ui.utils.components.FormFieldState


//All the parts that get displayed on the LoginScreen
@Composable
fun LoginButton(onClick: () -> Unit) {
    ElevatedButton(onClick = { onClick() }) {
        Text(stringResource(R.string.login_button))
    }
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

data class LoginUiState(
    val username: FormFieldState = FormFieldState(),
    val password: FormFieldState = FormFieldState(),
    val loginError: String? = null,
    val url: String = "",
    val urlError: String? = null,
    val showRestartMessage: Boolean = false,
    val healthStatus: HealthStatus = HealthStatus.IDLE,
    @StringRes val urlErrorResId: Int? = null,
    @StringRes val loginErrorResId: Int? = null
)