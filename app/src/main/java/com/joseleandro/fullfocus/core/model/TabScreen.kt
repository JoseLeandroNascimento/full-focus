package com.joseleandro.fullfocus.core.model

import com.joseleandro.fullfocus.R
import kotlinx.serialization.Serializable

interface TabScreen {

    @Serializable
    data object PomodoroTabScreen : TabScreen

    @Serializable
    data object ScoreTabScreen : TabScreen

    companion object {

        val items = listOf(
            BottomNavigationItem(
                route = TabScreen.PomodoroTabScreen,
                labelRes = R.string.pomodoro,
                iconRes = R.drawable.mingcute_time_line,
            ),
            BottomNavigationItem(
                route = TabScreen.ScoreTabScreen,
                labelRes = R.string.score,
                iconRes = R.drawable.outline_bar_chart_24,
            )
        )

    }
}

val TabScreen.index: Int
    get() = TabScreen.items.indexOfFirst { it.route == this }
