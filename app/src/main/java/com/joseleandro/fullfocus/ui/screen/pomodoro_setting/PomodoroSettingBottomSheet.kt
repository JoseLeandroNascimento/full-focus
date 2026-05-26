package com.joseleandro.fullfocus.ui.screen.pomodoro_setting

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.LocalLocale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.joseleandro.fullfocus.R
import com.joseleandro.fullfocus.ui.component.FullFocusInputWheelPicker
import com.joseleandro.fullfocus.ui.component.FullFocusWheelPickerDialog
import com.joseleandro.fullfocus.ui.effect.PomodoroSettingEffect
import com.joseleandro.fullfocus.ui.event.PomodoroSettingEvent
import com.joseleandro.fullfocus.ui.event.PomodoroSettingEvent.UpdateFocusTime
import com.joseleandro.fullfocus.ui.event.PomodoroSettingEvent.UpdateLongBreakTime
import com.joseleandro.fullfocus.ui.event.PomodoroSettingEvent.UpdateShortBreakTime
import com.joseleandro.fullfocus.ui.screen.pomodoro_setting.component.PickerColorDialog
import com.joseleandro.fullfocus.ui.screen.pomodoro_setting.component.PickerColorType
import com.joseleandro.fullfocus.ui.state.PomodoroSettingModalUiState
import com.joseleandro.fullfocus.ui.state.PomodoroSettingUiState
import com.joseleandro.fullfocus.ui.theme.FullFocusTheme
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PomodoroSettingBottomSheet(
    onDismissRequest: () -> Unit,
    sheetState: SheetState
) {

    val viewModel = koinViewModel<PomodoroSettingViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {

        viewModel.onEvent(event = PomodoroSettingEvent.LoadData)

        viewModel.effect.collect { effect ->
            when (effect) {
                PomodoroSettingEffect.CloseBottomSheet -> onDismissRequest()
            }
        }
    }


    PomodoroSettingBottomSheet(
        onDismissRequest = {
            scope.launch {
                sheetState.hide()
            }.invokeOnCompletion {
                if (!sheetState.isVisible) {
                    onDismissRequest()
                }
            }
        },
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
                    onEvent(UpdateFocusTime(time = it))
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
                    onEvent(UpdateLongBreakTime(time = it))
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
                    onEvent(UpdateShortBreakTime(time = it))
                },
                onDismiss = {
                    onEvent(PomodoroSettingEvent.CloseModal)
                }
            )
        }

        is PomodoroSettingModalUiState.PickerColor -> {

            PickerColorDialog(
                type = uiState.modal.type,
                onConfirm = { newColor ->
                    when (uiState.modal.type) {
                        PickerColorType.FOCUS_PICKER_COLOR -> onEvent(
                            PomodoroSettingEvent.UpdateFocusProgressColor(
                                color = newColor
                            )
                        )

                        PickerColorType.SHORT_BREAK_PICKER_COLOR -> onEvent(
                            PomodoroSettingEvent.UpdateShortBreakProgressColor(
                                color = newColor
                            )
                        )

                        PickerColorType.LONG_BREAK_PICKER_COLOR -> onEvent(
                            PomodoroSettingEvent.UpdateLongBreakProgressColor(
                                color = newColor
                            )
                        )
                    }
                },
                onCancel = {
                    onEvent(PomodoroSettingEvent.CloseModal)
                },
                onDismissRequest = {
                    onEvent(PomodoroSettingEvent.CloseModal)
                },
                color = uiState.modal.color
            )
        }

        PomodoroSettingModalUiState.None -> {}
    }

    if (isPreview) {
        PomodoroSettingBottomSheetContent(
            uiState = uiState,
            onEvent = onEvent,
            onDismissRequest = onDismissRequest
        )
    } else {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = onDismissRequest
        ) {
            PomodoroSettingBottomSheetContent(
                uiState = uiState,
                onEvent = onEvent,
                onDismissRequest = onDismissRequest
            )
        }
    }

}


