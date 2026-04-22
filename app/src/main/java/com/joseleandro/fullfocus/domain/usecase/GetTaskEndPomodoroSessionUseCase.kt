package com.joseleandro.fullfocus.domain.usecase

import com.joseleandro.fullfocus.domain.data.TaskWithPomodoroSessionsDomain
import com.joseleandro.fullfocus.domain.repository.PomodoroSessionRepository
import kotlinx.coroutines.flow.Flow

class GetTaskEndPomodoroSessionUseCase(
    private val pomodoroSessionRepository: PomodoroSessionRepository
) {

    operator fun invoke(): Flow<TaskWithPomodoroSessionsDomain?> =
        pomodoroSessionRepository.taskAndPomodoroSessionsCurrent

}