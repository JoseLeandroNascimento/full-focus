package com.joseleandro.fullfocus.domain.data

import com.joseleandro.fullfocus.data.local.database.model.enums.SessionStatus
import com.joseleandro.fullfocus.data.local.preferences.data.enums.StatusSession

data class PomodoroSessionDomain(
    val id: Int = 0,
    val taskId: Int,
    val type: StatusSession,
    val status: SessionStatus,
    val startedAt: Long,
    val endedAt: Long? = null,
    val duration: Long,
)
