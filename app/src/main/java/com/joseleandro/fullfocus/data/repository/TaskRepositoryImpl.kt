package com.joseleandro.fullfocus.data.repository

import com.joseleandro.fullfocus.data.datasource.TaskLocalDataSource
import com.joseleandro.fullfocus.domain.data.TaskDomain
import com.joseleandro.fullfocus.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow

class TaskRepositoryImpl(
    private val taskLocalDataSource: TaskLocalDataSource
) : TaskRepository {

    override val tasksAll: Flow<List<TaskDomain>>
        get() = taskLocalDataSource.tasksAll

    override val tasksFiltered: Flow<List<TaskDomain>>
        get() = taskLocalDataSource.tasksFiltered

    override suspend fun save(task: TaskDomain) {
        taskLocalDataSource.save(task)
    }
}