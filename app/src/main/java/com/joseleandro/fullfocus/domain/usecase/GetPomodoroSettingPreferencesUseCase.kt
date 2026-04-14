package com.joseleandro.fullfocus.domain.usecase

import com.joseleandro.fullfocus.domain.repository.PomodoroSettingPreferencesRepository

class GetPomodoroSettingPreferencesUseCase(
    private val pomodoroSettingRepository: PomodoroSettingPreferencesRepository
) {
    operator fun invoke() =
        pomodoroSettingRepository.pomodoroSettingPreferences
}