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
import androidx.compose.ui.res.stringResource
import com.joseleandro.fullfocus.R

@Composable
fun FullFocusWheelPickerDialog(
    title: String,
    items: List<String>,
    value: String? = null,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var tempValue by remember { mutableStateOf(value ?: items.firstOrNull() ?: "") }

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
                initialSelection = value,
                onItemSelected = { tempValue = it }
            )
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(tempValue) }) {
                Text(stringResource(R.string.confirmar))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancelar))
            }
        }
    )
}
