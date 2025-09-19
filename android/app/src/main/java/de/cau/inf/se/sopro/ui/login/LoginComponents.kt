package de.cau.inf.se.sopro.ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController

import de.cau.inf.se.sopro.ui.utils.AppNavigationType
import de.cau.inf.se.sopro.R
import de.cau.inf.se.sopro.ui.theme.CivitasAppTheme


@Composable
fun LoginButton(onClick: () -> Unit) {
    ElevatedButton(onClick = { onClick() }) {
        Text(stringResource(R.string.login_button))
    }
}

@Composable
fun UserNameTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: @Composable (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = label,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        singleLine = true
    )
}

@Composable
fun PasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: @Composable (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = label,
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        singleLine = true
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
