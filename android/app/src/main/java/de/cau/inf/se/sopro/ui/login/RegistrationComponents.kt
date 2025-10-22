package de.cau.inf.se.sopro.ui.login

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
import de.cau.inf.se.sopro.ui.utils.components.FormFieldState


//All the parts that get displayed on the RegistrationScreen
@Composable
fun RegistrationButton(onClick: () -> Unit) {
    ElevatedButton(onClick = { onClick() }) {
        Text(stringResource(R.string.registration_button))
    }
}

@Composable
fun GoToLoginScreen(navController: NavController) {
    Text(
        text = (stringResource(R.string.go_to_login_screen)),
        modifier = Modifier
            .padding(top = 8.dp)
            .clickable {
                navController.navigate(AppDestination.LoginDestination.route) {

                    popUpTo(AppDestination.RegistrationDestination.route) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
        },
        color = MaterialTheme.colorScheme.primary,
        textDecoration = TextDecoration.Underline
    )
}

data class RegistrationUiState(
    val username: FormFieldState = FormFieldState(),
    val email: FormFieldState = FormFieldState(),
    val password: FormFieldState = FormFieldState(),
    val confirmPassword: FormFieldState = FormFieldState(),
    val registrationError: String? = null
)