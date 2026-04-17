package com.joseleandro.fullfocus.domain.usecase

import com.joseleandro.fullfocus.domain.data.TaskDomain
import com.joseleandro.fullfocus.domain.repository.PomodoroTimeRepository
import com.joseleandro.fullfocus.domain.repository.TaskRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest

class GetTaskCurrentPomodoroUseCase(
    private val pomodoroTimeRepository: PomodoroTimeRepository,
    private val taskRepository: TaskRepository
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<TaskDomain?> {
        return pomodoroTimeRepository.pomodoroFlow.flatMapLatest { pomodoroTime ->
            taskRepository.getTaskById(id = pomodoroTime.idTask ?: 0)
        }
    }
}