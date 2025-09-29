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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import de.cau.inf.se.sopro.R
import de.cau.inf.se.sopro.ui.navigation.AppDestination


//All the parts that get displayed on the RegistrationScreen
@Composable
fun NewUsernameTextField(
    value: String,
    onValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    supportingText: @Composable (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChanged,
        modifier = modifier,
        label = label,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        singleLine = true,
        isError = isError,
        supportingText = supportingText
    )
}

@Composable
fun EmailTextField(
    value: String,
    onValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    supportingText: @Composable (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChanged,
        modifier = modifier,
        label = label,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        singleLine = true,
        isError = isError,
        supportingText = supportingText
    )
}

@Composable
fun NewPasswordTextField(
    value: String,
    onValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    supportingText: @Composable (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChanged,
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
fun RegistrationButton(onClick: () -> Unit) {
    ElevatedButton(onClick = { onClick() }) {
        Text(stringResource(R.string.registration_button))
    }
}

@Composable
fun ConfirmPasswordTextField(
    value: String,
    onValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    supportingText: @Composable (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChanged,
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

data class FormFieldState(
    val value: String = "",
    val errorMessageResId: Int? = null
) {

    val isError: Boolean
        get() = errorMessageResId != null
}


