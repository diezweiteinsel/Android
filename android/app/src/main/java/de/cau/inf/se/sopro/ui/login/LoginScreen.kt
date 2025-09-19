package de.cau.inf.se.sopro.ui.login


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import de.cau.inf.se.sopro.ui.core.ScreenScaffold
import de.cau.inf.se.sopro.ui.utils.AppNavigationType
import de.cau.inf.se.sopro.R
import de.cau.inf.se.sopro.ui.core.BottomBarSpec
import de.cau.inf.se.sopro.ui.core.createBottomBar
import de.cau.inf.se.sopro.ui.navigation.AppDestination
import de.cau.inf.se.sopro.ui.navigation.navigateTopLevel
import de.cau.inf.se.sopro.ui.theme.CivitasAppTheme


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
            }
        )
    }
}



@Composable
fun LoginContent(
    modifier: Modifier = Modifier,
    onLoginClick: () -> Unit
) {

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = modifier.padding(16.dp),
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
                value = username,
                onValueChange = { username = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                label = { Text(stringResource(R.string.user_name_text_field)) }
            )

            PasswordTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                label = { Text(stringResource(R.string.password_text_field)) }    //TODO change to strings.xml thingie
            )

            LoginButton(
                onClick = onLoginClick
            )
        }
    }
}
