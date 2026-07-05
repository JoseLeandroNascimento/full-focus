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
                    title = stringResource(R.string.personalizacao_por_etapa),
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
                            title = stringResource(R.string.alerta_de_foco_concluido),
                            subtitle = uiState.alertSoundFocus?.let { stringResource(id = it.title) },
                            icon = R.drawable.sound,
                            onClick = {
                                onEvent(NotificationSettingEvent.ShowModal(modal = NotificationSettingModalUiState.AlertSoundFocus))
                            }
                        )
                    }
                    option {
                        ConfigOptionNav(
                            title = stringResource(R.string.alerta_de_pausa_concluida),
                            subtitle = uiState.alertSoundPause?.let { stringResource(id = it.title) },
                            icon = R.drawable.sound,
                            onClick = {
                                onEvent(NotificationSettingEvent.ShowModal(modal = NotificationSettingModalUiState.AlertSoundPause))
                            }
                        )
                    }
                    option {
                        ConfigOptionSwitch(
                            title = stringResource(R.string.vibracao_inteligente),
                            subtitle = stringResource(R.string.padrao_curto_para_pausas_e_longo_para_foco),
                            icon = R.drawable.solar_smartphone_vibration_linear,
                            checked = uiState.isVibrationEnabled,
                            onCheckedChange = {
                                onEvent(NotificationSettingEvent.UpdateVibrationEnabled(isEnabled = it))
                            }
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
