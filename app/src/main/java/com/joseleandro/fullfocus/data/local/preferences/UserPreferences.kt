package com.joseleandro.fullfocus.data.local.preferences

import com.joseleandro.fullfocus.data.local.preferences.data.PomodoroPreferences
import com.joseleandro.fullfocus.data.local.preferences.data.TaskFilterPreferences
import kotlinx.serialization.Serializable

@Serializable
data class UserPreferences(
    val taskFilter: TaskFilterPreferences = TaskFilterPreferences(),
    val pomodoro: PomodoroPreferences = PomodoroPreferences()
)