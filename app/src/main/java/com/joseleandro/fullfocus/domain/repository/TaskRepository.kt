package com.joseleandro.fullfocus.domain.repository

import com.joseleandro.fullfocus.domain.data.TaskDomain
import kotlinx.coroutines.flow.Flow

interface TaskRepository {

    val tasksAll: Flow<List<TaskDomain>>

    val tasksFiltered: Flow<List<TaskDomain>>

    suspend fun save(task: TaskDomain)

    fun getTaskById(id: Int): Flow<TaskDomain?>

    suspend fun setProgressPomodoro(id: Int, progress: Int)
}