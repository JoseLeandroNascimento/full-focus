package com.joseleandro.fullfocus.data.local.database.mapper

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.fromColorLong
import com.joseleandro.fullfocus.data.local.database.model.dto.TagWithTasksDetailsDto
import com.joseleandro.fullfocus.domain.data.TagWithTasksDetailsDomain

fun TagWithTasksDetailsDto.toDomain(): TagWithTasksDetailsDomain =
    TagWithTasksDetailsDomain(
        id = tag.id,
        name = tag.name,
        color = Color.fromColorLong(tag.color),
        countTasks = countTasks
    )

fun List<TagWithTasksDetailsDto>.toDomain(): List<TagWithTasksDetailsDomain> =
    map { it.toDomain() }