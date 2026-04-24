package com.joseleandro.fullfocus.domain.usecase

import com.joseleandro.fullfocus.domain.repository.PomodoroTimeRepository

class SetCurrentTaskPomodoroUseCase(
    private val pomodoroTimeRepository: PomodoroTimeRepository
) {

    suspend operator fun invoke(id: Int? = null) {

        pomodoroTimeRepository.currentTask(id = id)

    }
}