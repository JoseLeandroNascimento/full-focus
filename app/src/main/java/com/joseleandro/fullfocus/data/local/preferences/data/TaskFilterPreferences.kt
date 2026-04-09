package com.joseleandro.fullfocus.data.local.preferences.data

import kotlinx.serialization.Serializable

@Serializable
data class TaskFilterPreferences(
    val tagFilter: Int? = null
)
