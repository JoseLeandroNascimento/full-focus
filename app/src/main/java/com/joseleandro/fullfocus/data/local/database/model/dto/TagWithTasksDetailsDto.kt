package com.joseleandro.fullfocus.data.local.database.model.dto

import androidx.room.Embedded
import com.joseleandro.fullfocus.data.local.database.model.entity.TagEntity

data class TagWithTasksDetailsDto(
    @Embedded val tag: TagEntity,
    val countTasks: Int
)