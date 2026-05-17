package com.joseleandro.fullfocus.ui.screen.pomodoro_setting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.joseleandro.fullfocus.R
import com.joseleandro.fullfocus.ui.component.FullFocusInputWheelPicker
import com.joseleandro.fullfocus.ui.component.FullFocusWheelPickerDialog
import com.joseleandro.fullfocus.ui.theme.FullFocusTheme
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PomodoroSettingBottomSheet(
    modifier: Modifier = Modifier,
    viewModel: PomodoroSettingViewModel = koinViewModel(),
    onDismissRequest: () -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    PomodoroSettingBottomSheet(
        modifier = modifier,
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        onSave = {
            viewModel.onEvent(PomodoroSettingEvent.OnSave)
            scope.launch {
                sheetState.hide()
            }.invokeOnCompletion {
                if (!sheetState.isVisible) {
                    onDismissRequest()
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PomodoroSettingBottomSheet(
    modifier: Modifier = Modifier,
    uiState: PomodoroSettingUiState,
    onEvent: (PomodoroSettingEvent) -> Unit,
    onDismissRequest: () -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState(),
    onSave: () -> Unit = {}
) {
    val isPreview = LocalInspectionMode.current

    if (uiState.showFocusPicker) {
        FullFocusWheelPickerDialog(
            title = stringResource(R.string.tempo_de_foco),
            items = uiState.focusItems,
            initialSelection = uiState.focusTime,
            onDismiss = { onEvent(PomodoroSettingEvent.OnDismissPicker) },
            onConfirm = {
                onEvent(PomodoroSettingEvent.OnFocusTimeChange(it))
            }
        )
    }

    if (uiState.showShortPausePicker) {
        FullFocusWheelPickerDialog(
            title = "Pausa Curta",
            items = uiState.shortPauseItems,
            initialSelection = uiState.shortPauseTime,
            onDismiss = { onEvent(PomodoroSettingEvent.OnDismissPicker) },
            onConfirm = {
                onEvent(PomodoroSettingEvent.OnShortPauseTimeChange(it))
            }
        )
    }

    if (uiState.showLongPausePicker) {
        FullFocusWheelPickerDialog(
            title = "Pausa Longa",
            items = uiState.longPauseItems,
            initialSelection = uiState.longPauseTime,
            onDismiss = { onEvent(PomodoroSettingEvent.OnDismissPicker) },
            onConfirm = {
                onEvent(PomodoroSettingEvent.OnLongPauseTimeChange(it))
            }
        )
    }

    if (isPreview) {
        PomodoroSettingBottomSheetContent(
            modifier = Modifier.padding(16.dp),
            focoValue = uiState.focusTime,
            pausaCurtaValue = uiState.shortPauseTime,
            pausaLongaValue = uiState.longPauseTime,
            onFocoClick = { onEvent(PomodoroSettingEvent.OnShowPicker(PickerType.FOCUS)) },
            onPausaCurtaClick = { onEvent(PomodoroSettingEvent.OnShowPicker(PickerType.SHORT_PAUSE)) },
            onPausaLongaClick = { onEvent(PomodoroSettingEvent.OnShowPicker(PickerType.LONG_PAUSE)) },
            onSaveClick = onSave
        )
    } else {
        ModalBottomSheet(
            modifier = modifier,
            onDismissRequest = onDismissRequest,
            sheetState = sheetState
        ) {
            PomodoroSettingBottomSheetContent(
                modifier = Modifier.padding(16.dp),
                focoValue = uiState.focusTime,
                pausaCurtaValue = uiState.shortPauseTime,
                pausaLongaValue = uiState.longPauseTime,
                onFocoClick = { onEvent(PomodoroSettingEvent.OnShowPicker(PickerType.FOCUS)) },
                onPausaCurtaClick = { onEvent(PomodoroSettingEvent.OnShowPicker(PickerType.SHORT_PAUSE)) },
                onPausaLongaClick = { onEvent(PomodoroSettingEvent.OnShowPicker(PickerType.LONG_PAUSE)) },
                onSaveClick = onSave
            )
        }
    }
}

@Composable
private fun PomodoroSettingBottomSheetContent(
    modifier: Modifier = Modifier,
    focoValue: String,
    pausaCurtaValue: String,
    pausaLongaValue: String,
    onFocoClick: () -> Unit,
    onPausaCurtaClick: () -> Unit,
    onPausaLongaClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(R.string.configura_es_de_tempo),
            style = MaterialTheme.typography.titleSmall
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            FullFocusInputWheelPicker(
                modifier = Modifier.weight(1f),
                label = "Foco",
                value = focoValue,
                onShowPicker = onFocoClick
            )

            FullFocusInputWheelPicker(
                modifier = Modifier.weight(1f),
                label = "Pausa curta",
                value = pausaCurtaValue,
                onShowPicker = onPausaCurtaClick
            )

            FullFocusInputWheelPicker(
                modifier = Modifier.weight(1f),
                label = "Pausa longa",
                value = pausaLongaValue,
                onShowPicker = onPausaLongaClick
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            onClick = onSaveClick,
        ) {
            Text(
                text = stringResource(R.string.salvar),
                style = MaterialTheme.typography.labelMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun PomodoroSettingBottomSheetLightPreview() {

    FullFocusTheme(
        dynamicColor = false,
        darkTheme = false
    ) {
        PomodoroSettingBottomSheet(
            uiState = PomodoroSettingUiState(),
            onEvent = {},
            onDismissRequest = {}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun PomodoroSettingBottomSheetDarkPreview() {

    FullFocusTheme(
        dynamicColor = false,
        darkTheme = true
    ) {
        PomodoroSettingBottomSheet(
            uiState = PomodoroSettingUiState(),
            onEvent = {},
            onDismissRequest = {}
        )
    }
}
