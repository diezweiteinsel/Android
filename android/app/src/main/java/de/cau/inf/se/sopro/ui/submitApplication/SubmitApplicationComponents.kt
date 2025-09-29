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
import androidx.compose.material3.ElevatedButton
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
import androidx.compose.ui.unit.dp
import de.cau.inf.se.sopro.R
import de.cau.inf.se.sopro.ui.utils.DynamicSelectTextField

@Composable
fun SubmitApplicationCategory(
    modifier: Modifier = Modifier
) {



    //The text field for the categories for the applications

}
@Composable
fun SubmitApplicationForm(
    modifier: Modifier = Modifier
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

@Composable
fun DynamicForm(
    modifier: Modifier = Modifier,
    blocks: List<Block>,
    values: Map<String, String>,
    onValueChange: (String, String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(blocks) { block -> //using items() to loop through the blocks and dynamically create a composable for each block
            when (block.type) {
                FieldType.TEXT -> OutlinedTextField(
                    value = values[block.id] ?: "", //if the value is null, we want to return an empty string
                    onValueChange = { onValueChange(block.id, it) },
                    label = { Text(block.label) },
                    modifier = Modifier.fillMaxWidth()
                )
                FieldType.NUMBER -> OutlinedTextField(
                    value = values[block.id] ?: "",
                    onValueChange = { onValueChange(block.id, it) },
                    label = { Text(block.label) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
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
    val blocks: List<Block> = emptyList() //the blocks of our form
)

    enum class FieldType { TEXT, NUMBER}
    data class Block( //this is how we define a block for now
        val id: String,
        val label: String,
        val type: FieldType,
    )

    val blocks = mutableListOf(
        Block("Vorname", "Vorname", FieldType.TEXT),
        Block("Nachname", "Nachname", FieldType.TEXT),
        Block("E-Mail", "E-Mail", FieldType.TEXT),
        Block("Telefonnummer", "Telefonnummer", FieldType.TEXT)
    )