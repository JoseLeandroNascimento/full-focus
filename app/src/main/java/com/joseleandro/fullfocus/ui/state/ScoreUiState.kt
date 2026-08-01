package com.joseleandro.fullfocus.ui.state

import androidx.annotation.DrawableRes
import java.time.LocalDate
import java.time.YearMonth

data class ScoreUiState(
    val totalHoursMonth: String = "0h",
    val totalFocusTimeToday: String = "00:00",
    val focusSessionsCompleted: Int = 0,
    val pomodorosCompleted: Int = 0,
    val dailyStreak: Int = 0,
    val highestStreak: Int = 0,
    val averageFocusTime: String = "0h",
    val weeklyGoalProgress: String = "0/0",
    val monthlyGoalDays: Int = 0,
    val monthlyGoalTotal: Int = 0,
    val consistencyRate: Int = 0,
    val weeklyActivity: List<WeeklyHistoryData> = emptyList(),
    val achievements: List<AchievementUiState> = emptyList(),
    val currentMonthLabel: String = "Maio 2025",
    val calendarMonthName: String = "Julho",
    val monthlyStrikeLabel: String = "12 dias",
    val selectedChartPeriod: String = "Por semana",
    val chartPeriodOptions: List<String> = listOf("Por semana", "Por mês"),
    val focusedDates: List<LocalDate> = emptyList(),
    val currentYearMonth: YearMonth = YearMonth.now()
)

data class WeeklyHistoryData(
    val label: String,
    val subLabel: String,
    val value: Float, // 0.0 a 1.0
    val timeLabel: String = ""
)

data class AchievementUiState(
    val id: String,
    val title: String,
    val description: String,
    @DrawableRes val iconRes: Int,
    val isUnlocked: Boolean = false,
    val colorHex: Long = 0xFF25D9FF
)
