package de.cau.inf.se.sopro.ui.login

import app.cash.turbine.test
import de.cau.inf.se.sopro.data.Repository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals
import de.cau.inf.se.sopro.R
import de.cau.inf.se.sopro.model.applicant.Usertype
import io.mockk.coEvery
import kotlinx.coroutines.test.runTest

class RegistrationViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: Repository // Mocked Repository
    private lateinit var viewModel: RegistrationViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk(relaxed = true)
        viewModel = RegistrationViewModel(repository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when onUsernameChanged is called, uiState updates username`() {
        // Arrange
        val newUsername = "testuser"

        // Act
        viewModel.onUsernameChanged(newUsername)

        // Assert
        val currentState = viewModel.uiState.value
        assertEquals(newUsername, currentState.username.value)
    }

    @Test
    fun `when onPasswordChanged is called, uiState updates password`() {
        // Arrange
        val password = "testpassword"

        // Act
        viewModel.onPasswordChanged(password)

        // Assert
        val currentState = viewModel.uiState.value
        assertEquals(password, currentState.password.value)
    }

    @Test
    fun `when onConfirmPasswordChanged is called, uiState updates confirmPassword`() {
        // Arrange
        val confirmPassword = "testpassword"

        // Act
        viewModel.onConfirmPasswordChanged(confirmPassword)

        // Assert
        val currentState = viewModel.uiState.value
        assertEquals(confirmPassword, currentState.confirmPassword.value)
    }

    @Test
    fun `onRegisterClick with valid data and successful repository call emits success event`() = runTest {
        // Given: All user inputs are valid
        val username = "validUser"
        val email = "valid@email.com"
        val password = "validPassword123"

        viewModel.onUsernameChanged(username)
        viewModel.onEmailChanged(email)
        viewModel.onPasswordChanged(password)
        viewModel.onConfirmPasswordChanged(password)

        // Given: The repository call will be successful
        coEvery { repository.createApplicant(username, email, password, Usertype.APPLICANT) } returns true

        // When: The register button is clicked
        viewModel.onRegisterClick()

        // Then: The success event is emitted
        viewModel.registrationSuccess.test {
            // Assert that one `true` event was received
            assertEquals(true, awaitItem())
            // Ensure no other events were sent
            cancelAndIgnoreRemainingEvents()
        }

        // Then: The repository function was called exactly once with the correct data
        coVerify(exactly = 1) {
            repository.createApplicant(
                username = username,
                email = email,
                password = password,
                role = Usertype.APPLICANT
            )
        }
    }

    @Test
    fun `onRegisterClick with mismatching passwords sets registrationError and does not call repository`() {
        viewModel.onPasswordChanged("password123")
        viewModel.onConfirmPasswordChanged("password456")
        viewModel.onUsernameChanged("validUsername")
        viewModel.onEmailChanged("valid@email.com")

        viewModel.onRegisterClick()

        val uiState = viewModel.uiState.value
        assertEquals(R.string.passwords_do_not_match, uiState.confirmPassword.errorMessageResId)

        coVerify(exactly = 0) {
            repository.createApplicant(any(), any(), any(), any())
        }
    }

    @Test
    fun `onRegisterClick with invalid username sets usernameError`() {
        viewModel.onUsernameChanged("lol")

        viewModel.onRegisterClick()

        val uiState = viewModel.uiState.value
        assertEquals(R.string.username_too_short, uiState.username.errorMessageResId)
    }

    @Test
    fun `onRegisterClick with blank password sets passwordError`() {
        viewModel.onPasswordChanged("")

        viewModel.onRegisterClick()

        val uiState = viewModel.uiState.value
        assertEquals(R.string.password_is_empty, uiState.password.errorMessageResId)
    }

    @Test
    fun `onRegisterClick with too short password sets passwordError`() {
        viewModel.onPasswordChanged("ab12")

        viewModel.onRegisterClick()

        val uiState = viewModel.uiState.value
        assertEquals(R.string.password_too_short, uiState.password.errorMessageResId)
    }
}