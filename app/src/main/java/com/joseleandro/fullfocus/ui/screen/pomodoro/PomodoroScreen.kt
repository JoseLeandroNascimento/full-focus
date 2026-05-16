package com.joseleandro.fullfocus.ui.screen.pomodoro

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import com.joseleandro.fullfocus.ui.component.FullFocusPomodoroTime
import com.joseleandro.fullfocus.ui.screen.event.PomodoroEvent
import com.joseleandro.fullfocus.ui.screen.pomodoro.component.PomodoroButton
import com.joseleandro.fullfocus.ui.screen.state.PomodoroUiState
import com.joseleandro.fullfocus.ui.theme.FullFocusTheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PomodoroScreen() {

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
                        onClick = {}
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
                state = uiState.statePomodoro,
                timeTotal = uiState.durationTime
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(
                    space = 16.dp,
                    alignment = Alignment.CenterVertically
                )
            ) {

                Text(
                    text = "pomodoro 1 de 4",
                    style = MaterialTheme.typography.bodySmall
                )

            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(
                    space = 8.dp,
                    alignment = Alignment.CenterHorizontally
                )
            ) {


                if (uiState.isRunning) {

                    PomodoroButton(
                        onClick = {
                            onEvent(PomodoroEvent.OnPause)
                        }
                    ) {
                        Icon(
                            modifier = Modifier.size(36.dp),
                            painter = painterResource(id = R.drawable.material_symbols_pause_rounded),
                            contentDescription = "pause"
                        )
                    }

                } else {

                    PomodoroButton(
                        onClick = {
                            onEvent(PomodoroEvent.OnPlay)
                        }
                    ) {
                        Icon(
                            modifier = Modifier.size(36.dp),
                            painter = painterResource(id = R.drawable.line_md_play_filled),
                            contentDescription = "play"
                        )
                    }

                    if (uiState.progressPomodoro) {

                        PomodoroButton(
                            onClick = {
                                onEvent(PomodoroEvent.OnRestart)
                            }
                        ) {
                            Icon(
                                modifier = Modifier.size(36.dp),
                                painter = painterResource(id = R.drawable.fa7_solid_rotate_back),
                                contentDescription = "restart"
                            )
                        }

                        PomodoroButton(
                            onClick = {
                                onEvent(PomodoroEvent.OnCancel)
                            }
                        ) {
                            Icon(
                                modifier = Modifier.size(36.dp),
                                painter = painterResource(id = R.drawable.material_symbols_close_rounded),
                                contentDescription = "cancel"
                            )
                        }
                    }
                }
            }
        }
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
                progressPercent = .8f,
                durationTime = 25 * 60 * 1000
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
                progressPercent = .8f,
                durationTime = 25 * 60 * 1000
            ),
            onEvent = {}
        )
    }
}