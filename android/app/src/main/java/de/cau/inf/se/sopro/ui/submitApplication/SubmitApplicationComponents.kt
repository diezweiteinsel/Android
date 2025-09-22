package de.cau.inf.se.sopro.ui.submitApplication

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import kotlinx.coroutines.delay

@Composable
fun AutoCompleteTextFieldCategories() {
    var text by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val textFieldFocusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current


    val filteredSuggestions = remember(text) {
        if (text.isBlank()) {
            Suggestion.CATEGORIES
        } else {
            Suggestion.CATEGORIES.filter { it.contains(text, ignoreCase = true) }
        }
    }

    LaunchedEffect(expanded) {
        if (expanded) {
            // Tastatur öffnen mit einer kleinen Verzögerung
            delay(100)
            keyboardController?.show()
            textFieldFocusRequester.requestFocus()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                indication = null, // Keine visuelle Feedback
                interactionSource = remember { MutableInteractionSource() }
            ) {
                // Entfernt Focus und schließt Dropdown wenn irgendwo geklickt wird
                focusManager.clearFocus()
                expanded = false
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Dein AutoComplete TextField
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        // Verhindert dass der Parent-Click den Focus entfernt wenn auf TextField geklickt wird
                    }
            ) {
                OutlinedTextField(
                    value = text,
                    onValueChange = { newText ->
                        text = newText
                        expanded = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(textFieldFocusRequester)
                        .onFocusChanged { focusState ->

                            if (focusState.isFocused) {
                                expanded = true
                                keyboardController?.show()
                            }
                        },


                    label = { Text("Categories") },
                    placeholder = { Text("Type to see suggestions...") }
                )

                DropdownMenu(
                    expanded = expanded && filteredSuggestions.isNotEmpty(),
                    onDismissRequest = {
                        expanded = false
                        keyboardController?.hide()
                    },
                    modifier = Modifier.fillMaxWidth(0.9f)
                ) {
                    if (filteredSuggestions.isEmpty()) {
                        DropdownMenuItem(
                            text = { Text("No matches found") },
                            onClick = { expanded = false }
                        )
                    } else {
                        filteredSuggestions.forEach { suggestion ->
                            DropdownMenuItem(
                                text = { Text(suggestion) },
                                onClick = {
                                    text = suggestion
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}