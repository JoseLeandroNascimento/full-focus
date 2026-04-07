package com.joseleandro.fullfocus.data.local.database.mapper

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.fromColorLong
import androidx.compose.ui.graphics.toColorLong
import com.joseleandro.fullfocus.data.local.database.model.TagEntity
import com.joseleandro.fullfocus.domain.data.TagDomain

fun TagEntity.toDomain(): TagDomain =
    TagDomain(
        id = id,
        name = name,
        color = Color.fromColorLong(color)
    )


fun List<TagEntity>.toDomain(): List<TagDomain> =
    map { it.toDomain() }


fun TagDomain.toEntity(): TagEntity =
    TagEntity(
        id = id,
        name = name,
        color = color.toColorLong()
    )