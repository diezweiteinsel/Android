package de.cau.inf.se.sopro.ui.yourApplication

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import de.cau.inf.se.sopro.model.application.Application
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ApplicationCard(application: Application) {
    // Der Formatter, um das Datum schön anzuzeigen
    val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm 'Uhr'")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp) // Abstand zwischen Elementen
        ) {
            // Feste Attribute anzeigen
            Text(
                text = "Antrag von: ${application.applicantName}",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Eingereicht am: ${application.createdAt.format(dateFormatter)}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "Status: ${application.status}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )

            // Trennlinie vor den dynamischen Attributen
            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

            // Dynamische Attribute anzeigen, falls vorhanden
            if (application.dynamicAttributes.isNotEmpty()) {
                Text(
                    text = "Zusätzliche Informationen:",
                    style = MaterialTheme.typography.labelMedium
                )
                // Durch die Map iterieren und jedes Attribut anzeigen
                application.dynamicAttributes.forEach { (key, value) ->
                    Text(text = "• $key: $value")
                }
            }
        }
    }
}