package com.joseleandro.fullfocus.data.local.database.model

import androidx.room.Embedded

data class TagWithTasksDetailsDto(
    @Embedded val tag: TagEntity,
    val countTasks: Int
)
