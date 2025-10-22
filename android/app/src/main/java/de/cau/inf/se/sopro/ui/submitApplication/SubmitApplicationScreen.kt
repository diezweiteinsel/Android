package de.cau.inf.se.sopro.ui.submitApplication


import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import de.cau.inf.se.sopro.R
import de.cau.inf.se.sopro.ui.core.ScreenScaffold
import de.cau.inf.se.sopro.ui.core.createBottomBar
import de.cau.inf.se.sopro.ui.navigation.AppDestination
import de.cau.inf.se.sopro.ui.utils.AppNavigationType


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubmitApplicationScreen(
    navigationType: AppNavigationType,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val vm: SubmitApplicationViewModel = hiltViewModel()
    val uiState = vm.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) { vm.onInit() }
    // Reuse the same BottomBar instance across recompositions (hence remember) as long as
    // navigationType and navController stay the same. This avoids unnecessary work.
    val bottomBar = remember(navigationType, navController) {
        createBottomBar(navigationType, currentTab = AppDestination.SubmitApplicationDestination, navController)
    }

    ScreenScaffold(
        titleRes = R.string.submit_application_title,
        bottomBar = bottomBar
    ) { innerPadding ->

        SubmitApplicationContent(
            modifier = Modifier.padding(innerPadding),
            values = uiState.value.values, //look into Components submitUiState
            blocks = uiState.value.blocks,
            onValueChange = vm::onValueChange, //method from viewModel
            onCancelClicked = { vm.onCancelClicked(navController) }, //button actions
            onSubmit = vm::onSubmit,
            onCategoryChange = vm::onCategoryChange,
            categories = uiState.value.categories,
            selectedCategory = uiState.value.selectedCategory
        )
    }
}


@Composable
fun SubmitApplicationContent(
    modifier: Modifier = Modifier,
    values: Map<String, String>,
    blocks: List<UiBlock>,
    onValueChange: (String, String) -> Unit,
    onCancelClicked: () -> Unit,
    onSubmit: () -> Unit,
    onCategoryChange: (String) -> Unit,
    categories: List<String>,
    selectedCategory: String
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                focusManager.clearFocus()
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SubmitApplicationCategory(
            modifier = Modifier.fillMaxWidth(), //this is our dropdown
            onValueChange = onCategoryChange,
            selectedCategory,
            categories
        )


        if (selectedCategory.isNotEmpty()) {
            DynamicForm(
                modifier = Modifier.fillMaxWidth(),
                blocks = blocks,
                values = values,
                onValueChange = onValueChange,
                footerContent = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp), // Padding für Buttons
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        CancelButton(onClick = onCancelClicked)
                        SubmitButton(onClick = onSubmit)
                    }
                }
            )

        } else {
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}