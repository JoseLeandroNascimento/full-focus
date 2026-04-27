package com.joseleandro.fullfocus.data.local.database.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.joseleandro.fullfocus.data.local.database.model.enums.SessionStatus
import com.joseleandro.fullfocus.data.local.preferences.data.enums.StatusSession

@Entity(
    tableName = "pomodoro_session_table",
    foreignKeys = [
        ForeignKey(
            entity = TaskEntity::class,
            parentColumns = ["id"],
            childColumns = ["task_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["task_id"]),
        Index(value = ["pomodoro_id"])
    ]
)
data class PomodoroSessionEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "task_id")
    val taskId: Int? = null,

    @ColumnInfo(name = "pomodoro_id")
    val pomodoroId: String,

    @ColumnInfo(name = "session_id")
    val sessionId: String,

    val type: StatusSession,

    val status: SessionStatus,

    val startedAt: Long,
    val endedAt: Long? = null,

    val duration: Long,

    val createdAt: Long = System.currentTimeMillis()
)