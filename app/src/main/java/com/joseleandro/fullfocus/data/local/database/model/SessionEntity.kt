package com.joseleandro.fullfocus.data.local.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "session_table",
    foreignKeys = [
        ForeignKey(
            entity = PomodoroEntity::class,
            parentColumns = ["id"],
            childColumns = ["pomodoro_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class SessionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo("pomodoro_id")
    val pomodoroId: Long,
    val duration: Long,
    val elapsedTime: Long = 0,
    val lastStartTime: Long? = null,
    val state: PomodoroState,
    val status: SessionStatus
)
