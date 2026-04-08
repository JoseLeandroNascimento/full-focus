package com.joseleandro.fullfocus.domain.repository

import com.joseleandro.fullfocus.domain.data.TaskDomain
import kotlinx.coroutines.flow.Flow

interface TaskRepository {

    val tasks: Flow<List<TaskDomain>>

    suspend fun save(task: TaskDomain)

}