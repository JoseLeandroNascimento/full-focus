package com.joseleandro.fullfocus.ui.screen.pomodoro.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joseleandro.fullfocus.R
import com.joseleandro.fullfocus.ui.component.FullFocusDialog
import com.joseleandro.fullfocus.ui.theme.FullFocusTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmCancelSessionDialog(
    onDismissRequest: () -> Unit,
    onConfirm: (Boolean) -> Unit
) {
    var isFinished by remember { mutableStateOf(true) }

    FullFocusDialog(
        onDismissRequest = onDismissRequest,
        title = stringResource(R.string.encerrar_sessao)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.mensagem_confirmacao_cancelar_pomodoro),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            SingleChoiceSegmentedButtonRow(
                modifier = Modifier.fillMaxWidth()
            ) {
                SegmentedButton(
                    selected = isFinished,
                    onClick = { isFinished = true },
                    shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2),
                    label = {
                        Text(
                            text = "Concluída",
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                )
                SegmentedButton(
                    selected = !isFinished,
                    onClick = { isFinished = false },
                    shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2),
                    label = {
                        Text(
                            text = "Descartar",
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                )
            }

            Text(
                text = if (isFinished)
                    "A sessão será registrada no seu histórico de foco."
                else
                    "O progresso atual será perdido e não será contabilizado.",
                style = MaterialTheme.typography.bodySmall,
                color = if (isFinished) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.End),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = onDismissRequest) {
                    Text(
                        text = stringResource(R.string.nao),
                        style = MaterialTheme.typography.labelLarge
                    )
                }
                Button(
                    onClick = { onConfirm(isFinished) },
                    contentPadding = PaddingValues(horizontal = 32.dp)
                ) {
                    Text(
                        text = stringResource(R.string.sim),
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun ConfirmCancelSessionDialogLightPreview() {
    FullFocusTheme(
        dynamicColor = false,
        darkTheme = false
    ) {
        ConfirmCancelSessionDialog(
            onDismissRequest = {},
            onConfirm = {}
        )
    }
}

@Preview
@Composable
private fun ConfirmCancelSessionDialogDarkPreview() {
    FullFocusTheme(
        dynamicColor = false,
        darkTheme = true
    ) {
        ConfirmCancelSessionDialog(
            onDismissRequest = {},
            onConfirm = {}
        )
    }
}