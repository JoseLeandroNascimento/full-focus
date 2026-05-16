package com.joseleandro.fullfocus.ui.screen.pomodoro

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.joseleandro.fullfocus.R
import com.joseleandro.fullfocus.ui.component.FullFocusPomodoroTime
import com.joseleandro.fullfocus.ui.screen.event.PomodoroEvent
import com.joseleandro.fullfocus.ui.screen.state.PomodoroUiState
import com.joseleandro.fullfocus.ui.theme.FullFocusTheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PomodoroScreen(modifier: Modifier = Modifier) {

    val viewModel = koinViewModel<PomodoroViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    PomodoroScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent
    )

}

@Composable
fun PomodoroScreen(
    uiState: PomodoroUiState,
    onEvent: (PomodoroEvent) -> Unit
) {
    Scaffold(
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

            if (uiState.isRunning) {
                PomodoroButton(
                    onClick = {
                        onEvent(PomodoroEvent.OnPause)
                    }
                ) {
                    Icon(
                        modifier = Modifier.size(40.dp),
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
                        modifier = Modifier.size(40.dp),
                        painter = painterResource(id = R.drawable.line_md_play_filled),
                        contentDescription = "play"
                    )
                }
            }

        }
    }
}

@Composable
fun PomodoroButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true,
    icon: @Composable () -> Unit
) {

    Box(
        modifier = modifier
            .clip(shape = CircleShape)
            .clickable(
                enabled = enabled,
                onClick = onClick
            )
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        icon()
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