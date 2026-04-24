package com.joseleandro.fullfocus.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.joseleandro.fullfocus.data.local.database.model.dto.PomodoroSessionWithTaskDto
import com.joseleandro.fullfocus.data.local.database.model.dto.TaskWithPomodoroSessionsDto
import com.joseleandro.fullfocus.data.local.database.model.entity.PomodoroSessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PomodoroSessionDao {

    @Insert
    suspend fun create(pomodoroSession: PomodoroSessionEntity)

    @Query(
        """
            SELECT * FROM pomodoro_session_table 
            WHERE status = 'IN_PROGRESS'
            LIMIT 1
    """
    )
    suspend fun getActiveSession(): PomodoroSessionEntity?

    @Transaction
    @Query("SELECT * FROM pomodoro_session_table WHERE status = 'IN_PROGRESS' LIMIT 1")
    fun getActiveSessionWithTask(): Flow<PomodoroSessionWithTaskDto?>

    @Transaction
    @Query("SELECT * FROM task_entity WHERE id = :taskId")
    fun getTaskWithSessions(taskId: Int): Flow<TaskWithPomodoroSessionsDto>

    @Update
    suspend fun update(pomodoroSession: PomodoroSessionEntity)
}
