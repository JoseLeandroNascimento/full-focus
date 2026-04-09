package com.joseleandro.fullfocus.domain.repository

import com.joseleandro.fullfocus.data.local.preferences.data.TaskFilterPreferences
import kotlinx.coroutines.flow.Flow

interface TaskFilterPreferencesRepository {

    val taskFilter: Flow<TaskFilterPreferences>

    suspend fun updateTaskFilter(idTag: Int?)

}