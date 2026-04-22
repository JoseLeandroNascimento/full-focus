package com.joseleandro.fullfocus.data.datasource

import com.joseleandro.fullfocus.data.local.database.dao.TaskDao
import com.joseleandro.fullfocus.data.local.database.mapper.toDomain
import com.joseleandro.fullfocus.data.local.database.mapper.toEntity
import com.joseleandro.fullfocus.data.local.database.model.entity.TaskEntity
import com.joseleandro.fullfocus.domain.data.TaskDomain
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

class TaskLocalDataSourceImpl(
    private val taskDao: TaskDao,
    private val taskFilterLocalPreferencesDataSource: TaskFilterLocalPreferencesDataSource
) : TaskLocalDataSource {

    override val tasksAll: Flow<List<TaskDomain>>
        get() = taskDao.getAll().map { it.toDomain() }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val tasksFiltered: Flow<List<TaskDomain>> =
        taskFilterLocalPreferencesDataSource.taskFilter
            .map { it.tagFilter }
            .distinctUntilChanged()
            .flatMapLatest { tagId ->
                taskDao.getByFilter(tagId = tagId).map { it.toDomain() }
            }


    override suspend fun save(task: TaskDomain) {

        val data: TaskEntity = task.toEntity()

        if (data.id == 0) {
            taskDao.save(data)
        } else {
            taskDao.update(
                id = data.id,
                title = data.title,
                pomodoros = data.estimatedPomodoros,
                progress = data.completedPomodoros,
                tag = data.tagId,
                updatedAt = System.currentTimeMillis()
            )
        }

    }

    override fun getTaskById(id: Int): Flow<TaskDomain?> =
        taskDao.getById(id = id).map { it?.toDomain() }

    override suspend fun setProgressPomodoro(id: Int, progress: Int) =
        taskDao.updateProgressPomodoro(idTask = id, progress = progress)

}