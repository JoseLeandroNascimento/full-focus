package com.joseleandro.fullfocus.domain.data

import androidx.compose.ui.graphics.Color

data class TagWithTasksDetailsDomain(
    val id: Int = 0,
    val name: String,
    val color: Color,
    val countTasks: Int = 0
)
