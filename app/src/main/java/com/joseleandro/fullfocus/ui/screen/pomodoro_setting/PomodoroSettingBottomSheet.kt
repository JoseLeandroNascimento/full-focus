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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.LocalLocale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.joseleandro.fullfocus.R
import com.joseleandro.fullfocus.ui.component.FullFocusInputWheelPicker
import com.joseleandro.fullfocus.ui.component.FullFocusWheelPickerDialog
import com.joseleandro.fullfocus.ui.event.PomodoroSettingEvent
import com.joseleandro.fullfocus.ui.state.PomodoroSettingModalUiState
import com.joseleandro.fullfocus.ui.state.PomodoroSettingUiState
import com.joseleandro.fullfocus.ui.theme.FullFocusTheme
import org.koin.compose.viewmodel.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PomodoroSettingBottomSheet(
    onDismissRequest: () -> Unit,
    sheetState: SheetState
) {

    val viewModel = koinViewModel<PomodoroSettingViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    PomodoroSettingBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        uiState = uiState,
        onEvent = viewModel::onEvent
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PomodoroSettingBottomSheet(
    onDismissRequest: () -> Unit,
    sheetState: SheetState,
    uiState: PomodoroSettingUiState,
    onEvent: (PomodoroSettingEvent) -> Unit
) {

    val isPreview = LocalInspectionMode.current

    when (uiState.modal) {
        PomodoroSettingModalUiState.FocusTimer -> {
            FullFocusWheelPickerDialog(
                title = stringResource(id = R.string.tempo_de_foco),
                value = uiState.focusTime.split(":")[0],
                items = (1..60).map {
                    String.format(LocalLocale.current.platformLocale, "%02d", it)
                },
                onConfirm = {
                    onEvent(PomodoroSettingEvent.UpdateFocusTime(time = it))
                },
                onDismiss = {
                    onEvent(PomodoroSettingEvent.CloseModal)
                }
            )
        }

        PomodoroSettingModalUiState.LongBreakTimer -> {
            FullFocusWheelPickerDialog(
                title = stringResource(id = R.string.pausa_longa),
                value = uiState.longBreakTime.split(":")[0],
                items = (1..60).map {
                    String.format(LocalLocale.current.platformLocale, "%02d", it)
                },
                onConfirm = {
                    onEvent(PomodoroSettingEvent.UpdateLongBreakTime(time = it))
                },
                onDismiss = {
                    onEvent(PomodoroSettingEvent.CloseModal)
                }
            )
        }

        PomodoroSettingModalUiState.ShortBreakTimer -> {
            FullFocusWheelPickerDialog(
                title = stringResource(id = R.string.pausa_curta),
                value = uiState.shortBreakTime.split(":")[0],
                items = (1..60).map {
                    String.format(LocalLocale.current.platformLocale, "%02d", it)
                },
                onConfirm = {
                    onEvent(PomodoroSettingEvent.UpdateShortBreakTime(time = it))
                },
                onDismiss = {
                    onEvent(PomodoroSettingEvent.CloseModal)
                }
            )
        }

        PomodoroSettingModalUiState.None -> {}
    }

    if (isPreview) {
        PomodoroSettingBottomSheetContent(
            uiState = uiState,
            onEvent = onEvent
        )
    } else {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = onDismissRequest
        ) {
            PomodoroSettingBottomSheetContent(
                uiState = uiState,
                onEvent = onEvent
            )
        }
    }

}

@Composable
private fun PomodoroSettingBottomSheetContent(
    modifier: Modifier = Modifier,
    uiState: PomodoroSettingUiState,
    onEvent: (PomodoroSettingEvent) -> Unit
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.configurar_pomodoro),
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.configura_es_de_tempo),
                style = MaterialTheme.typography.bodySmall
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                FullFocusInputWheelPicker(
                    modifier = Modifier.weight(1f),
                    label = "Foco",
                    value = uiState.focusTime,
                    onShowPicker = {
                        onEvent(PomodoroSettingEvent.ShowModal(modal = PomodoroSettingModalUiState.FocusTimer))
                    }
                )

                FullFocusInputWheelPicker(
                    modifier = Modifier.weight(1f),
                    label = "Pausa curta",
                    value = uiState.shortBreakTime,
                    onShowPicker = {
                        onEvent(PomodoroSettingEvent.ShowModal(modal = PomodoroSettingModalUiState.ShortBreakTimer))
                    }
                )

                FullFocusInputWheelPicker(
                    modifier = Modifier.weight(1f),
                    label = "Pausa longa",
                    value = uiState.longBreakTime,
                    onShowPicker = {
                        onEvent(PomodoroSettingEvent.ShowModal(modal = PomodoroSettingModalUiState.LongBreakTimer))
                    }
                )
            }
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Button(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            onClick = {
                onEvent(PomodoroSettingEvent.OnSave)
            }
        ) {
            Text(
                text = stringResource(id = R.string.salvar),
                style = MaterialTheme.typography.labelLarge
            )
        }
        Spacer(
            modifier = Modifier.height(32.dp)
        )
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
            sheetState = rememberModalBottomSheetState(),
            onDismissRequest = { },
            uiState = PomodoroSettingUiState(),
            onEvent = {}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun PomodoroSettingBottomSheetDarkPreview() {
    FullFocusTheme(
        dynamicColor = false,
        darkTheme = false
    ) {
        PomodoroSettingBottomSheet(
            sheetState = rememberModalBottomSheetState(),
            onDismissRequest = {},
            uiState = PomodoroSettingUiState(),
            onEvent = {}
        )
    }
}