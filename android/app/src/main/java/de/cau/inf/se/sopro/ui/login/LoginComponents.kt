package de.cau.inf.se.sopro.ui.login

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import de.cau.inf.se.sopro.R
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

/*
@Composable
fun LanguageChangeFAB(modifier: Modifier = Modifier) {
    // State to control if the dropdown menu is open or closed.
    var menuExpanded by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // A Box is used to anchor the DropdownMenu to the FAB.
    Box(modifier = modifier) {
        FloatingActionButton(
            onClick = { menuExpanded = true } // Click opens the menu.
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = "Change Language"
            )
        }

        DropdownMenu(
            expanded = menuExpanded,
            onDismissRequest = { menuExpanded = false } // Clicking outside closes the menu.
        ) {
            DropdownMenuItem(
                text = { Text("Deutsch 🇩🇪") },
                onClick = {
                    LocaleHelper.setLocale(context, "de")
                    menuExpanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("English 🇬🇧") },
                onClick = {
                    LocaleHelper.setLocale(context, "en")
                    menuExpanded = false
                }
            )
            // Add more languages here...
        }
    }
}
*/



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
