package de.cau.inf.se.sopro.ui.login

import app.cash.turbine.test
import de.cau.inf.se.sopro.data.LoginResult
import de.cau.inf.se.sopro.data.Repository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class LoginViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: Repository // Mocked Repository
    private lateinit var viewModel: LoginViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk(relaxed = true)
        viewModel = LoginViewModel(repository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun onUsernameChanged_isCalled_uiStateUpdatesUsername() {
        // Arrange
        val newUsername = "testuser"

        // Act
        viewModel.onUsernameChange(newUsername)

        // Assert
        val currentState = viewModel.uiState.value
        assertEquals(newUsername, currentState.username.value)
    }

    @Test
    fun `onLoginClick with valid credentials and successful repository login emits success event`() = runTest {
        val username = "testuser"
        val password = "password123"
        viewModel.onUsernameChange(username)
        viewModel.onPasswordChange(password)

        coEvery { repository.authenticateLogin(username, password) } returns LoginResult.Success

        viewModel.onLoginClick()

        viewModel.loginSuccess.test {
            assertEquals(true, awaitItem())
            cancelAndIgnoreRemainingEvents() // Ensure no other events were sent
        }

        coVerify(exactly = 1) {
            repository.authenticateLogin(username, password)
        }

        val finalState = viewModel.uiState.value
        assertEquals(null, finalState.username.errorMessageResId)
        assertEquals(null, finalState.password.errorMessageResId)
        assertEquals(null, finalState.loginError)
    }
}