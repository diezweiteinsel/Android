package de.cau.inf.se.sopro.ui.submitApplication

import androidx.annotation.StringRes
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import de.cau.inf.se.sopro.R


@Composable
fun SubmitApplicationCategory(

    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit,
    selectedCategory: String,
    categories: List<String>
) {
    var expanded by remember { mutableStateOf(false) }
    Box(modifier = modifier) {
        Button(
            onClick = { expanded = true },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = selectedCategory.ifEmpty { stringResource(id = R.string.choose_form) }
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            categories.forEach { category ->
                DropdownMenuItem(
                    text = { Text(category) },
                    onClick = {
                        onValueChange(category)
                        expanded = false
                    }
                )
            }
        }
    }
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
        modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp),
    ) {
        items(blocks) { block -> //using items() to loop through the blocks and dynamically create a composable for each block
            when (block.type) {
                FieldType.TEXT -> OutlinedTextField(
                    value = values[block.label]
                        ?: "", //if the value is null, we want to return an empty string
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
                FieldType.DATE -> OutlinedTextField(
                    value = values[block.label] ?: "",
                    onValueChange = { },
                    label = { Text("DOB") },
                    placeholder = { Text("MM/DD/YYYY") },
                    modifier = modifier
                        .fillMaxWidth()
                        .pointerInput(values[block.label]) {
                            awaitEachGesture {
                                awaitFirstDown(pass = PointerEventPass.Initial)
                                val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                            }
                        }
                )
            }
        }
    }
}

@Composable
fun CancelButton(
    modifier : Modifier= Modifier,
    onClick: () -> Unit
) {
    ElevatedButton(modifier = modifier
        .padding(horizontal = 8.dp)
        .height(48.dp), onClick = { onClick() },
        shape = RoundedCornerShape(12.dp)) {
        Text(stringResource(R.string.cancel))
    }
}

@Composable
fun SubmitButton(
    modifier: Modifier= Modifier,
    onClick: () -> Unit,
    @StringRes textResId: Int = R.string.submit_application
){
    ElevatedButton(
        modifier = modifier
            .padding(horizontal = 8.dp)
            .height(48.dp),
        onClick = { onClick() },
        shape = RoundedCornerShape(12.dp)
    ){
        Text(stringResource(id = textResId))
    }
}

//all our dataclasses for the submit application screen
data class SubmitApplicationUiState( //this is our uiState, which we want to be a single source of truth
    val isLoading: Boolean = false,
    val values : Map<String, String> = emptyMap(), //userinput
    val errorMessage: String? = null,
    val blocks: List<UiBlock> = emptyList(), //the blocks of our form
    val categories: List<String> = emptyList(),
    val selectedCategory: String = ""
)

enum class FieldType { TEXT, NUMBER, DATE} //fieldtypes to generate
data class UiBlock( //this is how we define a block for now
    val label: String,
    val datatype: String,
    val required: Boolean,
    val type: FieldType,
    val constraintsJson: List<String>?
)

data class FieldPayload( //for our payload to submit the application
val label: String,
val value: String,
val data_type: String
)