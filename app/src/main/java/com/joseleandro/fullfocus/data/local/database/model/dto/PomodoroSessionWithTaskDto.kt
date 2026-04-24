package com.joseleandro.fullfocus.data.local.database.model.dto

import androidx.room.Embedded
import androidx.room.Relation
import com.joseleandro.fullfocus.data.local.database.model.entity.PomodoroSessionEntity
import com.joseleandro.fullfocus.data.local.database.model.entity.TaskEntity

data class PomodoroSessionWithTaskDto(
    @Embedded
    val session: PomodoroSessionEntity,

    @Relation(
        parentColumn = "task_id",
        entityColumn = "id"
    )
    val task: TaskEntity?
)
