package com.joseleandro.fullfocus.data.local.database.model.dto

import androidx.room.Embedded
import androidx.room.Relation
import com.joseleandro.fullfocus.data.local.database.model.entity.PomodoroSessionEntity
import com.joseleandro.fullfocus.data.local.database.model.entity.TaskEntity

data class TaskWithPomodoroSessionsDto(
    @Embedded
    val task: TaskEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "task_id"
    )
    val sessions: List<PomodoroSessionEntity>
)
