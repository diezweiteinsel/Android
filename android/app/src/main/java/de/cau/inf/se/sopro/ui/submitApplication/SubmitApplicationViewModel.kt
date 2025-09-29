package de.cau.inf.se.sopro.ui.submitApplication

import androidx.compose.material3.OutlinedTextField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavHostController
import de.cau.inf.se.sopro.CivitasApplication
import de.cau.inf.se.sopro.data.Repository
import de.cau.inf.se.sopro.model.application.Application
import de.cau.inf.se.sopro.model.application.Form
import de.cau.inf.se.sopro.ui.navigation.AppDestination
import de.cau.inf.se.sopro.ui.navigation.navigateTopLevel
import kotlinx.coroutines.flow.update


import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SubmitApplicationViewModel(private val repository: Repository) : ViewModel() {
    private val _uiState = MutableStateFlow(SubmitApplicationUiState())

    val uiState: StateFlow<SubmitApplicationUiState> = _uiState.asStateFlow()

    fun onInit() {
        viewModelScope.launch {
            _uiState.value = SubmitApplicationUiState(isLoading = true)
            repository.getForms()
        }
    }

    fun onSubmit(navController: NavHostController){
        if(_uiState.value.values.isNotEmpty()){
            viewModelScope.launch {
                //repository.createApplication(Application(_uiState.value.values))
                navController.navigateTopLevel(AppDestination.YourApplicationDestination)
            }
        }
    }

    fun checkApplication() { //we want to check if the values match the form
        //todo
    }
    fun onCancelClicked(navController: NavHostController) {
        navController.navigateTopLevel(AppDestination.YourApplicationDestination)
    }

    fun createDynamicApplication() {
        viewModelScope.launch {
            val form =
                repository.getFormByTitle("Dog") //we want to get the selected form, then the structure will load
                val buildingBlocks: List<Block> =
                    form!!.sections.map { block -> //wir mappen jeden block aus form.sections auf ein Building Block object
                        Block(
                            block.blockId,
                            block.label, //problem, we dont know what a building block looks like
                            type = when (block.type.lowercase()) {
                                "text" -> FieldType.TEXT
                                "number" -> FieldType.NUMBER
                                else -> FieldType.TEXT
                            }
                        )
                    }
                for (block in buildingBlocks) { //erlaubt es uns dynamisch blocks hinzuzufügen
                    if (block !in blocks) {
                        blocks.add(block)
                    }
                }

                _uiState.update { it.copy(blocks = buildingBlocks) }

            }
    }
    fun onValueChange(id: String, value: String) {
        _uiState.update {
            it.copy(
                values = it.values.plus(Pair(id, value))
            )
        }
    }
    companion object { //viewmodel factory to be able to use our repository

        val Factory = viewModelFactory {
            initializer {
                val application =
                    this[AndroidViewModelFactory.APPLICATION_KEY] as CivitasApplication
                val repository = application.container.repository
                //val savedStateHandle = this.createSavedStateHandle()
                SubmitApplicationViewModel(repository)
            }
        }
    }


}

