package com.joseleandro.fullfocus.data.local.database.mapper

import com.joseleandro.fullfocus.data.local.database.model.dto.TaskWithPomodoroSessionsDto
import com.joseleandro.fullfocus.domain.data.TaskWithPomodoroSessionsDomain

fun TaskWithPomodoroSessionsDto.toDomain(): TaskWithPomodoroSessionsDomain =
    TaskWithPomodoroSessionsDomain(
        task = task.toDomain(),
        sessions = sessions.toDomain()
    )