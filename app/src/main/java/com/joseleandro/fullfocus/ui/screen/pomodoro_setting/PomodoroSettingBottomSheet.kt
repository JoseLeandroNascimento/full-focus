package com.joseleandro.fullfocus.ui.screen.pomodoro_setting

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.LocalLocale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.joseleandro.fullfocus.R
import com.joseleandro.fullfocus.core.model.Screen
import com.joseleandro.fullfocus.core.model.SettingSound
import com.joseleandro.fullfocus.core.viewModel.FullFocusNavigationViewModel
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
import com.joseleandro.fullfocus.ui.screen.progress_time_color_customize.component.PickerColorType
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
    val navigationViewModel = koinViewModel<FullFocusNavigationViewModel>()
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
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
            onDismissRequest = onDismissRequest,
            contentWindowInsets = {
                WindowInsets(left = 0, top = 0, right = 0, bottom = 0)
            }
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


@OptIn(ExperimentalMaterial3Api::class)
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
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, bottom = 8.dp, start = 16.dp, end = 8.dp),
        ) {
            Text(
                modifier = Modifier
                    .align(alignment = Alignment.Center),
                text = stringResource(R.string.configurar_pomodoro),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface
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

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(state = rememberScrollState())
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
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

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                    border = BorderStroke(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
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
            }

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
                        title = stringResource(R.string.cor_de_foco),
                        subtitle = stringResource(R.string.personalize_a_cor_do_timer_de_foco),
                        color = uiState.focusProgressColor,
                        onClick = {
                            navigate(
                                Screen.ProgressTimeColorCustomizeScreen(
                                    type = PickerColorType.FOCUS_PICKER_COLOR,
                                    initialColor = uiState.focusProgressColor
                                )
                            )
                        }
                    )
                }
                option {
                    ConfigOptionColor(
                        title = stringResource(R.string.cor_da_pausa_curta),
                        subtitle = stringResource(R.string.personalize_a_cor_da_pausa_curta),
                        color = uiState.shortBreakProgressColor,
                        onClick = {
                            navigate(
                                Screen.ProgressTimeColorCustomizeScreen(
                                    type = PickerColorType.SHORT_BREAK_PICKER_COLOR,
                                    initialColor = uiState.shortBreakProgressColor
                                )
                            )
                        }
                    )
                }
                option {
                    ConfigOptionColor(
                        title = stringResource(R.string.cor_da_pausa_longa),
                        subtitle = stringResource(R.string.personalize_a_cor_da_pausa_longa),
                        color = uiState.longBreakProgressColor,
                        onClick = {
                            navigate(
                                Screen.ProgressTimeColorCustomizeScreen(
                                    type = PickerColorType.LONG_BREAK_PICKER_COLOR,
                                    initialColor = uiState.longBreakProgressColor
                                )
                            )
                        }
                    )
                }
            }

            FullFocusCardConfigSection(
                title = stringResource(R.string.som_e_notifica_o),
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
                        title = stringResource(R.string.modo_silencioso),
                        subtitle = stringResource(R.string.desativa_todos_os_sons),
                        icon = R.drawable.mingcute_volume_mute_line,
                        checked = uiState.silentMode,
                        onCheckedChange = {
                            onEvent(PomodoroSettingEvent.UpdateSilentMode(value = it))
                        }
                    )
                }
                option {
                    ConfigOptionNav(
                        title = stringResource(R.string.som_de_foco),
                        subtitle = uiState.soundFocus?.title ?: "",
                        enabled = !uiState.silentMode,
                        icon = R.drawable.wpf_audio_wave,
                        onClick = {
                            navigate(Screen.SittingSoundPomodoroScreen(type = SettingSound.SETTING_SOUND_FOCUS))
                        }
                    )
                }
                option {
                    ConfigOptionNav(
                        title = stringResource(R.string.som_de_pausa),
                        subtitle = uiState.soundPause?.title ?: "",
                        enabled = !uiState.silentMode,
                        icon = R.drawable.mynaui_coffee,
                        onClick = {
                            navigate(Screen.SittingSoundPomodoroScreen(type = SettingSound.SETTING_SOUND_BREAK))
                        }
                    )
                }
                option {
                    ConfigOptionNav(
                        title = stringResource(R.string.configura_o_de_notificacao),
                        subtitle = stringResource(R.string.gerenciar_alertas_do_sistema),
                        enabled = !uiState.silentMode,
                        icon = R.drawable.basil_notification_on_outline,
                        onClick = {
                            navigate(Screen.NotificationSettingScreen)
                        }
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(8.dp)
            )
        }

        Surface(
            modifier = Modifier
                .fillMaxWidth(),
            color = BottomSheetDefaults.ContainerColor,
        ) {
            Button(
                enabled = uiState.changedSetting,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .navigationBarsPadding(),
                shape = MaterialTheme.shapes.medium,
                onClick = {
                    onEvent(PomodoroSettingEvent.OnSave)
                }
            ) {
                Text(
                    text = stringResource(id = R.string.salvar),
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                )
            }
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
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        icon?.invoke()

        Text(
            text = label,
            style = MaterialTheme.typography.titleSmall.copy(fontSize = 14.sp),
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.SemiBold
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