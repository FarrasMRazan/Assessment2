package com.farrasmuhammadrazan0100.assement2.ui.screen.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.farrasmuhammadrazan0100.assement2.model.ManhwaEntity

@Composable
fun ManhwaItem(
    manhwa: ManhwaEntity,
    isGrid: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        if (isGrid) {
            Column {
                AsyncImage(
                    model = manhwa.imageUri,
                    contentDescription = null,
                    modifier = Modifier.height(180.dp).fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = manhwa.title,
                    modifier = Modifier.padding(8.dp),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        } else {
            Row(modifier = Modifier.padding(8.dp)) {
                AsyncImage(
                    model = manhwa.imageUri,
                    contentDescription = null,
                    modifier = Modifier.size(80.dp),
                    contentScale = ContentScale.Crop
                )
                Column(modifier = Modifier.padding(start = 16.dp)) {
                    Text(text = manhwa.title, style = MaterialTheme.typography.titleMedium)
                    Text(text = manhwa.author, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}