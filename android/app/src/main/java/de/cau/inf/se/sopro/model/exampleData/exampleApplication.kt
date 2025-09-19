package de.cau.inf.se.sopro.model.exampleData

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun ApplicationCard(modifier: Modifier = Modifier) {
    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = modifier
            .fillMaxWidth()
    )   {
        Text(text = "Hund",
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )
        Text(text = "5 Jahre",
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )
        Text(text = "Chihuahua",
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}