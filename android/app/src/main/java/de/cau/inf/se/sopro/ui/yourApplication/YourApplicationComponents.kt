package de.cau.inf.se.sopro.ui.yourApplication

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName
import de.cau.inf.se.sopro.model.application.Application
import de.cau.inf.se.sopro.model.application.Status
import de.cau.inf.se.sopro.ui.theme.StatusApprovedDark
import de.cau.inf.se.sopro.ui.theme.StatusApprovedLight
import de.cau.inf.se.sopro.ui.theme.StatusPendingDark
import de.cau.inf.se.sopro.ui.theme.StatusPendingLight
import de.cau.inf.se.sopro.ui.theme.StatusRejectedDark
import de.cau.inf.se.sopro.ui.theme.StatusRejectedLight
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale

enum class CardDisplayMode {
    StatusColor,
    Public
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ApplicationCard(
    application: Application,
    displayMode: CardDisplayMode = CardDisplayMode.StatusColor
) {

    var isExpanded by remember { mutableStateOf(false) }

    val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm 'Uhr'")

    val formattedDate = remember(application.createdAt) {
        if (application.createdAt == null) {
            "N/A" // Fallback, if the date is null
        } else {
            try {
                val dateTime = LocalDateTime.parse(application.createdAt)
                dateTime.format(dateFormatter)
            } catch (e: DateTimeParseException) {
                application.createdAt
            }
        }
    }

    val isDark = isSystemInDarkTheme()

    val cardColor = if (displayMode == CardDisplayMode.Public) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        when (application.status) {
            Status.APPROVED -> if (isDark) StatusApprovedDark else StatusApprovedLight
            Status.REJECTED -> if (isDark) StatusRejectedDark else StatusRejectedLight
            Status.PENDING -> if (isDark) StatusPendingDark else StatusPendingLight
            null -> if (isDark) StatusPendingDark else StatusPendingLight
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { isExpanded = !isExpanded },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = cardColor
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Submitted: $formattedDate",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "Status: ${application.status}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )

            if (application.isPublic)
                Text(
                    text = "This application is public and visible to anyone.",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )


            if (application.dynamicAttributes?.isNotEmpty() == true) {

                AnimatedVisibility(visible = isExpanded) {
                    Column {
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                        Text(
                            text = "More information:",
                            style = MaterialTheme.typography.labelMedium
                        )

                        val parsedAttributes = remember(application.dynamicAttributes) {
                            val gson = Gson()
                            application.dynamicAttributes.entries
                                .sortedBy { it.key.toIntOrNull() ?: 0 }
                                .map { entry ->
                                    try {
                                        gson.fromJson(entry.value, DynamicAttribute::class.java)
                                    } catch (e: Exception) {
                                        Log.e("UI", "Failed to parse dynamic attribute: ${entry.value}", e)
                                        null
                                    }
                                }
                        }

                        parsedAttributes.forEach { attribute ->
                            if (attribute != null) {
                                DynamicAttributeView(attribute = attribute)
                            } else {
                                Text(
                                    text = "Error: Could not parse dynamic attribute.",
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

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