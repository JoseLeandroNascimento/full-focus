package com.joseleandro.fullfocus.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.joseleandro.fullfocus.data.local.database.model.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Insert
    suspend fun save(task: TaskEntity)

    @Query("UPDATE task_entity SET title = :title, pomodoros = :pomodoros, progress = :progress, updated_at = :updatedAt WHERE id = :id")
    suspend fun update(id: Int, title: String, pomodoros: Int, progress: Int, updatedAt: Long)

    @Query("SELECT * FROM task_entity")
    fun getAll(): Flow<List<TaskEntity>>
}