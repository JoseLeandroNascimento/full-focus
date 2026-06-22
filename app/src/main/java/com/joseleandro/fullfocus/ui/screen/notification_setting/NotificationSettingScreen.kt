package com.joseleandro.fullfocus.ui.screen.notification_setting

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.joseleandro.fullfocus.R
import com.joseleandro.fullfocus.ui.component.ConfigOptionNav
import com.joseleandro.fullfocus.ui.component.ConfigOptionSwitch
import com.joseleandro.fullfocus.ui.component.FullFocusCardConfigSection
import com.joseleandro.fullfocus.ui.event.NotificationSettingEvent
import com.joseleandro.fullfocus.ui.screen.notification_setting.component.AlertSoundBottomSheet
import com.joseleandro.fullfocus.ui.state.NotificationSettingModalUiState
import com.joseleandro.fullfocus.ui.state.NotificationSettingUiState
import com.joseleandro.fullfocus.ui.theme.FullFocusTheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun NotificationSettingScreen(
    onNavigateBack: () -> Unit
) {
    val viewModel = koinViewModel<NotificationSettingViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.onEvent(
            NotificationSettingEvent.LoadData
        )
    }

    NotificationSettingScreen(
        onNavigateBack = onNavigateBack,
        uiState = uiState,
        onEvent = viewModel::onEvent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationSettingScreen(
    onNavigateBack: () -> Unit,
    uiState: NotificationSettingUiState,
    onEvent: (NotificationSettingEvent) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.configuracoes_de_notificacao),
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            painter = painterResource(id = R.drawable.outline_arrow_back_ios_new_24),
                            contentDescription = stringResource(R.string.voltar)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            item { Spacer(modifier = Modifier.height(16.dp)) }

            item {
                FullFocusCardConfigSection(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    title = "Personalização por Etapa",
                    titleIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.material_symbols_timer_outline_rounded),
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                ) {
                    option {
                        ConfigOptionNav(
                            title = "Alerta de Foco Concluído",
                            subtitle = "Toque suave: Zen Bell",
                            icon = R.drawable.sound,
                            onClick = {
                                onEvent(NotificationSettingEvent.ShowModal(modal = NotificationSettingModalUiState.AlertSoundFocus))
                            }
                        )
                    }
                    option {
                        ConfigOptionNav(
                            title = "Alerta de Pausa Concluída",
                            subtitle = "Toque vibrante: Morning Bird",
                            icon = R.drawable.sound,
                            onClick = {
                                onEvent(NotificationSettingEvent.ShowModal(modal = NotificationSettingModalUiState.AlertSoundPause))
                            }
                        )
                    }
                    option {
                        ConfigOptionSwitch(
                            title = "Vibração Inteligente",
                            subtitle = "Padrão curto para pausas e longo para foco",
                            icon = R.drawable.solar_smartphone_vibration_linear,
                            checked = uiState.isVibrationEnabled,
                            onCheckedChange = {
                                onEvent(NotificationSettingEvent.UpdateVibrationEnabled(isEnabled = it))
                            }
                        )
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }

            item {
                FullFocusCardConfigSection(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    title = "Visibilidade e Foco",
                    titleIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.basil_notification_on_outline),
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                ) {
                    option {
                        ConfigOptionSwitch(
                            title = "Auto-limpeza",
                            subtitle = "Remover notificação anterior ao iniciar novo ciclo",
                            icon = R.drawable.material_symbols_close_rounded,
                            checked = true,
                            onCheckedChange = {}
                        )
                    }
                    option {
                        ConfigOptionSwitch(
                            title = "Timer na Barra de Status",
                            subtitle = "Exibir contagem regressiva fixa durante o foco",
                            icon = R.drawable.mingcute_time_line,
                            checked = true,
                            onCheckedChange = {}
                        )
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }

            // SEÇÃO: INTELIGÊNCIA (Funcionalidade Útil: Antecipação e Respeito)
            item {
                FullFocusCardConfigSection(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    title = "Configurações Inteligentes",
                    titleIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.mdi_timer_cog_outline),
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                ) {
                    option {
                        ConfigOptionSwitch(
                            title = "Alerta de Proximidade",
                            subtitle = "Sinal sutil faltando 1 minuto para o fim",
                            icon = R.drawable.fluent_alert_12_regular,
                            checked = false,
                            onCheckedChange = {}
                        )
                    }
                    option {
                        ConfigOptionSwitch(
                            title = "Ignorar Silencioso",
                            subtitle = "Alertas sonoros mesmo se o celular estiver no vibrar",
                            icon = R.drawable.mingcute_volume_mute_line,
                            checked = false,
                            onCheckedChange = {}
                        )
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(32.dp)) }
        }
    }

    NotificationSettingModal(
        uiState = uiState,
        onEvent = onEvent
    )


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NotificationSettingModal(
    uiState: NotificationSettingUiState,
    onEvent: (NotificationSettingEvent) -> Unit
) {

    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false
    )

    when (uiState.modal) {
        NotificationSettingModalUiState.AlertSoundFocus -> {
            AlertSoundBottomSheet(
                sheetState = bottomSheetState,
                soundSelected = uiState.alertSoundFocus,
                onSoundSelected = { soundSelected ->
                    onEvent(NotificationSettingEvent.UpdateAlertSoundFocus(sound = soundSelected))
                },
                onDismissRequest = {
                    onEvent(NotificationSettingEvent.CloseModal)
                }
            )
        }

        NotificationSettingModalUiState.AlertSoundPause -> {
            AlertSoundBottomSheet(
                sheetState = bottomSheetState,
                soundSelected = uiState.alertSoundPause,
                onSoundSelected = { soundSelected ->
                    onEvent(NotificationSettingEvent.UpdateAlertSoundPause(sound = soundSelected))
                },
                onDismissRequest = {
                    onEvent(NotificationSettingEvent.CloseModal)
                }
            )
        }

        NotificationSettingModalUiState.None -> {}
    }
}

@Preview
@Composable
private fun NotificationSettingScreenLightPreview() {
    FullFocusTheme(dynamicColor = false, darkTheme = false) {
        NotificationSettingScreen(
            onNavigateBack = {},
            uiState = NotificationSettingUiState(),
            onEvent = {}
        )
    }
}

@Preview
@Composable
private fun NotificationSettingScreenDarkPreview() {
    FullFocusTheme(dynamicColor = false, darkTheme = true) {
        NotificationSettingScreen(
            onNavigateBack = {},
            uiState = NotificationSettingUiState(),
            onEvent = {}
        )
    }
}
