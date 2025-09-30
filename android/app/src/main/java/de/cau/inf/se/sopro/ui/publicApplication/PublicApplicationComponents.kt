package de.cau.inf.se.sopro.ui.publicApplication

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.unit.dp
import de.cau.inf.se.sopro.model.application.Application
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PublicApplicationCard(application: Application) {

    var isExpanded by remember { mutableStateOf(false) }

    val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm 'Uhr'")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { isExpanded = !isExpanded },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            /*
            Text(
                text = application.category,
                style = MaterialTheme.typography.titleMedium
            )

             */
            Text(
                text = "Submitted: ${application.createdAt.format(dateFormatter)}",
                style = MaterialTheme.typography.bodySmall
            )


            /*
            Text(
                text = "Wow! Look at that ${application.category}!",
                style = MaterialTheme.typography.bodyMedium
            )

             */


            if (application.dynamicAttributes.isNotEmpty()) {

                AnimatedVisibility(visible = isExpanded) {
                    Column {
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

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
    }
}