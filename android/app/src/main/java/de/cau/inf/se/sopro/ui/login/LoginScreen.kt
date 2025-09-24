package de.cau.inf.se.sopro.ui.login


import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import de.cau.inf.se.sopro.R
import de.cau.inf.se.sopro.data.Repository
import de.cau.inf.se.sopro.ui.core.BottomBarSpec
import de.cau.inf.se.sopro.ui.core.ScreenScaffold
import de.cau.inf.se.sopro.ui.navigation.AppDestination
import de.cau.inf.se.sopro.ui.navigation.navigateTopLevel
import de.cau.inf.se.sopro.ui.theme.CivitasAppTheme
import de.cau.inf.se.sopro.ui.utils.AppNavigationType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navigationType: AppNavigationType, // TODO: navigation drawer or navigation rail
    navController: NavHostController,
    modifier: Modifier = Modifier
) {

    ScreenScaffold(
        titleRes = R.string.login_title,
        bottomBar = BottomBarSpec.Hidden
    ) { innerPadding ->
        LoginContent(
            modifier = modifier.padding(innerPadding),
            onLoginClick = {
                navController.navigateTopLevel(AppDestination.YourApplicationDestination)
            },
            navController = navController
        )
    }
}


//Unites all the components to be displayed on the LoginScreen
@Composable
fun LoginContent(
    modifier: Modifier = Modifier,
    onLoginClick: () -> Unit,
    navController: NavHostController
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    val vm: LoginViewModel = viewModel(factory = LoginViewModel.Factory)

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                keyboardController?.hide()
                focusManager.clearFocus()
            },

        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Choose value depending on what looks better. Higher value means more space at the top
        Spacer(modifier = Modifier.weight(0.3f))

        Column(
            modifier = Modifier.weight(2f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            UserNameTextField(
                value = vm.username,
                onValueChange = { vm.onUsernameChange(it)},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                label = { Text(stringResource(R.string.user_name_text_field)) }
            )

            PasswordTextField(
                value = vm.password,
                onValueChange = { vm.onPasswordChange(it)},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                label = { Text(stringResource(R.string.password_text_field)) }
            )

            LoginButton(
                onClick = { vm.Login() }

            )

            GoToRegistrationScreen(
                navController = navController,
                destinationRoute = AppDestination.RegistrationDestination.route
            )

        }
    }
}

