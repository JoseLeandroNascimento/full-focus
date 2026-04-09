package com.joseleandro.fullfocus.data.repository

import com.joseleandro.fullfocus.data.datasource.TaskFilterLocalPreferencesDataSource
import com.joseleandro.fullfocus.data.local.preferences.data.TaskFilterPreferences
import com.joseleandro.fullfocus.domain.repository.TaskFilterPreferencesRepository
import kotlinx.coroutines.flow.Flow

class TaskFilterPreferencesRepositoryImpl(
    private val taskFilterLocalPreferencesDataSource: TaskFilterLocalPreferencesDataSource
) : TaskFilterPreferencesRepository {
    override val taskFilter: Flow<TaskFilterPreferences>
        get() = taskFilterLocalPreferencesDataSource.taskFilter


    override suspend fun updateTaskFilter(idTag: Int?) {
        taskFilterLocalPreferencesDataSource.updateTaskFilter(idTag)
    }
}