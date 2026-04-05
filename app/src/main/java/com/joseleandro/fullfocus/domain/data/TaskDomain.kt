package com.joseleandro.fullfocus.domain.data

import java.util.UUID

data class TaskDomain(
    val id: UUID = UUID.randomUUID(),
    val title: String,
    val pomodorosTotal: Int,
    val pomodorosCheck: Int,
    val isDone: Boolean = false
)
