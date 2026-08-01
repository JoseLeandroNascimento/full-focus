package com.joseleandro.fullfocus.domain.usecase

import com.joseleandro.fullfocus.R
import com.joseleandro.fullfocus.data.local.database.model.PomodoroState
import com.joseleandro.fullfocus.data.local.database.model.PomodoroWithSessions
import com.joseleandro.fullfocus.data.local.database.model.SessionStatus
import com.joseleandro.fullfocus.domain.model.AchievementDomain
import com.joseleandro.fullfocus.domain.model.StatisticDomain
import com.joseleandro.fullfocus.domain.repository.StatisticRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.TextStyle
import java.time.temporal.WeekFields
import java.util.Locale

class GetStatisticsUseCase(
    private val repository: StatisticRepository
) {
    operator fun invoke(referenceDate: LocalDate): Flow<StatisticDomain> {
        val zoneId = ZoneId.systemDefault()
        val locale = Locale.forLanguageTag("pt-BR")
        val today = LocalDate.now()

        return repository.getAllPomodorosWithSessions().map { pomodoros ->
            // Optimization: Filter focus sessions once
            val allFocusSessions = pomodoros.flatMap { it.sessions }
                .filter { it.state == PomodoroState.FOCUS }

            val sessionsForStats = allFocusSessions.filter {
                it.status == SessionStatus.COMPLETED ||
                        ((it.status == SessionStatus.CANCEL || it.status == SessionStatus.SKIPPED) && it.elapsedTime > 0)
            }

            // Map sessions to dates once to avoid redundant conversions
            val sessionsWithDates = sessionsForStats.map {
                it to Instant.ofEpochMilli(it.createdAt).atZone(zoneId).toLocalDate()
            }

            val allSessionsByDate = sessionsWithDates.groupBy { it.second }
                .mapValues { entry -> entry.value.map { it.first } }

            val allFocusedDates = allSessionsByDate.keys.sortedDescending()

            // Global metrics
            val globalHighestStreak = calculateHighestStreak(allFocusedDates)
            val globalTotalCompletedSessions = allFocusSessions.count { it.status == SessionStatus.COMPLETED }

            // Reference month limits
            val monthStart = referenceDate.withDayOfMonth(1)
            val monthEnd = referenceDate.withDayOfMonth(referenceDate.lengthOfMonth())

            val sessionsInMonthForStats = sessionsWithDates.filter { (_, date) ->
                !date.isBefore(monthStart) && !date.isAfter(monthEnd)
            }.map { it.first }

            val totalTimeMonth = sessionsInMonthForStats.sumOf { it.elapsedTime }
            val focusedDatesInMonth = allFocusedDates.filter { !it.isBefore(monthStart) && !it.isAfter(monthEnd) }
            
            val isCurrentMonth = referenceDate.month == today.month && referenceDate.year == today.year

            // Weekly Activity (Last 7 days relative to ref date)
            val chartEndDate = if (isCurrentMonth) today else monthEnd
            val weeklyActivity = (0..6).reversed().map { daysAgo ->
                val date = chartEndDate.minusDays(daysAgo.toLong())
                val label = date.dayOfWeek.getDisplayName(TextStyle.SHORT, locale).replaceFirstChar { it.uppercase() }
                val dailyTime = allSessionsByDate[date]?.sumOf { it.elapsedTime } ?: 0L
                val maxDailyGoal = 8 * 60 * 60 * 1000L
                Triple(label, (dailyTime.toFloat() / maxDailyGoal).coerceIn(0f, 1f), dailyTime.formattedTimeShort())
            }

            // Monthly Activity
            val weekFields = WeekFields.of(locale)
            val monthlyActivity = sessionsWithDates.filter { (_, date) ->
                !date.isBefore(monthStart) && !date.isAfter(monthEnd)
            }.groupBy { it.second.get(weekFields.weekOfMonth()) }
                .let { groupedByWeek ->
                    (1..5).map { weekNum ->
                        val weekTime = groupedByWeek[weekNum]?.sumOf { it.first.elapsedTime } ?: 0L
                        val maxWeeklyGoal = 40 * 60 * 60 * 1000L
                        Triple("Sem $weekNum", (weekTime.toFloat() / maxWeeklyGoal).coerceIn(0f, 1f), weekTime.formattedTimeShort())
                    }
                }

            // Weekly Goal (Completed sessions)
            val startOfWeek = referenceDate.minusDays(referenceDate.dayOfWeek.value.toLong() - 1)
            val sessionsCompletedThisWeek = allFocusSessions.count {
                val date = Instant.ofEpochMilli(it.createdAt).atZone(zoneId).toLocalDate()
                it.status == SessionStatus.COMPLETED && !date.isBefore(startOfWeek) && !date.isAfter(referenceDate)
            }

            StatisticDomain(
                dailyStreak = calculateCurrentStreak(allFocusedDates),
                highestStreak = globalHighestStreak,
                totalFocusTimeToday = if (isCurrentMonth) allSessionsByDate[today]?.sumOf { it.elapsedTime } ?: 0L else 0L,
                totalFocusTimeMonth = totalTimeMonth,
                focusSessionsCompleted = sessionsInMonthForStats.count { it.status == SessionStatus.COMPLETED },
                pomodorosCompleted = pomodoros.count {
                    val date = Instant.ofEpochMilli(it.pomodoro.createAt).atZone(zoneId).toLocalDate()
                    it.pomodoro.completed && !date.isBefore(monthStart) && !date.isAfter(monthEnd)
                },
                averageFocusTimePerDay = if (focusedDatesInMonth.isNotEmpty()) totalTimeMonth / focusedDatesInMonth.size else 0L,
                weeklyGoalProgress = "$sessionsCompletedThisWeek/20",
                monthlyGoalDays = focusedDatesInMonth.size,
                monthlyGoalTotal = referenceDate.lengthOfMonth(),
                consistencyRate = calculateConsistency(referenceDate, allSessionsByDate.keys),
                weeklyActivity = weeklyActivity,
                monthlyActivity = monthlyActivity,
                currentMonthLabel = "${referenceDate.month.getDisplayName(TextStyle.FULL, locale).replaceFirstChar { it.uppercase() }} ${referenceDate.year}",
                calendarMonthName = referenceDate.month.getDisplayName(TextStyle.FULL, locale).replaceFirstChar { it.uppercase() },
                focusedDates = focusedDatesInMonth,
                achievements = getAchievements(globalTotalCompletedSessions, globalHighestStreak)
            )
        }
    }

    private fun calculateConsistency(referenceDate: LocalDate, focusedDates: Set<LocalDate>): Int {
        val thirtyDaysBeforeRef = referenceDate.minusDays(29)
        val activeDaysLast30 = focusedDates.count { !it.isBefore(thirtyDaysBeforeRef) && !it.isAfter(referenceDate) }
        return (activeDaysLast30 * 100) / 30
    }

    private fun getAchievements(totalSessions: Int, highestStreak: Int): List<AchievementDomain> {
        return listOf(
            AchievementDomain("1", "Primeira semana", "7 dias seguidos", R.drawable.fluent_emoji_flat_fire, highestStreak >= 7, 0xFFFF8C00),
            AchievementDomain("2", "Um mês focado", "30 dias seguidos", R.drawable.mynaui_coffee, highestStreak >= 30, 0xFFE91E63),
            AchievementDomain("3", "Foco extremo", "100 sessões", R.drawable.boxicons_timer, totalSessions >= 100, 0xFF9C27B0),
            AchievementDomain("4", "Disciplina total", "15 dias seguidos", R.drawable.lucide_lab_farm, highestStreak >= 15, 0xFF4CAF50)
        )
    }

    private fun Long.formattedTimeShort(): String {
        val totalMinutes = this / (1000 * 60)
        val hours = totalMinutes / 60
        val minutes = totalMinutes % 60
        return if (hours > 0) "${hours}h ${minutes}m" else "${minutes}m"
    }

    private fun calculateCurrentStreak(dates: List<LocalDate>): Int {
        if (dates.isEmpty()) return 0
        val today = LocalDate.now()
        val yesterday = today.minusDays(1)
        val firstDate = dates.first()
        if (firstDate != today && firstDate != yesterday) return 0
        var streak = 0
        var expected = firstDate
        for (date in dates) {
            if (date == expected) {
                streak++
                expected = expected.minusDays(1)
            } else if (date.isBefore(expected)) break
        }
        return streak
    }

    private fun calculateHighestStreak(dates: List<LocalDate>): Int {
        if (dates.isEmpty()) return 0
        val sorted = dates.sorted()
        var max = 0
        var current = 0
        var expected: LocalDate? = null
        for (date in sorted) {
            if (expected == null || date == expected) current++
            else {
                max = maxOf(max, current)
                current = 1
            }
            expected = date.plusDays(1)
        }
        return maxOf(max, current)
    }
}
