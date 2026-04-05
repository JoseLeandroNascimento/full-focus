package com.joseleandro.fullfocus.core.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.joseleandro.fullfocus.R

enum class ETabScreen(
    @get:StringRes val label: Int,
    @get:DrawableRes val iconRes: Int,
    val route: TabScreen
) {
    POMODORO(
        label = R.string.pomodoro,
        iconRes = R.drawable.baseline_access_time_filled_24,
        route = TabScreen.PomodoroScreen
    ),

    LIST_TASKS(
        label = R.string.tarefas,
        iconRes = R.drawable.boxicons_seal_check_filled,
        route = TabScreen.ListTasksScreen
    ),

    REPORT(
        label = R.string.relat_rio,
        iconRes = R.drawable.rounded_bar_chart_24,
        route = TabScreen.ReportScreen
    )
}