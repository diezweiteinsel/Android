package de.cau.inf.se.sopro.ui.editApplication

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import de.cau.inf.se.sopro.R
import de.cau.inf.se.sopro.ui.core.ScreenScaffold
import de.cau.inf.se.sopro.ui.core.createBottomBar
import de.cau.inf.se.sopro.ui.navigation.AppDestination
import de.cau.inf.se.sopro.ui.submitApplication.CancelButton
import de.cau.inf.se.sopro.ui.submitApplication.DynamicForm
import de.cau.inf.se.sopro.ui.submitApplication.SubmitButton
import de.cau.inf.se.sopro.ui.utils.AppNavigationType
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalFocusManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditApplicationScreen(
    navController: NavHostController,
    viewModel: EditApplicationViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is EditApplicationViewModel.NavigationEvent.NavigateBack -> {
                    navController.popBackStack()
                }
            }
        }
    }

    ScreenScaffold(
        titleRes = R.string.edit_application_title
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    focusManager.clearFocus()
                },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DynamicForm(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                blocks = uiState.blocks,
                values = uiState.values,
                onValueChange = viewModel::onValueChange
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                CancelButton(
                    onClick = { viewModel.onCancelClicked(navController) }
                )
                SubmitButton(
                    onClick = viewModel::onSubmit,
                    textResId = R.string.save_changes
                )
            }
        }
    }
}