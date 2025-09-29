package de.cau.inf.se.sopro.ui.submitApplication

import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import de.cau.inf.se.sopro.data.Repository
import de.cau.inf.se.sopro.model.application.Block
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class SubmitApplicationViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var repository: Repository
    private lateinit var viewModel: SubmitApplicationViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk(relaxed = true)
        viewModel = SubmitApplicationViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onInit sets loading state and fetches forms`() = runTest {
        coEvery { repository.getForms() } returns emptyList()

        viewModel.onInit()
        testDispatcher.scheduler.advanceUntilIdle()

        val uiState = viewModel.uiState.first()
        assertEquals(true, uiState.isLoading)
    }

    @Test
    fun `onValueChange updates values map`() = runTest {
        val key = "Vorname"
        val value = "Max"

        viewModel.onValueChange(key, value)
        testDispatcher.scheduler.advanceUntilIdle()

        val uiState = viewModel.uiState.first()
        assertEquals(value, uiState.values[key])
    }

    @Test
    fun `createDynamicApplication maps blocks correctly`() = runTest {
        val mockForm = mockk<de.cau.inf.se.sopro.model.application.Form>()
        coEvery { repository.getFormByTitle("Dog") } returns mockForm
        // Assuming sections is list of blocks in the form
        coEvery { mockForm.sections } returns listOf(
            Block(
                id = "1",
                name = "Vorname",
                type = "String",
                value = "Max"
            )
        )

        viewModel.createDynamicApplication()
        testDispatcher.scheduler.advanceUntilIdle()

        val uiState = viewModel.uiState.first()
        val block = uiState.blocks.first()
        assertEquals("Vorname", block.label)
        assertEquals(FieldType.TEXT, block.type)
    }

    @Test
    fun `onSubmit triggers navigation`() = runTest {
        val values = mapOf("Vorname" to "Max")
        viewModel.onValueChange("Vorname", "Max")

        var navTriggered = false
        val fakeController = mockk<NavHostController>(relaxed = true)
        fun NavHostController.navigate(route: String, navOptions: NavOptions? = null) {
            navTriggered = true
        }
        viewModel.onSubmit(fakeController)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(true,navTriggered)
    }
}
