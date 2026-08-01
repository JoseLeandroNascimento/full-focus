package com.joseleandro.fullfocus.domain.model

import java.time.LocalDate

data class StatisticDomain(
    val dailyStreak: Int = 0,
    val highestStreak: Int = 0,
    val totalFocusTimeToday: Long = 0L,
    val totalFocusTimeMonth: Long = 0L,
    val focusSessionsCompleted: Int = 0,
    val pomodorosCompleted: Int = 0,
    val averageFocusTimePerDay: Long = 0L,
    val weeklyGoalProgress: String = "0/0",
    val monthlyGoalDays: Int = 0,
    val monthlyGoalTotal: Int = 0,
    val consistencyRate: Int = 0,
    val weeklyActivity: List<Triple<String, Float, String>> = emptyList(), // Day label, progress (0.0 to 1.0), and time label
    val currentMonthLabel: String = "",
    val calendarMonthName: String = "",
    val focusedDates: List<LocalDate> = emptyList(),
    val achievements: List<AchievementDomain> = emptyList()
)
