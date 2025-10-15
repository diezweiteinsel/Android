package de.cau.inf.se.sopro.ui.submitApplication


import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import de.cau.inf.se.sopro.R
import de.cau.inf.se.sopro.ui.core.ScreenScaffold
import de.cau.inf.se.sopro.ui.core.createBottomBar
import de.cau.inf.se.sopro.ui.navigation.AppDestination
import de.cau.inf.se.sopro.ui.utils.AppNavigationType


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubmitApplicationScreen(
    navigationType: AppNavigationType, // TODO: navigation drawer or navigation rail
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val vm: SubmitApplicationViewModel = viewModel(factory = SubmitApplicationViewModel.Factory)
    val uiState = vm.uiState.collectAsStateWithLifecycle()



    LaunchedEffect(uiState) {
        vm.onInit()

    }
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
            modifier = Modifier.padding(innerPadding).heightIn(0.dp,600.dp),
            navController = navController, // Pass the collected uiState
            values = uiState.value.values, //look into Components submitUiState
            blocks = uiState.value.blocks,
            onValueChange = vm::onValueChange, //method from viewModel
            onCancelClicked = {vm.onCancelClicked(navController)}, //button actions
            onSubmit = vm::onSubmit,
            onCategoryChange = vm::onCategoryChange,
            categories = uiState.value.categories,
            selectedCategory = uiState.value.selectedCategory)

        }
    }


@Composable
fun SubmitApplicationContent(
    modifier: Modifier = Modifier,
    navController: NavHostController,
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

    Column(             //we are putting everything in a column
        modifier = modifier
            .fillMaxSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                focusManager.clearFocus()
            },
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SubmitApplicationCategory(modifier = Modifier.fillMaxWidth(), //this is our dropdown
            onValueChange = onCategoryChange,
            selectedCategory,categories )


        if(selectedCategory.isNotEmpty()) { //only if the applicant has chosen a category, we want to show the form/ the form is built
            DynamicForm(
                modifier = Modifier.fillMaxWidth().weight(1f),
                blocks,
                values = values,
                onValueChange = onValueChange,
                onCancelClicked = onCancelClicked,
                onSubmit = onSubmit
            )
        }
    }
}


