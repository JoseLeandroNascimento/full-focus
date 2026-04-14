package com.joseleandro.fullfocus.domain.usecase

import com.joseleandro.fullfocus.data.local.preferences.data.PomodoroSettingPreferences
import com.joseleandro.fullfocus.domain.repository.PomodoroSettingPreferencesRepository

class UpdatePomodoroSettingUseCase(
    private val repository: PomodoroSettingPreferencesRepository
) {
    suspend operator fun invoke(pomodoroSetting: PomodoroSettingPreferences) {
        repository.updatePomodoroSetting(pomodoroSetting)
    }
}