package com.joseleandro.fullfocus.data.local.preferences

import com.joseleandro.fullfocus.data.local.preferences.data.PomodoroCurrentPreferences
import com.joseleandro.fullfocus.data.local.preferences.data.PomodoroSettingPreferences
import com.joseleandro.fullfocus.data.local.preferences.data.TaskFilterPreferences
import kotlinx.serialization.Serializable

@Serializable
data class UserPreferences(
    val taskFilter: TaskFilterPreferences = TaskFilterPreferences(),
    val pomodoro: PomodoroCurrentPreferences = PomodoroCurrentPreferences(),
    val pomodoroSettingPreferences: PomodoroSettingPreferences = PomodoroSettingPreferences()
)