package com.joseleandro.fullfocus.ui.screen.pomodoro.component

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.joseleandro.fullfocus.R
import com.joseleandro.fullfocus.data.local.preferences.data.PomodoroStatus
import com.joseleandro.fullfocus.data.local.preferences.data.enums.StatusSession
import com.joseleandro.fullfocus.ui.state.PomodoroUiState

@Composable
fun PomodoroControls(
    modifier: Modifier = Modifier,
    uiState: PomodoroUiState,
    onStart: () -> Unit,
    onPause: () -> Unit,
    onPlay: () -> Unit,
    onReset: () -> Unit,
    onSkip: () -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        if (uiState.pomodoroStatus != PomodoroStatus.START) {

            if (uiState.statusSession == StatusSession.PAUSE_LONG || uiState.statusSession == StatusSession.PAUSE_SHORT) {
                IconButton(
                    onClick = onSkip,
                    enabled = uiState.taskCurrent != null
                ) {
                    Icon(
                        painterResource(R.drawable.rounded_skip_next_24),
                        contentDescription = "Pular"
                    )
                }
            } else {

                IconButton(
                    onClick = onReset,
                    enabled = uiState.taskCurrent != null
                ) {
                    Icon(
                        painterResource(R.drawable.outline_restart_alt_24),
                        contentDescription = "Reiniciar"
                    )
                }
            }

        }

        PomodoroButtonPrimary(
            label = stringResource(id = getButtonLabel(uiState)),
            iconRes = getButtonIcon(uiState),
            enabled = uiState.taskCurrent != null,
            onClick = {
                when (uiState.pomodoroStatus) {
                    PomodoroStatus.IDLE,
                    PomodoroStatus.START -> onStart()

                    PomodoroStatus.PROGRESS -> {
                        if (uiState.isPlay) onPause() else onPlay()
                    }

                    PomodoroStatus.FINISHED -> onReset()
                }
            }
        )

        if (uiState.pomodoroStatus != PomodoroStatus.START) {
            IconButton(
                onClick = {},
                enabled = uiState.taskCurrent != null
            ) {
                Icon(
                    painterResource(R.drawable.baseline_close_24),
                    contentDescription = "cancelar"
                )
            }
        }
    }
}

@StringRes
fun getButtonLabel(uiState: PomodoroUiState): Int {
    return when (uiState.pomodoroStatus) {
        PomodoroStatus.IDLE,
        PomodoroStatus.START -> {
            if (uiState.statusSession == StatusSession.FOCUS) {
                R.string.iniciar_foco
            } else {
                R.string.iniciar_pausa
            }
        }

        PomodoroStatus.PROGRESS -> if (uiState.isPlay) R.string.pausar else R.string.retomar
        PomodoroStatus.FINISHED -> R.string.recomecar
    }
}

@DrawableRes
fun getButtonIcon(uiState: PomodoroUiState): Int {
    return when (uiState.pomodoroStatus) {
        PomodoroStatus.IDLE,
        PomodoroStatus.START -> R.drawable.round_play_arrow_24

        PomodoroStatus.PROGRESS -> {
            if (uiState.isPlay) R.drawable.baseline_pause_24
            else R.drawable.round_play_arrow_24
        }

        PomodoroStatus.FINISHED -> R.drawable.outline_restart_alt_24
    }
}