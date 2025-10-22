package de.cau.inf.se.sopro.ui.editApplication

import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.google.gson.Gson
import com.google.gson.JsonElement
import dagger.hilt.android.lifecycle.HiltViewModel
import de.cau.inf.se.sopro.R
import de.cau.inf.se.sopro.data.Repository
import de.cau.inf.se.sopro.data.UpdateResult
import de.cau.inf.se.sopro.ui.navigation.EDIT_APP_ID_ARG
import de.cau.inf.se.sopro.ui.navigation.EDIT_FORM_ID_ARG
import de.cau.inf.se.sopro.ui.submitApplication.FieldPayload
import de.cau.inf.se.sopro.ui.submitApplication.FieldType
import de.cau.inf.se.sopro.ui.submitApplication.UiBlock
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DynamicAttribute(
    val label: String,
    val value: JsonElement
)

data class EditApplicationUiState(
    val isLoading: Boolean = true,
    val formName: String = "",
    val blocks: List<UiBlock> = emptyList(),
    val values: Map<String, String> = emptyMap(),
    val error: String? = null,
    @StringRes val updateErrorResId: Int? = null
)

@HiltViewModel
class EditApplicationViewModel @Inject constructor(
    private val repository: Repository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditApplicationUiState())
    val uiState: StateFlow<EditApplicationUiState> = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<NavigationEvent>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val navigationEvent: SharedFlow<NavigationEvent> = _navigationEvent.asSharedFlow()

    private val applicationId: Int = checkNotNull(savedStateHandle[EDIT_APP_ID_ARG])
    private val formId: Int = checkNotNull(savedStateHandle[EDIT_FORM_ID_ARG])

    private val gson = Gson()

    init {
        Log.d("EditVM", "Received AppID: $applicationId, FormID: $formId")
        loadApplicationData()
    }

    fun onSubmit() {
        _uiState.update { it.copy(isLoading = true, updateErrorResId = null) }

        viewModelScope.launch {
            val payload = mutableMapOf<Int, FieldPayload>()

            val currentBlocks = _uiState.value.blocks
            val currentValues = _uiState.value.values

            for (i in currentBlocks.indices) {
                val block = currentBlocks[i]
                val value = currentValues[block.label] ?: ""
                payload[i + 1] = FieldPayload(block.label, value, block.datatype)
            }

            Log.d("EditVM", "Submitting Payload: $payload")
            val result = repository.updateApplication(applicationId, formId, payload)

            when (result) {
                is UpdateResult.Success -> {
                    _navigationEvent.tryEmit(NavigationEvent.NavigateBack)
                    _uiState.update { it.copy(isLoading = false) }
                }

                is UpdateResult.HttpError -> {
                    val errorRes = when (result.code) {
                        404 -> R.string.error_application_not_found
                        422 -> R.string.error_invalid_data
                        500 -> R.string.error_server_internal
                        else -> R.string.error_generic_update
                    }
                    _uiState.update { it.copy(isLoading = false, updateErrorResId = errorRes) }
                }

                is UpdateResult.NetworkError -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            updateErrorResId = R.string.error_network
                        )
                    }
                }

                is UpdateResult.UnknownError -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            updateErrorResId = R.string.error_generic_update
                        )
                    }
                }
            }
        }
    }

    sealed class NavigationEvent {
        data object NavigateBack : NavigationEvent()
    }

    private fun loadApplicationData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val application = repository.getApplicationByCompositeKey(applicationId, formId)
            if (application == null) {
                _uiState.update { it.copy(isLoading = false, error = "Application not found") }
                return@launch
            }

            val form = repository.getFormById(application.formId)
            if (form == null) {
                _uiState.update { it.copy(isLoading = false, error = "Form not found") }
                return@launch
            }

            val uiBlocks = form.blocks?.values?.map { block ->
                UiBlock(
                    block.label,
                    block.dataType,
                    block.required,
                    type = when (block.dataType.lowercase()) {
                        "string" -> FieldType.TEXT
                        "float" -> FieldType.NUMBER
                        "date" -> FieldType.DATE
                        else -> FieldType.TEXT
                    },
                    constraintsJson = null
                )
            } ?: emptyList()

            val initialValues = application.dynamicAttributes?.mapNotNull { entry ->
                try {
                    val attribute = gson.fromJson(entry.value, DynamicAttribute::class.java)

                    attribute.label to attribute.value.asString
                } catch (e: Exception) {
                    null
                }
            }?.toMap() ?: emptyMap()

            _uiState.update {
                it.copy(
                    isLoading = false,
                    formName = form.formName ?: "Edit Application",
                    blocks = uiBlocks,
                    values = initialValues
                )
            }
        }
    }

    fun onValueChange(label: String, value: String) {
        _uiState.update {
            it.copy(
                values = it.values.plus(label to value)
            )
        }
    }

    fun onCancelClicked(navController: NavHostController) {
        navController.popBackStack()
    }
}