@Composable
private fun PomodoroSettingBottomSheetContent(
    modifier: Modifier = Modifier,
    uiState: PomodoroSettingUiState,
    onEvent: (PomodoroSettingEvent) -> Unit,
    onDismissRequest: () -> Unit
) {

    Column(
        modifier = modifier
            .verticalScroll(state = rememberScrollState())
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(alignment = Alignment.Center),
                text = stringResource(R.string.configurar_pomodoro),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )

            IconButton(
                modifier = Modifier.align(alignment = Alignment.CenterEnd),
                onClick = onDismissRequest
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.material_symbols_close_rounded),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            LabelSection(
                label = stringResource(R.string.duracao_das_etapas),
                icon = {
                    Icon(
                        modifier = Modifier.size(18.dp),
                        painter = painterResource(id = R.drawable.mingcute_time_line),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                FullFocusInputWheelPicker(
                    modifier = Modifier.weight(1f),
                    label = stringResource(R.string.foco),
                    value = uiState.focusTime,
                    onShowPicker = {
                        onEvent(PomodoroSettingEvent.ShowModal(modal = PomodoroSettingModalUiState.FocusTimer))
                    }
                )

                FullFocusInputWheelPicker(
                    modifier = Modifier.weight(1f),
                    label = stringResource(R.string.pausa_curta),
                    value = uiState.shortBreakTime,
                    onShowPicker = {
                        onEvent(PomodoroSettingEvent.ShowModal(modal = PomodoroSettingModalUiState.ShortBreakTimer))
                    }
                )

                FullFocusInputWheelPicker(
                    modifier = Modifier.weight(1f),
                    label = stringResource(R.string.pausa_longa),
                    value = uiState.longBreakTime,
                    onShowPicker = {
                        onEvent(PomodoroSettingEvent.ShowModal(modal = PomodoroSettingModalUiState.LongBreakTimer))
                    }
                )
            }
        }

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            LabelSection(
                label = stringResource(R.string.cores_e_visual),
                icon = {
                    Icon(
                        modifier = Modifier.size(18.dp),
                        painter = painterResource(id = R.drawable.solar_pallete_2_linear),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            )

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f),
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
                )
            ) {
                Column {
                    StyleColorItem(
                        label = "Cor do Foco",
                        color = uiState.focusProgressColor,
                        onClick = {
                            onEvent(
                                PomodoroSettingEvent.ShowModal(
                                    modal = PomodoroSettingModalUiState.PickerColor(
                                        color = uiState.focusProgressColor,
                                        type = PickerColorType.FOCUS_PICKER_COLOR
                                    )
                                )
                            )
                        }
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 12.dp),
                        thickness = 0.5.dp,
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
                    )
                    StyleColorItem(
                        label = "Cor da Pausa Curta",
                        color = uiState.shortBreakProgressColor,
                        onClick = {
                            onEvent(
                                PomodoroSettingEvent.ShowModal(
                                    modal = PomodoroSettingModalUiState.PickerColor(
                                        color = uiState.shortBreakProgressColor,
                                        type = PickerColorType.SHORT_BREAK_PICKER_COLOR
                                    )
                                )
                            )
                        }
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 12.dp),
                        thickness = 0.5.dp,
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
                    )
                    StyleColorItem(
                        label = "Cor da Pausa Longa",
                        color = uiState.longBreakProgressColor,
                        onClick = {
                            onEvent(
                                PomodoroSettingEvent.ShowModal(
                                    modal = PomodoroSettingModalUiState.PickerColor(
                                        color = uiState.longBreakProgressColor,
                                        type = PickerColorType.LONG_BREAK_PICKER_COLOR
                                    )
                                )
                            )
                        }
                    )
                }
            }

        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Button(
            enabled = uiState.changedSetting,
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

@Composable
private fun StyleColorItem(
    label: String,
    color: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(color)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                    shape = CircleShape
                )
        )
    }
}

@Composable
private fun LabelSection(
    modifier: Modifier = Modifier,
    icon: (@Composable () -> Unit)? = null,
    label: String
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        icon?.invoke()

        Text(
            text = label,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary
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