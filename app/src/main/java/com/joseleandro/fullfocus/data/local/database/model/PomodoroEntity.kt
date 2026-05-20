package com.joseleandro.fullfocus.data.local.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pomodoro_table")
data class PomodoroEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val completed: Boolean = false,
    val createAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
