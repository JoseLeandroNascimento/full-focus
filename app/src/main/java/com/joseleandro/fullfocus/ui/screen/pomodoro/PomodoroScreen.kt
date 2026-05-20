package com.joseleandro.fullfocus.ui.screen.pomodoro

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.joseleandro.fullfocus.R
import com.joseleandro.fullfocus.data.local.database.model.PomodoroState
import com.joseleandro.fullfocus.ui.component.FullFocusPomodoroTime
import com.joseleandro.fullfocus.ui.event.PomodoroEvent
import com.joseleandro.fullfocus.ui.screen.pomodoro.component.PomodoroButton
import com.joseleandro.fullfocus.ui.screen.pomodoro_setting.PomodoroSettingBottomSheet
import com.joseleandro.fullfocus.ui.state.PomodoroModalUiState
import com.joseleandro.fullfocus.ui.state.PomodoroUiState
import com.joseleandro.fullfocus.ui.theme.FullFocusTheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PomodoroScreen(
) {

    val viewModel = koinViewModel<PomodoroViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    PomodoroScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PomodoroScreen(
    uiState: PomodoroUiState,
    onEvent: (PomodoroEvent) -> Unit
) {

    val sheetState = rememberModalBottomSheetState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = {}
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.material_symbols_menu_rounded),
                            contentDescription = stringResource(R.string.menu)
                        )
                    }
                },
                title = {
                    Text(
                        text = stringResource(R.string.pomodoro),
                        style = MaterialTheme.typography.titleSmall.copy(
                            letterSpacing = 4.sp
                        )
                    )
                },
                actions = {
                    IconButton(
                        onClick = {
                            onEvent(PomodoroEvent.ShowModal(modal = PomodoroModalUiState.PomodoroSetting))
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.mdi_timer_cog_outline),
                            contentDescription = stringResource(R.string.setting_pomodoro)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(
                space = 32.dp,
                alignment = Alignment.CenterVertically
            ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            FullFocusPomodoroTime(
                progress = uiState.progressPercent,
                state = uiState.pomodoroState,
                timeTotal = uiState.duration
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(
                    space = 16.dp,
                    alignment = Alignment.CenterVertically
                )
            ) {

                Text(
                    text = "pomodoro ${uiState.completedPomodoroCount} de ${uiState.sessionsUntilLongPause}",
                    style = MaterialTheme.typography.bodySmall
                )

            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(
                    space = 16.dp,
                    alignment = Alignment.CenterHorizontally
                )
            ) {
                if (uiState.isRunning) {
                    PomodoroButton(
                        onClick = { onEvent(PomodoroEvent.Pause) }
                    ) {
                        Icon(
                            modifier = Modifier.size(36.dp),
                            painter = painterResource(id = R.drawable.material_symbols_pause_rounded),
                            contentDescription = "pause"
                        )
                    }
                } else {
                    if (uiState.progressPercent < 1f) {
                        // Paused state buttons
                        PomodoroButton(
                            onClick = { onEvent(PomodoroEvent.Skip) }
                        ) {
                            Icon(
                                modifier = Modifier.size(24.dp),
                                painter = painterResource(id = R.drawable.mage_next_fill),
                                contentDescription = "skip"
                            )
                        }

                        PomodoroButton(
                            onClick = { onEvent(PomodoroEvent.Play) }
                        ) {
                            Icon(
                                modifier = Modifier.size(36.dp),
                                painter = painterResource(id = R.drawable.line_md_play_filled),
                                contentDescription = "play"
                            )
                        }

                        PomodoroButton(
                            onClick = { onEvent(PomodoroEvent.Reverse) }
                        ) {
                            Icon(
                                modifier = Modifier.size(24.dp),
                                painter = painterResource(id = R.drawable.fa7_solid_rotate_back),
                                contentDescription = "restart"
                            )
                        }

                        PomodoroButton(
                            onClick = { onEvent(PomodoroEvent.ShowModal(PomodoroModalUiState.CancelOptions)) }
                        ) {
                            Icon(
                                modifier = Modifier.size(24.dp),
                                painter = painterResource(id = R.drawable.material_symbols_close_rounded),
                                contentDescription = "cancel"
                            )
                        }
                    } else {
                        // Initial state
                        PomodoroButton(
                            onClick = { onEvent(PomodoroEvent.Play) }
                        ) {
                            Icon(
                                modifier = Modifier.size(36.dp),
                                painter = painterResource(id = R.drawable.line_md_play_filled),
                                contentDescription = "play"
                            )
                        }
                    }
                }
            }
        }
    }

    when (uiState.modal) {
        PomodoroModalUiState.PomodoroSetting -> {
            PomodoroSettingBottomSheet(
                onDismissRequest = {
                    onEvent(PomodoroEvent.CloseModal)
                },
                sheetState = sheetState
            )
        }

        PomodoroModalUiState.CancelOptions -> {
            AlertDialog(
                onDismissRequest = { onEvent(PomodoroEvent.CloseModal) },
                title = { Text(text = stringResource(R.string.cancelar_pomodoro)) },
                text = { Text(text = stringResource(R.string.escolha_como_deseja_cancelar)) },
                confirmButton = {
                    TextButton(
                        onClick = {
                            onEvent(PomodoroEvent.CancelAndSave)
                            onEvent(PomodoroEvent.CloseModal)
                        }
                    ) {
                        Text(text = stringResource(R.string.salvar_progresso))
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            onEvent(PomodoroEvent.CancelAndDelete)
                            onEvent(PomodoroEvent.CloseModal)
                        }
                    ) {
                        Text(
                            text = stringResource(R.string.descartar),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            )
        }

        PomodoroModalUiState.None -> {}
    }
}


@Preview
@Composable
private fun PomodoroScreenLightPreview() {
    FullFocusTheme(
        dynamicColor = false,
        darkTheme = false
    ) {
        PomodoroScreen(
            uiState = PomodoroUiState(),
            onEvent = {}
        )
    }
}

@Preview
@Composable
private fun PomodoroScreenDarkPreview() {
    FullFocusTheme(
        dynamicColor = false,
        darkTheme = true
    ) {
        PomodoroScreen(
            uiState = PomodoroUiState(),
            onEvent = {}
        )
    }
}