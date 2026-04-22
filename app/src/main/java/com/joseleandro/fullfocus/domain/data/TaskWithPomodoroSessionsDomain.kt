package com.joseleandro.fullfocus.domain.data

data class TaskWithPomodoroSessionsDomain(
    val task: TaskDomain,
    val sessions: List<PomodoroSessionDomain>
)
