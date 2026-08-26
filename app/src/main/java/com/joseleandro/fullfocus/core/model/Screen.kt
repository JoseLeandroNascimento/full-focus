package com.joseleandro.fullfocus.core.model

import com.joseleandro.fullfocus.ui.screen.progress_time_color_customize.component.PickerColorType
import com.joseleandro.fullfocus.ui.theme.ColorStyle
import kotlinx.serialization.Serializable


sealed interface Screen {

    @Serializable
    data object PomodoroScreen : Screen

    @Serializable
    data object MainScreen : Screen

    @Serializable
    data class SittingSoundPomodoroScreen(val type: SettingSound) : Screen

    @Serializable
    data class ProgressTimeColorCustomizeScreen(
        val type: PickerColorType,
        val initialColor: ColorStyle
    ) : Screen

    @Serializable
    data object NotificationSettingScreen : Screen

    @Serializable
    data object MetaScreen : Screen

}