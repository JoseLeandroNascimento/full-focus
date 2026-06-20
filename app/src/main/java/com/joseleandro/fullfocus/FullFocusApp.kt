package com.joseleandro.fullfocus

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.joseleandro.fullfocus.core.model.Screen
import com.joseleandro.fullfocus.core.viewModel.FullFocusNavigation
import com.joseleandro.fullfocus.ui.screen.config_sound.ConfigSoundScreen
import com.joseleandro.fullfocus.ui.screen.progress_time_color_customize.ProgressTimeColorCustomizeScreen
import com.joseleandro.fullfocus.ui.screen.progress_time_color_customize.ProgressTimeColorCustomizeViewModel
import com.joseleandro.fullfocus.ui.screen.pomodoro.PomodoroScreen
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FullFocusApp() {

    val navigationViewModel = koinViewModel<FullFocusNavigation>()
    val uiState by navigationViewModel.uiState.collectAsStateWithLifecycle()

    val onBack = navigationViewModel::onBack

    NavDisplay(
        onBack = onBack,
        backStack = uiState.backStack,
        entryProvider = entryProvider {
            entry<Screen.PomodoroScreen> {
                PomodoroScreen()
            }
            entry<Screen.SittingSoundPomodoroScreen> {

                ConfigSoundScreen(
                    typeSettingSound = it.type,
                    onNavigateBack = onBack
                )
            }
            entry<Screen.PickerColorScreen> { screen ->
                ProgressTimeColorCustomizeScreen(
                    type = screen.type,
                    initialColor = screen.initialColor,
                    viewModel = koinViewModel<ProgressTimeColorCustomizeViewModel>(),
                    onNavigateBack = onBack,
                    onConfirm = {
                        navigationViewModel.onBack()
                    }
                )
            }
        }
    )

}