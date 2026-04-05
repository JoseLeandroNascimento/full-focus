package com.joseleandro.fullfocus.domain.data

import androidx.compose.ui.graphics.Color
import java.util.UUID

data class TagDomain(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val color: Color
)
