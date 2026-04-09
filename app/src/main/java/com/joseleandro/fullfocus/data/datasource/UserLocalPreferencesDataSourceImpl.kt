package com.joseleandro.fullfocus.data.datasource

import androidx.datastore.core.DataStore
import com.joseleandro.fullfocus.data.local.preferences.UserPreferences
import com.joseleandro.fullfocus.data.local.preferences.data.TaskFilterPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserLocalPreferencesDataSourceImpl(
    private val dataStore: DataStore<UserPreferences>
) : UserLocalPreferencesDataSource {

    override val taskFilter: Flow<TaskFilterPreferences>
        get() = dataStore.data.map { it.taskFilter }


    override suspend fun updateTaskFilter(idTag: Int?) {
        dataStore.updateData {
            it.copy(
                taskFilter = it.taskFilter.copy(
                    tagFilter = idTag
                )
            )
        }
    }
}