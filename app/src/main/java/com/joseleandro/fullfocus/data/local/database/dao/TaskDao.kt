package com.joseleandro.fullfocus.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.joseleandro.fullfocus.data.local.database.model.entity.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Insert
    suspend fun save(task: TaskEntity)

    @Query("UPDATE task_entity SET title = :title, estimatedPomodoros = :pomodoros, completedPomodoros = :progress, tag_id = :tag, updated_at = :updatedAt WHERE id = :id")
    suspend fun update(
        id: Int,
        title: String,
        pomodoros: Int,
        progress: Int,
        tag: Int? = null,
        updatedAt: Long
    )

    @Query("UPDATE task_entity SET completedPomodoros = :progress WHERE id = :idTask")
    suspend fun updateProgressPomodoro(idTask: Int, progress: Int)

    @Query("SELECT * FROM task_entity WHERE id = :id")
    fun getById(id: Int): Flow<TaskEntity?>

    @Query("SELECT * FROM task_entity")
    fun getAll(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM task_entity WHERE (:tagId IS NULL OR tag_id = :tagId)")
    fun getByFilter(
        tagId: Int? = null
    ): Flow<List<TaskEntity>>
}