package de.cau.inf.se.sopro.ui.submitApplication


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import dagger.hilt.android.lifecycle.HiltViewModel
import de.cau.inf.se.sopro.data.Repository
import de.cau.inf.se.sopro.network.api.createApplication
import de.cau.inf.se.sopro.ui.navigation.AppDestination
import de.cau.inf.se.sopro.ui.navigation.navigateTopLevel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class SubmitApplicationViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    private val _uiState = MutableStateFlow(SubmitApplicationUiState()) //our mutable uiState

    val uiState: StateFlow<SubmitApplicationUiState> = _uiState.asStateFlow()
    var curFormId : Int = 0 //our current form id


    fun onInit() {  //when we navigate to the submit application screen, we want to load the forms
        if (_uiState.value.categories.isNotEmpty()) {
            return
        }

        Log.d("MyApp", "onInit() executed")
        viewModelScope.launch {
            _uiState.value = SubmitApplicationUiState(isLoading = true)
            val forms = repository.getForms() //we get the forms from the repository
            for (form in forms!!) {
                if (form.formName !in _uiState.value.categories) { //for every form we update our categories which are displayed in the dropdown
                    _uiState.update {
                        it.copy(categories = it.categories.plus(form.formName) as List<String>) //we add the form name to our categories
                    }
                }
            }
            Log.d("MyApp", "categories: ${_uiState.value.categories}") //Logging
        }
    }
    fun onSubmit(){ //when we submit the application we want to create a new application
        if(_uiState.value.values.isNotEmpty()){ //check if something was filled out
            viewModelScope.launch {
                val payload = mutableMapOf<Int, FieldPayload>()
                for (i in _uiState.value.blocks.indices) {
                    val block = _uiState.value.blocks[i]
                    val value = uiState.value.values[block.label] ?: ""
                    payload[i + 1] = FieldPayload(block.label, value, block.datatype)
                }
                Log.d("MyApp", "payload: $payload")
                repository.createApplication(createApplication(curFormId,payload ))
                Log.d("MyApp", "${_uiState.value.values}")
                Log.d("MyApp", "${_uiState.value.blocks}")
                Log.d("MyApp", "onSubmit() executed")
            //navController.navigateTopLevel(AppDestination.YourApplicationDestination)
            }
        }
    }
    fun onCategoryChange(category: String) {
        _uiState.update { it.copy(selectedCategory = category) }
        createDynamicApplication()

    }
    //DAS WURDE LEIDER ZEITLICH NICHT MEHR GESCHAFFT
    fun checkApplication() { //we want to check if the values match the form,
        for (block in _uiState.value.blocks) {
            if (block.required && _uiState.value.values[block.label].isNullOrEmpty()) {
                // TODO: Zeige dem Nutzer eine Fehlermeldung (z.B. über den uiState)
                Log.w("ViewModel", "Required field '${block.label}' is empty.")
                return
            }
        }
    }

    fun createDynamicApplication() {

        viewModelScope.launch {
            val form = repository.getFormByTitle(uiState.value.selectedCategory)
            curFormId = form?.id ?: 0

            val buildingBlocks: List<UiBlock> =
                form?.blocks?.values?.map { block ->
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

            _uiState.update { it.copy(blocks = buildingBlocks) }
        }
    }
    fun onValueChange(id: String, value: String) {
        _uiState.update {

            it.copy(
                values = it.values.plus(Pair(id, value)) //updating our values
            )
        }
    }
    fun onCancelClicked(navController:NavHostController) {
        navController.navigateTopLevel(AppDestination.YourApplicationDestination)
    }
}

