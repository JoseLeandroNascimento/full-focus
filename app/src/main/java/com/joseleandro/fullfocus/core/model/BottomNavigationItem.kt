package com.joseleandro.fullfocus.core.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class BottomNavigationItem(
    val route: TabScreen,
    @get:StringRes val labelRes: Int,
    @get:DrawableRes val iconRes: Int,
    @get:DrawableRes val selectedIconRes: Int? = null
)