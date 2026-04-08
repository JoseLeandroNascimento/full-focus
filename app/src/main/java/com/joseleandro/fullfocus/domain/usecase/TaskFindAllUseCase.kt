package com.joseleandro.fullfocus.domain.usecase

import com.joseleandro.fullfocus.domain.repository.TaskRepository

class TaskFindAllUseCase(
    private val taskRepository: TaskRepository
) {

    operator fun invoke() = taskRepository.tasks

}