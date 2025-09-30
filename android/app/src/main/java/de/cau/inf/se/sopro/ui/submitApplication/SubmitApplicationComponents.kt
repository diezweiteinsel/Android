package de.cau.inf.se.sopro.ui.submitApplication

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.room.ColumnInfo
import de.cau.inf.se.sopro.R
import de.cau.inf.se.sopro.ui.utils.DynamicSelectTextField
import kotlinx.serialization.json.JsonObject

@Composable
fun SubmitApplicationCategory(
    modifier: Modifier = Modifier
) {



    //The text field for the categories for the applications

}
@Composable
fun SubmitApplicationForm(
    modifier: Modifier = Modifier,
    onValueChange: () -> Unit
) {
    val categories = Suggestion.CATEGORIES
    //this is here to remember the state of our textfield
    var searchQuery by remember { mutableStateOf("") }
    DynamicSelectTextField(
        //we have to declare all these attributes every time we want a new DynamicSelectTextField
        searchQuery = searchQuery,
        { query -> searchQuery = query },
        { option ->
            searchQuery = option
        },
        categories,
        "Category",
        modifier = modifier.then(
            Modifier
                .padding(dimensionResource(id = R.dimen.padding_small))
        )
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DynamicForm(
    modifier: Modifier = Modifier,
    blocks: List<UiBlock>,
    values: Map<String, String>,
    onValueChange: (String, String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        items(blocks) { block -> //using items() to loop through the blocks and dynamically create a composable for each block
            when (block.type) {
                FieldType.TEXT -> OutlinedTextField(
                    value = values[block.label] ?: "", //if the value is null, we want to return an empty string
                    onValueChange = { onValueChange(block.label, it) },
                    label = { Text(block.label) },
                    modifier = Modifier.fillMaxWidth()
                )
                FieldType.NUMBER -> OutlinedTextField(
                    value = values[block.label] ?: "",
                    onValueChange = { onValueChange(block.label, it) },
                    label = { Text(block.label) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
               /* FieldType.DATE -> DatePicker(
                    state = TODO(),
                    modifier = TODO(),
                    dateFormatter = TODO(),
                    title = TODO(),
                    headline = TODO(),
                    showModeToggle = TODO(),
                    colors = TODO()
                )*/
            }
            Spacer(Modifier.height(16.dp))
        }
    }
}
@Composable
fun CancelButton( // Renamed to follow Composable naming conventions (PascalCase)
    modifier: Modifier = Modifier, // Provide a default Modifier
    onClick: () -> Unit
) {
    ElevatedButton(onClick = { onClick() }) {
        Text(stringResource(R.string.cancel))
    }
}

@Composable
fun submitButton(modifier: Modifier= Modifier, onClick: () -> Unit){
    ElevatedButton(onClick = { onClick() }) {
        Text(stringResource(R.string.submit_application))
    }
}

data class SubmitApplicationUiState( //this is our uiState, which we want to be a single source of truth
    val isLoading: Boolean = false,
    val values : Map<String, String> = emptyMap(), //userinput
    val errorMessage: String? = null,
    val blocks: List<UiBlock> = emptyList() //the blocks of our form
)

    enum class FieldType { TEXT, NUMBER} //DATE
    data class UiBlock( //this is how we define a block for now
        val label: String,
        val datatype: String,
        val required: Boolean,
        val type: FieldType,
        val constraintsJson: List<String>?
    )

    var blocks = mutableListOf(UiBlock("Vorname", "STRING",  true,FieldType.TEXT, emptyList()),
        UiBlock("Nachname", "STRING", true,FieldType.TEXT,constraintsJson = emptyList()),
        UiBlock("E-Mail", "STRING", false,FieldType.TEXT, constraintsJson = emptyList()))