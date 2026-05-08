package com.joseleandro.fullfocus.ui.screen.pomodoro

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.joseleandro.fullfocus.domain.model.PomodoroState
import com.joseleandro.fullfocus.ui.component.FullFocusPomodoroTime
import com.joseleandro.fullfocus.ui.theme.FullFocusTheme

@Composable
fun PomodoroScreen() {
    Scaffold(
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            FullFocusPomodoroTime(
                progress = .2f,
                state = PomodoroState.FOCUS,
                timeTotal = 25 * 60 * 1000
            )
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