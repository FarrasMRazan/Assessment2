package com.farrasmuhammadrazan0100.assement2.ui.screen.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.farrasmuhammadrazan0100.assement2.R

@Composable
fun ShiroRecommendDialog(
    isLoading: Boolean,
    onDismiss: () -> Unit,
    onFetchRandom: () -> Unit,
    onFetchByCategory: (String) -> Unit
) {
    val categories = listOf("action", "fantasy", "comedy", "over-powered", "isekai", "romance")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.dialog_recommend_title)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = stringResource(R.string.dialog_recommend_desc),
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = stringResource(R.string.label_pick_category),
                    style = MaterialTheme.typography.labelMedium
                )
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(categories) { category ->
                        FilterChip(
                            selected = false,
                            onClick = { if (!isLoading) onFetchByCategory(category) },
                            label = { Text(category.replaceFirstChar { it.uppercase() }) }
                        )
                    }
                }
                if (isLoading) {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { if (!isLoading) onFetchRandom() }, enabled = !isLoading) {
                Text(stringResource(R.string.btn_random))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.btn_cancel))
            }
        }
    )
}