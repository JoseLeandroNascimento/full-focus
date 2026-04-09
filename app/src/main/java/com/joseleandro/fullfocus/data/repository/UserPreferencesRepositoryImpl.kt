package com.joseleandro.fullfocus.data.repository

import com.joseleandro.fullfocus.data.datasource.UserLocalPreferencesDataSource
import com.joseleandro.fullfocus.data.local.preferences.data.TaskFilterPreferences
import com.joseleandro.fullfocus.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow

class UserPreferencesRepositoryImpl(
    private val userLocalPreferencesDataSource: UserLocalPreferencesDataSource
) : UserPreferencesRepository {
    override val taskFilter: Flow<TaskFilterPreferences>
        get() = userLocalPreferencesDataSource.taskFilter


    override suspend fun updateTaskFilter(idTag: Int?) {
        userLocalPreferencesDataSource.updateTaskFilter(idTag)
    }
}