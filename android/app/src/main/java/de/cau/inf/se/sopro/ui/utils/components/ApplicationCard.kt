package de.cau.inf.se.sopro.ui.utils.components

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.gson.Gson
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


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ApplicationCard(
    application: Application,
    formName: String,
    displayMode: CardDisplayMode = CardDisplayMode.StatusColor,
    showPublicStatusIndicator: Boolean = true
) {

    var isExpanded by remember { mutableStateOf(false) }

    val formattedDate = rememberFormattedDate(application.createdAt)
    val cardColor = rememberApplicationCardColor(displayMode, application.status)

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
                text = formName
            )
            Text(
                text = "Submitted: $formattedDate",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "Status: ${application.status}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )

            if (showPublicStatusIndicator && application.isPublic)
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

enum class CardDisplayMode {
    StatusColor,
    Public
}

@Composable
private fun rememberApplicationCardColor(
    displayMode: CardDisplayMode,
    status: Status?
): Color {
    val isDark = isSystemInDarkTheme()
    val publicColor = MaterialTheme.colorScheme.primaryContainer

    return remember(displayMode, status, isDark, publicColor) {
        if (displayMode == CardDisplayMode.Public) {
            publicColor
        } else {
            when (status) {
                Status.APPROVED -> if (isDark) StatusApprovedDark else StatusApprovedLight
                Status.REJECTED -> if (isDark) StatusRejectedDark else StatusRejectedLight
                Status.PENDING -> if (isDark) StatusPendingDark else StatusPendingLight
                null -> if (isDark) StatusPendingDark else StatusPendingLight
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun rememberFormattedDate(createdAt: String?): String {
    val dateFormatter = remember { DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm 'Uhr'") }
    return remember(createdAt) {
        if (createdAt == null) {
            "N/A"
        } else {
            try {
                val dateTime = LocalDateTime.parse(createdAt)
                dateTime.format(dateFormatter)
            } catch (e: DateTimeParseException) {
                createdAt
            }
        }
    }
}