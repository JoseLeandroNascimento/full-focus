package com.joseleandro.fullfocus.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun FullFocusWheelPickerDialog(
    title: String,
    items: List<String>,
    initialSelection: String? = null,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var tempValue by remember { mutableStateOf(initialSelection ?: items.firstOrNull() ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            FullFocusWheelPicker(
                modifier = Modifier.fillMaxWidth(),
                items = items,
                initialSelection = initialSelection,
                onItemSelected = { tempValue = it }
            )
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(tempValue) }) {
                Text("Confirmar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
