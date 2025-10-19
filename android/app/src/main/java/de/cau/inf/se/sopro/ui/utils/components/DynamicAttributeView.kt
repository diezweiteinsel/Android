package de.cau.inf.se.sopro.ui.utils.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName
import java.util.Locale

@Composable
fun DynamicAttributeView(attribute: DynamicAttribute) {
    val valueAsString = if (attribute.value == null || attribute.value.isJsonNull) {
        "N/A"
    } else if (attribute.value.isJsonPrimitive) {
        attribute.value.asString
    } else {
        attribute.value.toString()
    }

    val formattedLabel = attribute.label.replace('_', ' ').replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$formattedLabel:",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(0.2f)
        )
        Text(
            text = valueAsString,
            modifier = Modifier.weight(0.8f)
        )
    }
}

data class DynamicAttribute(
    @SerializedName("label")
    val label: String,

    @SerializedName("value")
    val value: JsonElement
)