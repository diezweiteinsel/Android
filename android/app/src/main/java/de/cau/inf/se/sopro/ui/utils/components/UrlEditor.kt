package de.cau.inf.se.sopro.ui.utils.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import de.cau.inf.se.sopro.R
import de.cau.inf.se.sopro.ui.utils.HealthStatus


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