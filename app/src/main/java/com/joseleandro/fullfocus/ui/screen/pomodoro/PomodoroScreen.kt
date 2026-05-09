package com.joseleandro.fullfocus.ui.screen.pomodoro

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joseleandro.fullfocus.domain.model.PomodoroState
import com.joseleandro.fullfocus.ui.component.FullFocusPomodoroTime
import com.joseleandro.fullfocus.ui.component.FullFocusRadioProgressPomodoroIndicator
import com.joseleandro.fullfocus.ui.theme.FullFocusTheme

@Composable
fun PomodoroScreen() {
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
                progress = .8f,
                state = PomodoroState.FOCUS,
                timeTotal = 25 * 60 * 1000
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

                FullFocusRadioProgressPomodoroIndicator(
                    indexCurrent = 3,
                    totalPomodoros = 4
                )
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
        PomodoroScreen()
    }
}

@Preview
@Composable
private fun PomodoroScreenDarkPreview() {
    FullFocusTheme(
        dynamicColor = false,
        darkTheme = true
    ) {
        PomodoroScreen()
    }
}