package com.joseleandro.fullfocus.data.local.database.mapper

import com.joseleandro.fullfocus.data.local.database.model.TaskEntity
import com.joseleandro.fullfocus.domain.data.TaskDomain

fun TaskEntity.toDomain(): TaskDomain {

    return TaskDomain(
        id = id,
        title = title,
        pomodoros = pomodoros,
        progress = progress
    )
}

fun List<TaskEntity>.toDomain(): List<TaskDomain> {
    return map { it.toDomain() }
}

fun TaskDomain.toEntity(): TaskEntity {
    return TaskEntity(
        id = id,
        title = title,
        pomodoros = pomodoros,
        isDone = isDone,
        tagId = tag,
        progress = progress
    )
}