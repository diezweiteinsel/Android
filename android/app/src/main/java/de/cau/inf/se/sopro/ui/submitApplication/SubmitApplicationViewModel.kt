package de.cau.inf.se.sopro.ui.submitApplication

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.material3.OutlinedTextField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavHostController
import dagger.hilt.android.internal.Contexts.getApplication
import de.cau.inf.se.sopro.CivitasApplication
import de.cau.inf.se.sopro.data.Repository
import de.cau.inf.se.sopro.data.TokenManager
import de.cau.inf.se.sopro.model.application.Application
import de.cau.inf.se.sopro.model.application.Block
import de.cau.inf.se.sopro.model.application.Form
import de.cau.inf.se.sopro.network.api.createApplication
import de.cau.inf.se.sopro.ui.navigation.AppDestination
import de.cau.inf.se.sopro.ui.navigation.navigateTopLevel
import kotlinx.coroutines.flow.update


import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime


class SubmitApplicationViewModel(private val repository: Repository) : ViewModel() {
    private val _uiState = MutableStateFlow(SubmitApplicationUiState()) //our mutable uiState

    val uiState: StateFlow<SubmitApplicationUiState> = _uiState.asStateFlow()
    var curFormId : Int = 0 //our current form id


    fun onInit() {  //when we navigate to the submit application screen, we want to load the forms
        Log.d("MyApp", "onInit() executed")
        viewModelScope.launch {
            _uiState.value = SubmitApplicationUiState(isLoading = true)
            val forms = repository.getForms() //we get the forms from the repository
            for (form in forms!!) {
                if (form.form_name !in _uiState.value.categories) { //for every form we update our categories which are displayed in the dropdown
                    _uiState.update {
                        it.copy(categories = it.categories.plus(form.form_name) as List<String>) //we add the form name to our categories
                    }
                }
                Log.d("MyApp", "categories: ${_uiState.value.categories}") //Logging

            }
        }
    }
    fun onSubmit(){ //when we submit the application we want to create a new application
        if(_uiState.value.values.isNotEmpty()){ //check if something was filled out
            viewModelScope.launch {
                val payload = mutableMapOf<Int, FieldPayload>()
                for(i in 1 until _uiState.value.blocks.size){
                    payload.put(i, FieldPayload(_uiState.value.blocks[i].label,uiState.value.values[_uiState.value.blocks[i].label].toString(),_uiState.value.blocks[i].datatype),)
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
        for(block in blocks){
            if(block.required && _uiState.value.values[block.label] == null){
                //Toast.makeText(createDynamicApplication(), "Please fill out all required fields", Toast.LENGTH_SHORT)
            }
        }
    }

    fun createDynamicApplication() {

        viewModelScope.launch {
            val form = repository.getFormByTitle(uiState.value.selectedCategory) //we want to get the selected form, then the structure will load
            curFormId = form?.id ?: 0
                val buildingBlocks: List<UiBlock> =
                    form?.blocks?.values?.map { block -> //wir mappen jeden block aus form.sections auf ein Building Block object
                        UiBlock(
                            block.label,
                            block.data_type,
                            block.required,
                            type = when (block.data_type.lowercase()) {//differentiation between the different field types
                                "STRING" -> FieldType.TEXT
                                "FLOAT" -> FieldType.NUMBER
                                "DATE" -> FieldType.DATE
                                else -> FieldType.TEXT
                            },null
                        )
                    } ?: emptyList()

            if (buildingBlocks != null) {
                for (block in buildingBlocks) { //allowing us to add blocks
                    if (block !in blocks) {
                        blocks.add(block) //the list we are using to create our UI
                    }
                }
            }



                _uiState.update {it.copy(blocks = buildingBlocks)} //update our blocks

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

