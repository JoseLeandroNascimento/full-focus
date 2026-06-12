package com.joseleandro.fullfocus.ui.screen.pomodoro_setting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.LocalLocale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.joseleandro.fullfocus.R
import com.joseleandro.fullfocus.core.model.Screen
import com.joseleandro.fullfocus.core.model.SettingSound
import com.joseleandro.fullfocus.core.viewModel.FullFocusNavigation
import com.joseleandro.fullfocus.domain.effect.PomodoroSettingEffect
import com.joseleandro.fullfocus.ui.component.ConfigOptionColor
import com.joseleandro.fullfocus.ui.component.ConfigOptionNav
import com.joseleandro.fullfocus.ui.component.ConfigOptionSwitch
import com.joseleandro.fullfocus.ui.component.FullFocusCardConfigSection
import com.joseleandro.fullfocus.ui.component.FullFocusInputWheelPicker
import com.joseleandro.fullfocus.ui.component.FullFocusWheelPickerDialog
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
    val navigationViewModel = koinViewModel<FullFocusNavigation>()
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
        onEvent = viewModel::onEvent,
        navigate = navigationViewModel::navigate
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PomodoroSettingBottomSheet(
    onDismissRequest: () -> Unit,
    sheetState: SheetState,
    uiState: PomodoroSettingUiState,
    onEvent: (PomodoroSettingEvent) -> Unit,
    navigate: (Screen) -> Unit
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
            onDismissRequest = onDismissRequest,
            navigate = navigate
        )
    } else {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = onDismissRequest
        ) {
            PomodoroSettingBottomSheetContent(
                uiState = uiState,
                onEvent = onEvent,
                onDismissRequest = onDismissRequest,
                navigate = navigate
            )
        }
    }

}


@Composable
private fun PomodoroSettingBottomSheetContent(
    modifier: Modifier = Modifier,
    uiState: PomodoroSettingUiState,
    onEvent: (PomodoroSettingEvent) -> Unit,
    onDismissRequest: () -> Unit,
    navigate: (Screen) -> Unit
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(.9f)
            .padding(horizontal = 16.dp)
    ) {

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(state = rememberScrollState())
                .fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(alignment = Alignment.Center),
                    text = stringResource(R.string.configurar_pomodoro),
                    style = MaterialTheme.typography.titleSmall,
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
                    label = stringResource(R.string.sessao),
                    icon = {
                        Icon(
                            modifier = Modifier.size(18.dp),
                            painter = painterResource(id = R.drawable.material_symbols_timer_outline_rounded),
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

            FullFocusCardConfigSection(
                title = stringResource(R.string.cores_e_visual),
                titleIcon = {
                    Icon(
                        modifier = Modifier.size(18.dp),
                        painter = painterResource(id = R.drawable.solar_pallete_2_linear),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            ) {
                option {
                    ConfigOptionColor(
                        title = "Cor de Foco",
                        subtitle = "Personalize a cor do timer de foco",
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
                }
                option {
                    ConfigOptionColor(
                        title = "Cor da Pausa Curta",
                        subtitle = "Personalize a cor da pausa curta",
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
                }
                option {
                    ConfigOptionColor(
                        title = "Cor da Pausa Longa",
                        subtitle = "Personalize a cor da pausa longa",
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

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            FullFocusCardConfigSection(
                title = "Som e notificação",
                titleIcon = {
                    Icon(
                        modifier = Modifier.size(18.dp),
                        painter = painterResource(id = R.drawable.basil_notification_on_outline),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            ) {
                option {
                    ConfigOptionSwitch(
                        title = "Modo silencioso",
                        subtitle = "Desativa todos os sons e vibrações",
                        icon = R.drawable.mingcute_volume_mute_line,
                        checked = uiState.silentMode,
                        onCheckedChange = {
                            onEvent(PomodoroSettingEvent.UpdateSilentMode(value = it))
                        }
                    )
                }
                option {
                    ConfigOptionNav(
                        title = "Som de foco",
                        subtitle = "Ondas do mar",
                        enabled = !uiState.silentMode,
                        icon = R.drawable.wpf_audio_wave,
                        onClick = {
                            navigate(Screen.SittingSoundPomodoroScreen(type = SettingSound.SETTING_SOUND_FOCUS))
                        }
                    )
                }
                option {
                    ConfigOptionNav(
                        title = "Som de pausa",
                        subtitle = "Cafeteria",
                        enabled = !uiState.silentMode,
                        icon = R.drawable.mynaui_coffee,
                        onClick = {
                            navigate(Screen.SittingSoundPomodoroScreen(type = SettingSound.SETTING_SOUND_BREAK))
                        }
                    )
                }
                option {
                    ConfigOptionNav(
                        title = "Configuração de Notificação",
                        subtitle = "Gerenciar alertas do sistema",
                        enabled = !uiState.silentMode,
                        icon = R.drawable.basil_notification_on_outline,
                        onClick = {}
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(16.dp)
            )
        }


        Button(
            enabled = uiState.changedSetting,
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
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
            style = MaterialTheme.typography.titleSmall.copy(fontSize = 14.sp),
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
            onEvent = {},
            navigate = {}
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
            onEvent = {},
            navigate = {}
        )
    }
}