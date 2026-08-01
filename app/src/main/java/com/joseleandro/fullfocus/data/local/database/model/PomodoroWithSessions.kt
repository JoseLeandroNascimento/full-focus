package com.joseleandro.fullfocus.data.local.database.model

import androidx.room.Embedded
import androidx.room.Relation

data class PomodoroWithSessions(
    @Embedded val pomodoro: PomodoroEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "pomodoro_id"
    )
    val sessions: List<SessionEntity>
)
