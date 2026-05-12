package com.farrasmuhammadrazan0100.assement2.ui.screen.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.farrasmuhammadrazan0100.assement2.model.ManhwaEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCharacterDialog(
    manhwaList: List<ManhwaEntity>,
    onDismiss: () -> Unit,
    onConfirm: (manhwaId: Int, charName: String, charRole: String) -> Unit
) {
    var charName by remember { mutableStateOf("") }
    var charRole by remember { mutableStateOf("") }
    var selectedManhwa by remember { mutableStateOf<ManhwaEntity?>(manhwaList.firstOrNull()) }
    var dropdownExpanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Tambah Karakter") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

                // Dropdown pilih Manhwa
                Text("Pilih Manhwa", style = MaterialTheme.typography.labelMedium)
                ExposedDropdownMenuBox(
                    expanded = dropdownExpanded,
                    onExpandedChange = { dropdownExpanded = !dropdownExpanded }
                ) {
                    OutlinedTextField(
                        value = selectedManhwa?.title ?: "Pilih Manhwa",
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropdownExpanded)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = dropdownExpanded,
                        onDismissRequest = { dropdownExpanded = false }
                    ) {
                        manhwaList.forEach { manhwa ->
                            DropdownMenuItem(
                                text = { Text(manhwa.title) },
                                onClick = {
                                    selectedManhwa = manhwa
                                    dropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                // Input nama karakter
                OutlinedTextField(
                    value = charName,
                    onValueChange = { charName = it },
                    label = { Text("Nama Karakter") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // Input role karakter
                OutlinedTextField(
                    value = charRole,
                    onValueChange = { charRole = it },
                    label = { Text("Role (Contoh: Protagonist)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (charName.isNotBlank() && charRole.isNotBlank() && selectedManhwa != null) {
                        onConfirm(selectedManhwa!!.id, charName.trim(), charRole.trim())
                    }
                }
            ) {
                Text("Tambah")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal")
            }
        }
    )
}