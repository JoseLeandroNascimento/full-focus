package com.joseleandro.fullfocus.ui.screen.pomodoro

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.style.ExperimentalFoundationStyleApi
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.joseleandro.fullfocus.ui.screen.pomodoro.component.ConfirmCancelPomodoroDialog
import com.joseleandro.fullfocus.ui.screen.pomodoro.component.PomodoroButton
import com.joseleandro.fullfocus.ui.screen.pomodoro.component.PomodoroFinishedSuccessDialog
import com.joseleandro.fullfocus.ui.screen.pomodoro_setting.PomodoroSettingBottomSheet
import com.joseleandro.fullfocus.ui.state.PomodoroModalUiState
import com.joseleandro.fullfocus.ui.state.PomodoroUiState
import com.joseleandro.fullfocus.ui.theme.ColorStyle
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


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationStyleApi::class)
@Composable
fun PomodoroScreen(
    uiState: PomodoroUiState,
    onEvent: (PomodoroEvent) -> Unit
) {

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
                .fillMaxSize()
                .verticalScroll(state = rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(
                space = 32.dp,
                alignment = Alignment.CenterVertically
            ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            FullFocusPomodoroTime(
                progress = uiState.progressPercent,
                state = uiState.pomodoroState,
                colorProgress = uiState.colorProgress,
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
                    text = stringResource(
                        R.string.pomodoro_de,
                        uiState.completedPomodoroCount,
                        uiState.sessionsUntilLongPause
                    ),
                    style = MaterialTheme.typography.bodySmall
                )

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
                            PomodoroButton(
                                onClick = { onEvent(PomodoroEvent.Play) }
                            ) {
                                Icon(
                                    modifier = Modifier.size(36.dp),
                                    painter = painterResource(id = R.drawable.line_md_play_filled),
                                    contentDescription = "play"
                                )
                            }
                            if (uiState.pomodoroState != PomodoroState.FOCUS) {
                                PomodoroButton(
                                    onClick = { onEvent(PomodoroEvent.Skip) }
                                ) {
                                    Icon(
                                        modifier = Modifier.size(24.dp),
                                        painter = painterResource(id = R.drawable.mage_next_fill),
                                        contentDescription = "skip"
                                    )
                                }
                            }
                        }
                    }
                }
            }

        }
    }

    PomodoroModal(
        uiState = uiState,
        onEvent = onEvent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PomodoroModal(
    uiState: PomodoroUiState,
    onEvent: (PomodoroEvent) -> Unit
) {

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

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
            ConfirmCancelPomodoroDialog(
                onDismissRequest = {
                    onEvent(PomodoroEvent.CloseModal)
                },
                onDiscard = {
                    onEvent(PomodoroEvent.CancelAndDelete)
                    onEvent(PomodoroEvent.CloseModal)
                },
                onSaveProgress = {
                    onEvent(PomodoroEvent.CancelAndSave)
                    onEvent(PomodoroEvent.CloseModal)
                }
            )
        }

        PomodoroModalUiState.FocusFinished -> {
            PomodoroFinishedSuccessDialog(
                type = PomodoroState.FOCUS,
                onDismissRequest = {
                    onEvent(PomodoroEvent.CloseModal)
                }
            )
        }

        PomodoroModalUiState.LongBreakFinished -> {
            PomodoroFinishedSuccessDialog(
                type = PomodoroState.LONG_PAUSE,
                onDismissRequest = {
                    onEvent(PomodoroEvent.CloseModal)
                }
            )
        }

        PomodoroModalUiState.ShortBreakFinished -> {
            PomodoroFinishedSuccessDialog(
                type = PomodoroState.SHORT_PAUSE,
                onDismissRequest = {
                    onEvent(PomodoroEvent.CloseModal)
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
            uiState = PomodoroUiState(
                colorProgress = ColorStyle.fromColor(Color(0xFF25D9FF))
            ),
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
            uiState = PomodoroUiState(
                colorProgress = ColorStyle.fromColor(Color(0xFF25D9FF))
            ),
            onEvent = {}
        )
    }
}