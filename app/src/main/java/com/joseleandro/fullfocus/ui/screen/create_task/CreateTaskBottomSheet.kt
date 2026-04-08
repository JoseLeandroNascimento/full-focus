package com.joseleandro.fullfocus.ui.screen.create_task

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.joseleandro.fullfocus.R
import com.joseleandro.fullfocus.ui.component.FullFocusBottomSheet
import com.joseleandro.fullfocus.ui.component.FullFocusBottomSheetHeader
import com.joseleandro.fullfocus.ui.component.FullFocusTextField
import com.joseleandro.fullfocus.ui.event.CreateTaskEvent
import com.joseleandro.fullfocus.ui.screen.create_task.component.PomodoroNumberSelect
import com.joseleandro.fullfocus.ui.state.CreateTaskUiState
import com.joseleandro.fullfocus.ui.theme.FullFocusTheme
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTaskBottomSheet(
    sheetState: SheetState,
    onDismissRequest: () -> Unit
) {

    val viewModel: CreateTaskViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CreateTaskBottomSheet(
        sheetState = sheetState,
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onDismissRequest = onDismissRequest
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTaskBottomSheet(
    sheetState: SheetState,
    uiState: CreateTaskUiState,
    onEvent: (CreateTaskEvent) -> Unit,
    onDismissRequest: () -> Unit
) {

    val titleFocus = remember {
        FocusRequester()
    }

    LaunchedEffect(Unit) {
        snapshotFlow { sheetState.isVisible }
            .filter { it }
            .collectLatest {
                titleFocus.requestFocus()
            }
    }

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
                        onClick = {
                            onEvent(
                                CreateTaskEvent.OnSave {
                                    onDismissRequest()
                                }
                            )
                        }
                    ) {
                        Text(
                            text = stringResource(id = R.string.salvar),
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

            FullFocusTextField(
                modifier = Modifier.focusRequester(titleFocus),
                value = uiState.form.title.value,
                error = uiState.form.title.messageError?.getMessage(),
                onValueChange = {
                    onEvent(
                        CreateTaskEvent.OnTitleChange(
                            value = it
                        )
                    )
                },
                label = stringResource(R.string.nome_da_tarefa)
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                Text(
                    text = stringResource(R.string.n_mero_de_pomodoros),
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
                            selected = uiState.form.pomodoros.value == numberPomo,
                            onClick = {
                                onEvent(CreateTaskEvent.OnPomodorosChange(value = numberPomo))
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
            uiState = CreateTaskUiState(),
            onEvent = {},
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
            uiState = CreateTaskUiState(),
            onEvent = {},
            onDismissRequest = {}
        )
    }
}