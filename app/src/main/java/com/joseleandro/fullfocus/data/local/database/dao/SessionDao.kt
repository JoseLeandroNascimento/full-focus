package com.joseleandro.fullfocus.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.joseleandro.fullfocus.data.local.database.model.SessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SessionDao {

    @Query("SELECT * FROM session_table WHERE status != 'CANCEL' AND status != 'COMPLETED' AND status != 'SKIPPED' ORDER BY id DESC LIMIT 1")
    fun getSessionCurrent(): Flow<SessionEntity?>

    @Insert
    suspend fun save(data: SessionEntity): Long

    @Update
    suspend fun update(data: SessionEntity): Int

    @Query("SELECT * FROM session_table WHERE pomodoro_id = :pomodoroId ORDER BY id DESC LIMIT 1")
    suspend fun getLastSessionByPomodoroId(pomodoroId: Long): SessionEntity?

    @Query("SELECT COUNT(*) FROM session_table WHERE pomodoro_id = :pomodoroId AND state = 'FOCUS' AND (status = 'COMPLETED' OR status = 'SKIPPED')")
    suspend fun getFocusCountByPomodoroId(pomodoroId: Long): Int

    @Query("SELECT COUNT(*) FROM session_table WHERE pomodoro_id = :pomodoroId AND state = 'FOCUS' AND (status = 'COMPLETED' OR status = 'SKIPPED')")
    fun getFocusCountByPomodoroIdFlow(pomodoroId: Long): Flow<Int>

    @Query("SELECT COUNT(*) FROM session_table WHERE pomodoro_id = :pomodoroId AND state != 'FOCUS' AND (status = 'COMPLETED' OR status = 'SKIPPED')")
    fun getCompletedPomodoroCountFlow(pomodoroId: Long): Flow<Int>
    
}