package com.joseleandro.fullfocus.data.local.database.mapper

import com.joseleandro.fullfocus.data.local.database.model.entity.PomodoroSessionEntity
import com.joseleandro.fullfocus.domain.data.PomodoroSessionDomain

fun PomodoroSessionEntity.toDomain(): PomodoroSessionDomain =
    PomodoroSessionDomain(
        id = id,
        taskId = taskId,
        pomodoroId = pomodoroId,
        sessionId = sessionId,
        duration = duration,
        type = type,
        status = status,
        endedAt = endedAt,
        startedAt = startedAt
    )

fun List<PomodoroSessionEntity>.toDomain(): List<PomodoroSessionDomain> =
    this.map { it.toDomain() }