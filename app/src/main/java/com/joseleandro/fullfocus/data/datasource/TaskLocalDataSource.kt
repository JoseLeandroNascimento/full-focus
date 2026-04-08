package com.joseleandro.fullfocus.data.datasource

import com.joseleandro.fullfocus.domain.data.TaskDomain
import kotlinx.coroutines.flow.Flow

interface TaskLocalDataSource {

    val tasks: Flow<List<TaskDomain>>

    suspend fun save(task: TaskDomain)

}