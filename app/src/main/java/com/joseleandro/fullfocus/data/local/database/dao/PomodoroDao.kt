package com.joseleandro.fullfocus.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.joseleandro.fullfocus.data.local.database.model.PomodoroEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PomodoroDao {

    @Query("SELECT * FROM pomodoro_table WHERE completed = 0 ORDER BY id DESC LIMIT 1")
    fun getPomodoroActive(): Flow<PomodoroEntity?>

    @Insert
    suspend fun save(data: PomodoroEntity): Long

    @Update
    suspend fun update(data: PomodoroEntity): Int

    @Query("DELETE FROM pomodoro_table WHERE id = :id")
    suspend fun deleteById(id: Long)
}
