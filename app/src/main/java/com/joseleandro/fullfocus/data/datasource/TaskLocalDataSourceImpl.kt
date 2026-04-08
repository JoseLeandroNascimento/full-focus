package com.joseleandro.fullfocus.data.datasource

import com.joseleandro.fullfocus.data.local.database.dao.TaskDao
import com.joseleandro.fullfocus.data.local.database.mapper.toDomain
import com.joseleandro.fullfocus.data.local.database.mapper.toEntity
import com.joseleandro.fullfocus.data.local.database.model.TaskEntity
import com.joseleandro.fullfocus.domain.data.TaskDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TaskLocalDataSourceImpl(
    private val taskDao: TaskDao
) : TaskLocalDataSource {

    override val tasks: Flow<List<TaskDomain>>
        get() = taskDao.getAll().map { it.toDomain() }


    override suspend fun save(task: TaskDomain) {

        val data: TaskEntity = task.toEntity()

        if (data.id == 0) {
            taskDao.save(data)
        } else {
            taskDao.update(
                id = data.id,
                title = data.title,
                pomodoros = data.pomodoros,
                progress = data.progress,
                updatedAt = System.currentTimeMillis()
            )
        }

    }
}