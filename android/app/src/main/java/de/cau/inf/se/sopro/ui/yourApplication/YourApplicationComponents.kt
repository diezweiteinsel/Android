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

    val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm 'Uhr'")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = application.category,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Submitted: ${application.createdAt.format(dateFormatter)}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "Status: ${application.status}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )

            if (application.isPublic)
                Text(
                    text = "This application is public and visible to anyone",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )

            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

            if (application.dynamicAttributes.isNotEmpty()) {
                Text(
                    text = "More information:",
                    style = MaterialTheme.typography.labelMedium
                )

                application.dynamicAttributes.forEach { (key, value) ->
                    Text(text = "• $key: $value")
                }
            }
        }
    }
}