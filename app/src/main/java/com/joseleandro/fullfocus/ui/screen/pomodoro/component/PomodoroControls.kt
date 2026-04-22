package com.joseleandro.fullfocus.ui.screen.pomodoro.component

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
import com.joseleandro.fullfocus.ui.screen.pomodoro.getButtonIcon
import com.joseleandro.fullfocus.ui.screen.pomodoro.getButtonLabel
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
        IconButton(
            onClick = onSkip,
            enabled = uiState.taskCurrent != null
        ) {
            Icon(
                painterResource(R.drawable.rounded_skip_next_24),
                contentDescription = "Pular"
            )
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

        IconButton(
            onClick = onReset,
            enabled = uiState.taskCurrent != null && uiState.pomodoroStatus != PomodoroStatus.START
        ) {
            Icon(
                painterResource(R.drawable.outline_restart_alt_24),
                contentDescription = "Reiniciar"
            )
        }
    }
}
