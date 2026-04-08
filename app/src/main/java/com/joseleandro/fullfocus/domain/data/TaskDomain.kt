package com.joseleandro.fullfocus.domain.data

data class TaskDomain(
    val id: Int = 0,
    val title: String,
    val pomodoros: Int,
    val progress: Int = 0,
    val isDone: Boolean = false
)
