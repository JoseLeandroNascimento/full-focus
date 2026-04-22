package com.joseleandro.fullfocus.data.datasource

import com.joseleandro.fullfocus.domain.data.TaskDomain
import kotlinx.coroutines.flow.Flow

interface TaskLocalDataSource {

    val tasksAll: Flow<List<TaskDomain>>

    val tasksFiltered: Flow<List<TaskDomain>>

    suspend fun save(task: TaskDomain)

    fun getTaskById(id: Int): Flow<TaskDomain?>

    suspend fun setProgressPomodoro(id: Int, progress: Int)
}