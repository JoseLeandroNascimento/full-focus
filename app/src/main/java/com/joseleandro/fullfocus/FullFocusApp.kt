package com.joseleandro.fullfocus

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.joseleandro.fullfocus.core.model.Screen
import com.joseleandro.fullfocus.core.viewModel.FullFocusNavigation
import com.joseleandro.fullfocus.ui.screen.config_sound.ConfigSoundScreen
import com.joseleandro.fullfocus.ui.screen.pomodoro.PomodoroScreen
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FullFocusApp(modifier: Modifier = Modifier) {

    val viewModel = koinViewModel<FullFocusNavigation>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    NavDisplay(
        onBack = viewModel::onBack,
        backStack = uiState.backStack,
        entryProvider = entryProvider {
            entry<Screen.PomodoroScreen> {
                PomodoroScreen()
            }
            entry<Screen.SittingSoundPomodoroScreen> {
                ConfigSoundScreen(onNavigateBack = viewModel::onBack)
            }
        }
    )

}