package com.farrasmuhammadrazan0100.assement2.ui.screen.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.farrasmuhammadrazan0100.assement2.R
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
        title = { Text(stringResource(R.string.dialog_add_character_title)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

                Text(
                    stringResource(R.string.label_pick_manhwa),
                    style = MaterialTheme.typography.labelMedium
                )

                ExposedDropdownMenuBox(
                    expanded = dropdownExpanded,
                    onExpandedChange = { dropdownExpanded = !dropdownExpanded }
                ) {
                    OutlinedTextField(
                        value = selectedManhwa?.title
                            ?: stringResource(R.string.label_pick_manhwa),
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropdownExpanded)
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
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

                OutlinedTextField(
                    value = charName,
                    onValueChange = { charName = it },
                    label = { Text(stringResource(R.string.label_char_name)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = charRole,
                    onValueChange = { charRole = it },
                    label = { Text(stringResource(R.string.label_char_role)) },
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
                Text(stringResource(R.string.btn_add))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.btn_cancel))
            }
        }
    )
}