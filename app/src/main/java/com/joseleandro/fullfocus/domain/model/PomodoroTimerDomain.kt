package com.joseleandro.fullfocus.domain.model

data class PomodoroTimerDomain(
    val time: Long = 0L,
    val isRunning: Boolean = false,
    val duration: Long = 0L
)
