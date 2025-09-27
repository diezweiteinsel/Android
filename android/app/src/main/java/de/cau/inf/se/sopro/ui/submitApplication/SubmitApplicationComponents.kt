package de.cau.inf.se.sopro.ui.submitApplication

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import de.cau.inf.se.sopro.R
import de.cau.inf.se.sopro.ui.utils.DynamicSelectTextField

@Composable
fun SubmitApplicationCategory(
    modifier: Modifier = Modifier
) {

    val categories = Suggestion.CATEGORIES

    //this is here to remember the state of our textfield
    var searchQuery by remember { mutableStateOf("") }

    //The text field for the categories for the applications
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