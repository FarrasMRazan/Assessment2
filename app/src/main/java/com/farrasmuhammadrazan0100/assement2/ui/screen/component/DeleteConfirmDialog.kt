package com.farrasmuhammadrazan0100.assement2.ui.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun DeleteConfirmDialog(
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(text = "Hapus Manhwa?")
        },
        text = {
            Text(text = "Data yang sudah dihapus tidak dapat dikembalikan. Apakah Anda yakin?")
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(text = "Hapus")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = "Batal")
            }
        }
    )
}