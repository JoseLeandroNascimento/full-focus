package com.joseleandro.fullfocus.data.datasource

import com.joseleandro.fullfocus.data.local.preferences.data.TaskFilterPreferences
import kotlinx.coroutines.flow.Flow

interface UserLocalPreferencesDataSource {

    val taskFilter: Flow<TaskFilterPreferences>

    suspend fun updateTaskFilter(idTag: Int?)

}