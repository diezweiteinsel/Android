package de.cau.inf.se.sopro.ui.options

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import de.cau.inf.se.sopro.R
import de.cau.inf.se.sopro.ui.core.ScreenScaffold
import de.cau.inf.se.sopro.ui.navigation.AppDestination
import de.cau.inf.se.sopro.ui.navigation.navigateTopLevel
import de.cau.inf.se.sopro.ui.utils.AppNavigationType


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OptionsScreen(navigationType: AppNavigationType,
                  navController: NavHostController,
                  modifier: Modifier = Modifier){
    ScreenScaffold(
        titleRes = R.string.options_title,
        onNavigateBack = { navController.popBackStack() }
    ) { innerPadding ->
        OptionsContent(
            modifier = modifier.padding(innerPadding),
            onSave = { navController.navigateTopLevel(AppDestination.YourApplicationDestination) },
            navController = navController
        )
    }
}

@Composable
fun OptionsContent(
    modifier: Modifier = Modifier,
    onSave: () -> Unit,
    navController: NavHostController
) {
    var showLogoutDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val defUrl = "http://localhost:8080"
            var newUrl by remember { mutableStateOf("") }

            //Choose value depending on what looks good. Higher value means more space at the top
            Spacer(modifier = Modifier.height(48.dp))

            ChangeURLTextField(
                value = newUrl,
                onValueChange = { newUrl = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(defUrl) }
            )

            Spacer(modifier = Modifier.height(8.dp))

            SaveButton(onClick = onSave)
        }

        Spacer(Modifier.weight(1f))

        LogoutButton(
            onShowDialog = { showLogoutDialog = true },
            modifier = Modifier.fillMaxWidth()
        )
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = {
                showLogoutDialog = false
            },
            title = {
                Text(stringResource(R.string.log_out))
            },
            text = {
                Text(stringResource(R.string.logout_confirmation))
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                        navController.navigateTopLevel(AppDestination.LoginDestination)
                    }
                ) {
                    Text(stringResource(R.string.log_out))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                    }
                ) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }
}