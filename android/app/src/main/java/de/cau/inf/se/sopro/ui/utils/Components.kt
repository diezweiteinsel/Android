package de.cau.inf.se.sopro.ui.utils

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import de.cau.inf.se.sopro.R


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
