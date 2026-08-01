package com.joseleandro.fullfocus.domain.model

import androidx.annotation.DrawableRes

data class AchievementDomain(
    val id: String,
    val title: String,
    val description: String,
    @DrawableRes val iconRes: Int,
    val isUnlocked: Boolean = false,
    val colorHex: Long = 0xFF25D9FF
)
