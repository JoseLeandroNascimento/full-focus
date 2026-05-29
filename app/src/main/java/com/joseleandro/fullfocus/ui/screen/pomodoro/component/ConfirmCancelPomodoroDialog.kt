package com.joseleandro.fullfocus.ui.screen.pomodoro.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.joseleandro.fullfocus.R

@Composable
fun ConfirmCancelPomodoroDialog(
    onDismissRequest: () -> Unit,
    onDiscard: () -> Unit,
    onSaveProgress: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(text = stringResource(R.string.cancelar_pomodoro)) },
        text = { Text(text = stringResource(R.string.escolha_como_deseja_cancelar)) },
        confirmButton = {
            TextButton(
                onClick = onSaveProgress
            ) {
                Text(text = stringResource(R.string.salvar_progresso))
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDiscard
            ) {
                Text(
                    text = stringResource(R.string.descartar),
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    )
}