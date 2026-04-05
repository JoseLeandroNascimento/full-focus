package com.joseleandro.fullfocus.ui.screen.create_task

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joseleandro.fullfocus.R
import com.joseleandro.fullfocus.ui.component.FullFocusBottomSheet
import com.joseleandro.fullfocus.ui.component.FullFocusBottomSheetHeader
import com.joseleandro.fullfocus.ui.screen.create_task.component.PomodoroNumberSelect
import com.joseleandro.fullfocus.ui.theme.FullFocusTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTaskBottomSheet(
    sheetState: SheetState,
    onDismissRequest: () -> Unit
) {

    var pomoNumber by remember { mutableStateOf<Int?>(null) }

    FullFocusBottomSheet(
        sheetState = sheetState,
        header = {
            FullFocusBottomSheetHeader(
                leadingIcon = {
                    IconButton(
                        onClick = onDismissRequest
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_close_24),
                            contentDescription = null
                        )
                    }
                },
                title = {
                    Text(
                        text = stringResource(R.string.nova_tarefa),
                    )
                },
                trailingIcon = {
                    TextButton(
                        onClick = {}
                    ) {
                        Text(
                            text = "Salvar",
                            style = MaterialTheme.typography.labelLarge
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            modifier = Modifier.size(20.dp),
                            painter = painterResource(id = R.drawable.baseline_check_24),
                            contentDescription = null
                        )
                    }
                }
            )
        },
        onDismissRequest = onDismissRequest
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = "",
                label = {
                    Text(
                        text = "Nome da tarefa",
                        style = MaterialTheme.typography.labelLarge.copy(
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = .4f),
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                },
                placeholder = {
                    Text(
                        text = "Ex: Estudar, Trabalhar...",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = .4f),
                            fontWeight = FontWeight.Medium
                        )
                    )
                },
                maxLines = 1,
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = .2f),
                    focusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = .5f)
                ),
                onValueChange = {}
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                Text(
                    text = "Número de pomodoros",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = .4f),
                        fontWeight = FontWeight.SemiBold
                    )
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    (1..8).forEach { numberPomo ->

                        PomodoroNumberSelect(
                            label = numberPomo.toString(),
                            selected = pomoNumber?.let { it == numberPomo } ?: false,
                            onClick = {
                                pomoNumber = numberPomo
                            }
                        )
                        
                    }
                }

            }

        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun CreateTaskBottomSheetLightPreview() {
    FullFocusTheme(
        dynamicColor = false,
        darkTheme = false
    ) {
        CreateTaskBottomSheet(
            sheetState = rememberModalBottomSheetState(),
            onDismissRequest = {}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun CreateTaskBottomSheetDarkPreview() {
    FullFocusTheme(
        dynamicColor = false,
        darkTheme = true
    ) {
        CreateTaskBottomSheet(
            sheetState = rememberModalBottomSheetState(),
            onDismissRequest = {}
        )
    }
}