package com.joseleandro.fullfocus.domain.usecase

import com.joseleandro.fullfocus.domain.repository.TaskRepository

class SetProgressPomodoroUseCase(
    private val taskRepository: TaskRepository
) {

    suspend operator fun invoke(id: Int, progress: Int) =
        taskRepository.setProgressPomodoro(id = id, progress = progress)
}