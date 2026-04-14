package com.joseleandro.fullfocus.ui.screen.pomodoro_setting

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.joseleandro.fullfocus.R
import com.joseleandro.fullfocus.ui.component.FullFocusBottomSheet
import com.joseleandro.fullfocus.ui.component.FullFocusBottomSheetHeader
import com.joseleandro.fullfocus.ui.event.PomodoroSettingEvent
import com.joseleandro.fullfocus.ui.screen.pomodoro_setting.component.TimerInput
import com.joseleandro.fullfocus.ui.screen.pomodoro_setting.component.WheelTimePickerDialog
import com.joseleandro.fullfocus.ui.state.PomodoroSettingUiState
import com.joseleandro.fullfocus.ui.state.TimerType
import com.joseleandro.fullfocus.ui.theme.FullFocusTheme
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PomodoroSettingBottomSheet(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    sheetState: SheetState
) {

    val viewModel: PomodoroSettingViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    PomodoroSettingBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        uiState = uiState,
        onEvent = viewModel::onEvent,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PomodoroSettingBottomSheet(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    sheetState: SheetState,
    uiState: PomodoroSettingUiState,
    onEvent: (PomodoroSettingEvent) -> Unit
) {


    FullFocusBottomSheet(
        modifier = modifier,
        header = {
            FullFocusBottomSheetHeader(
                title = {
                    Text(
                        text = "Ajustes do Timer",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                },
                trailingIcon = {
                    IconButton(
                        onClick = onDismissRequest
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_close_24),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface.copy(
                                alpha = .6f
                            )
                        )
                    }
                }
            )
        },
        sheetState = sheetState,
        onDismissRequest = onDismissRequest
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(.95f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(
                    space = 8.dp
                )
            ) {
                Icon(
                    modifier = Modifier.size(20.dp),
                    painter = painterResource(id = R.drawable.outline_access_time_24),
                    contentDescription = null
                )
                Text(
                    text = "Tempo",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Medium
                    )
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                TimerInput(
                    label = stringResource(R.string.label_pomodoro),
                    modifier = Modifier.weight(1f),
                    value = uiState.pomodoroTime.toString(),
                    onClick = { onEvent(PomodoroSettingEvent.OnTimerClick(TimerType.POMODORO)) }
                )

                TimerInput(
                    label = stringResource(R.string.label_pausa_curta),
                    modifier = Modifier.weight(1f),
                    value = uiState.shortBreakTime.toString(),
                    onClick = { onEvent(PomodoroSettingEvent.OnTimerClick(TimerType.SHORT_BREAK)) }
                )

                TimerInput(
                    label = stringResource(R.string.label_pausa_longa),
                    modifier = Modifier.weight(1f),
                    value = uiState.longBreakTime.toString(),
                    onClick = { onEvent(PomodoroSettingEvent.OnTimerClick(TimerType.LONG_BREAK)) }
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {

                ListItem(
                    modifier = Modifier.padding(horizontal = 0.dp),
                    headlineContent = {
                        Text(
                            text = "Auto-iniciar descanso",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                    },
                    supportingContent = {
                        Text(
                            text = "Inicia a pausa logo após o fim do foco",
                            style = MaterialTheme.typography.labelMedium
                        )
                    },
                    trailingContent = {
                        Switch(
                            checked = uiState.autoStartNextShortBreak,
                            onCheckedChange = {
                                onEvent(
                                    PomodoroSettingEvent.OnAutoStartShortBreakChange(
                                        it
                                    )
                                )
                            }
                        )
                    }
                )

                ListItem(
                    modifier = Modifier.padding(horizontal = 0.dp),
                    headlineContent = {
                        Text(
                            text = "Auto-iniciar foco",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                    },
                    supportingContent = {
                        Text(
                            text = "Inicia o foco logo após o fim da pausa",
                            style = MaterialTheme.typography.labelMedium
                        )
                    },
                    trailingContent = {
                        Switch(
                            checked = uiState.autoStartNextPomodoro,
                            onCheckedChange = {
                                onEvent(
                                    PomodoroSettingEvent.OnAutoStartPomodoroChange(
                                        it
                                    )
                                )
                            }
                        )
                    }
                )

                ListItem(
                    modifier = Modifier.padding(horizontal = 0.dp),
                    headlineContent = {
                        Text(
                            text = "Pausa longa após",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                    },
                    supportingContent = {
                        Text(
                            text = "Sessões de foco necessárias",
                            style = MaterialTheme.typography.labelMedium
                        )
                    },
                    trailingContent = {
                        TimerInputBox(
                            modifier = Modifier.width(110.dp),
                            value = "${uiState.longBreakInterval} sessões",
                            onClick = { onEvent(PomodoroSettingEvent.OnTimerClick(TimerType.LONG_BREAK_INTERVAL)) }
                        )
                    }
                )
            }
        }
    }

    if (uiState.showTimePicker) {
        WheelTimePickerDialog(
            initialValue = uiState.tempSelectedTime,
            onDismiss = { onEvent(PomodoroSettingEvent.OnDismissTimePicker) },
            onConfirm = { onEvent(PomodoroSettingEvent.OnTimerConfirm(it)) },
            title = when (uiState.selectedTimerType) {
                TimerType.POMODORO -> "Tempo de Foco"
                TimerType.SHORT_BREAK -> "Pausa Curta"
                TimerType.LONG_BREAK -> "Pausa Longa"
                TimerType.LONG_BREAK_INTERVAL -> "Intervalo de Pausa"
                null -> "Selecionar"
            }
        )
    }
}

@Composable
private fun TimerInputBox(
    modifier: Modifier = Modifier,
    value: String,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(shape = MaterialTheme.shapes.medium)
            .background(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = .05f),
                shape = MaterialTheme.shapes.medium
            )
            .height(48.dp)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.SemiBold
            )
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun PomodoroSettingBottomSheetLightPreview() {
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

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun PomodoroSettingBottomSheetDarkPreview() {
    FullFocusTheme(
        dynamicColor = false,
        darkTheme = true
    ) {
        PomodoroSettingBottomSheet(
            sheetState = rememberModalBottomSheetState(),
            onDismissRequest = {},
            uiState = PomodoroSettingUiState(),
            onEvent = {}
        )
    }
}