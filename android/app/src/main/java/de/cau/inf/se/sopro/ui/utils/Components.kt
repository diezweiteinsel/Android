package de.cau.inf.se.sopro.ui.utils

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import de.cau.inf.se.sopro.R
import de.cau.inf.se.sopro.ui.login.LoginViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DynamicSelectTextField(
    searchQuery: String,
    onQueryChanged: (String) -> Unit,
    onOptionSelected: (String) -> Unit,
    options: List<String>,
    label: String,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current

    // --- State for Validation ---
    var isError by remember { mutableStateOf(false) }

    // Tracks if the field has lost focus at least once to prevent showing an error initially
    var isFocused by remember { mutableStateOf(false) }

    // This effect runs whenever the search query changes or after the field has been focused and then unfocused
    LaunchedEffect(searchQuery, isFocused) {

        // An error occurs if the field has been touched, is not blank, and the query doesn't exactly match any option
        isError = !isFocused && searchQuery.isNotBlank() &&
                !options.any { it.equals(searchQuery, ignoreCase = true) }
    }

    // --- Dropdown Filtering Logic ---
    val filteredOptions = remember(searchQuery, options) {
        if (searchQuery.isBlank()) {
            options //show all options if there is no input
        } else {
            val filtered = options.filter { it.contains(searchQuery, ignoreCase = true) }
            filtered.ifEmpty {
                options //show all options if the input doesn't match the data
            }
        }
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = {
                onQueryChanged(it)

                if (!expanded) {
                    expanded = true
                }
            },
            label = { Text(text = label) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            isError = isError,
            supportingText = {
                if (isError) {
                    Text(text = stringResource(id = R.string.wrong_input))
                }
            },

            colors = OutlinedTextFieldDefaults.colors(),
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryEditable)
                .onFocusChanged { focusState ->
                    isFocused = focusState.isFocused
                }


        )
        if (filteredOptions.isNotEmpty()) {
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                    focusManager.clearFocus()
                }
            ) {
                filteredOptions.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(text = option) },
                        onClick = {
                            onOptionSelected(option)
                            onQueryChanged(option)
                            expanded = false
                            focusManager.clearFocus()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun UrlEditor(
    currentUrl: String,
    onUrlChange: (String) -> Unit,
    onSave: () -> Unit,
    toDefault: () -> Unit,
    checkHealth: () -> Unit,
    healthStatus: HealthStatus,
    isError: Boolean,
    supportingText: @Composable (() -> Unit)?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        OutlinedTextField(
            value = currentUrl,
            onValueChange = onUrlChange,
            modifier = Modifier.fillMaxWidth(0.7f),
            label = { Text(stringResource(id = R.string.change_url)) },
            placeholder = { Text(stringResource(id = R.string.example_url)) },
            isError = isError,
            supportingText = supportingText,
            singleLine = true
        )

        Row(
            modifier = Modifier.fillMaxWidth(0.7f),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = checkHealth,
                colors = when (healthStatus) {
                    HealthStatus.SUCCESS -> ButtonDefaults.buttonColors(containerColor = Color.Green)
                    HealthStatus.FAILURE -> ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    else -> ButtonDefaults.buttonColors()
                }
            ) {
                when (healthStatus) {
                    HealthStatus.LOADING -> {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }

                    else -> {
                        Text(stringResource(id = R.string.check_health))
                    }
                }
            }


            TextButton(onClick = toDefault) {
                Text(stringResource(id = R.string.reset))
            }
        }

        Button(onClick = onSave) {
            Text( stringResource(id = R.string.options_save_button) )
        }
    }
}