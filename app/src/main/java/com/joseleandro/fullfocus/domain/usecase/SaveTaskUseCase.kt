package com.joseleandro.fullfocus.domain.usecase

import com.joseleandro.fullfocus.domain.data.TaskDomain
import com.joseleandro.fullfocus.domain.repository.TaskRepository

class SaveTaskUseCase(
    private val taskRepository: TaskRepository
) {

    suspend operator fun invoke(title: String, pomodoros: Int) {

        val task = TaskDomain(
            title = title,
            pomodoros = pomodoros
        )

        taskRepository.save(task)
    }
}