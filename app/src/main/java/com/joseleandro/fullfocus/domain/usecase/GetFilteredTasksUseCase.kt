package com.joseleandro.fullfocus.domain.usecase

import com.joseleandro.fullfocus.domain.repository.TaskRepository

class GetFilteredTasksUseCase(
    private val taskRepository: TaskRepository
) {

    operator fun invoke() = taskRepository.tasksFiltered

}