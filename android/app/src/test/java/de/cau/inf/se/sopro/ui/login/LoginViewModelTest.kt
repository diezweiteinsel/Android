package de.cau.inf.se.sopro.ui.login

import de.cau.inf.se.sopro.data.Repository
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
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
}