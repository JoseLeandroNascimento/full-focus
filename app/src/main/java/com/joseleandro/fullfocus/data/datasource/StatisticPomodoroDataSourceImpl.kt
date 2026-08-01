package com.joseleandro.fullfocus.data.datasource

import com.joseleandro.fullfocus.R
import com.joseleandro.fullfocus.data.local.database.dao.PomodoroDao
import com.joseleandro.fullfocus.data.local.database.dao.SessionDao
import com.joseleandro.fullfocus.data.local.database.model.PomodoroState
import com.joseleandro.fullfocus.data.local.database.model.PomodoroWithSessions
import com.joseleandro.fullfocus.data.local.database.model.SessionStatus
import com.joseleandro.fullfocus.domain.model.AchievementDomain
import com.joseleandro.fullfocus.domain.model.StatisticDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.Locale

class StatisticPomodoroDataSourceImpl(
    private val pomodoroDao: PomodoroDao,
    private val sessionDao: SessionDao
) : StatisticPomodoroDataSource {

    override val statistic: Flow<StatisticDomain>
        get() = getStatisticByMonth(LocalDate.now())

    override fun getStatisticByMonth(date: LocalDate): Flow<StatisticDomain> {
        return pomodoroDao.getAllPomodorosWithSessions().map { pomodorosWithSessions ->
            calculateStatistics(pomodorosWithSessions, date)
        }
    }

    private fun calculateStatistics(pomodoros: List<PomodoroWithSessions>, referenceDate: LocalDate): StatisticDomain {
        val zoneId = ZoneId.systemDefault()
        val locale = Locale.forLanguageTag("pt-BR")
        val today = LocalDate.now()

        // Filter valid focus sessions (COMPLETED for count, but CANCEL/SKIPPED with time for stats)
        val allFocusSessions = pomodoros.flatMap { it.sessions }
            .filter { it.state == PomodoroState.FOCUS }

        val sessionsForStats = allFocusSessions.filter {
            it.status == SessionStatus.COMPLETED || 
            ((it.status == SessionStatus.CANCEL || it.status == SessionStatus.SKIPPED) && it.elapsedTime > 0)
        }

        // Group sessions by date for streaks and achievements - USE session.createdAt
        val allSessionsByDate = sessionsForStats.groupBy {
            Instant.ofEpochMilli(it.createdAt).atZone(zoneId).toLocalDate()
        }

        val allFocusedDates = allSessionsByDate.keys.sortedDescending()

        // Global metrics for achievements
        val globalHighestStreak = calculateHighestStreak(allFocusedDates)
        val globalTotalCompletedSessions = allFocusSessions.count { it.status == SessionStatus.COMPLETED }

        // Reference month metrics
        val monthStart = referenceDate.withDayOfMonth(1)
        val monthEnd = referenceDate.withDayOfMonth(referenceDate.lengthOfMonth())
        
        val sessionsInMonthForStats = sessionsForStats.filter {
            val date = Instant.ofEpochMilli(it.createdAt).atZone(zoneId).toLocalDate()
            !date.isBefore(monthStart) && !date.isAfter(monthEnd)
        }

        val totalTimeMonth = sessionsInMonthForStats.sumOf { it.elapsedTime }
        val focusedDatesInMonth = allSessionsByDate.keys.filter { !it.isBefore(monthStart) && !it.isAfter(monthEnd) }
        val monthlyGoalDays = focusedDatesInMonth.size
        val monthlyGoalTotal = referenceDate.lengthOfMonth()

        // Streak for the Hero Card
        val currentStreak = calculateCurrentStreak(allFocusedDates)

        // Today's metrics (only if reference month is current month)
        val isCurrentMonth = referenceDate.month == today.month && referenceDate.year == today.year
        val totalTimeToday = if (isCurrentMonth) allSessionsByDate[today]?.sumOf { it.elapsedTime } ?: 0L else 0L

        // Weekly Activity
        val chartEndDate = if (isCurrentMonth) today else monthEnd
        val weeklyActivity = (0..6).reversed().map { daysAgo ->
            val date = chartEndDate.minusDays(daysAgo.toLong())
            val label = date.dayOfWeek.getDisplayName(TextStyle.SHORT, locale).replaceFirstChar { it.uppercase() }
            val dailySessions = allSessionsByDate[date] ?: emptyList()
            val dailyTime = dailySessions.sumOf { it.elapsedTime }
            val maxDailyGoal = 8 * 60 * 60 * 1000L
            val dayProgress = (dailyTime.toFloat() / maxDailyGoal).coerceIn(0f, 1f)
            Triple(label, dayProgress, dailyTime.formattedTimeShort())
        }

        // Weekly Goal Progress (for reference week) - Counting completed sessions
        val startOfWeek = referenceDate.minusDays(referenceDate.dayOfWeek.value.toLong() - 1)
        val sessionsCompletedThisWeek = allFocusSessions.count {
            val date = Instant.ofEpochMilli(it.createdAt).atZone(zoneId).toLocalDate()
            it.status == SessionStatus.COMPLETED && !date.isBefore(startOfWeek) && !date.isAfter(referenceDate)
        }
        val weeklyGoalProgress = "$sessionsCompletedThisWeek/20"

        // Labels
        val currentMonthLabel = "${referenceDate.month.getDisplayName(TextStyle.FULL, locale).replaceFirstChar { it.uppercase() }} ${referenceDate.year}"
        val calendarMonthName = referenceDate.month.getDisplayName(TextStyle.FULL, locale).replaceFirstChar { it.uppercase() }

        val achievements = getAchievements(globalTotalCompletedSessions, globalHighestStreak)

        return StatisticDomain(
            dailyStreak = currentStreak,
            highestStreak = globalHighestStreak,
            totalFocusTimeToday = totalTimeToday,
            totalFocusTimeMonth = totalTimeMonth,
            focusSessionsCompleted = sessionsInMonthForStats.count { it.status == SessionStatus.COMPLETED },
            pomodorosCompleted = pomodoros.count { 
                val date = Instant.ofEpochMilli(it.pomodoro.createAt).atZone(zoneId).toLocalDate()
                it.pomodoro.completed && !date.isBefore(monthStart) && !date.isAfter(monthEnd)
            },
            averageFocusTimePerDay = if (focusedDatesInMonth.isNotEmpty()) totalTimeMonth / focusedDatesInMonth.size else 0L,
            weeklyGoalProgress = weeklyGoalProgress,
            monthlyGoalDays = monthlyGoalDays,
            monthlyGoalTotal = monthlyGoalTotal,
            consistencyRate = consistencyRate(referenceDate, allSessionsByDate.keys),
            weeklyActivity = weeklyActivity,
            currentMonthLabel = currentMonthLabel,
            calendarMonthName = calendarMonthName,
            focusedDates = focusedDatesInMonth.sorted(),
            achievements = achievements
        )
    }

    private fun consistencyRate(referenceDate: LocalDate, focusedDates: Set<LocalDate>): Int {
        val thirtyDaysBeforeRef = referenceDate.minusDays(29)
        val activeDaysLast30 = focusedDates.count { !it.isBefore(thirtyDaysBeforeRef) && !it.isAfter(referenceDate) }
        return (activeDaysLast30 * 100) / 30
    }

    private fun getAchievements(totalSessions: Int, highestStreak: Int): List<AchievementDomain> {
        return listOf(
            AchievementDomain(
                id = "1",
                title = "Primeira semana",
                description = "7 dias seguidos",
                iconRes = R.drawable.fluent_emoji_flat_fire,
                isUnlocked = highestStreak >= 7,
                colorHex = 0xFFFF8C00
            ),
            AchievementDomain(
                id = "2",
                title = "Um mês focado",
                description = "30 dias seguidos",
                iconRes = R.drawable.mynaui_coffee,
                isUnlocked = highestStreak >= 30,
                colorHex = 0xFFE91E63
            ),
            AchievementDomain(
                id = "3",
                title = "Foco extremo",
                description = "100 sessões",
                iconRes = R.drawable.boxicons_timer,
                isUnlocked = totalSessions >= 100,
                colorHex = 0xFF9C27B0
            ),
            AchievementDomain(
                id = "4",
                title = "Disciplina total",
                description = "15 dias seguidos",
                iconRes = R.drawable.lucide_lab_farm,
                isUnlocked = highestStreak >= 15,
                colorHex = 0xFF4CAF50
            )
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
            } else if (date.isBefore(expected)) {
                break
            }
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
            if (expected == null || date == expected) {
                current++
            } else {
                max = maxOf(max, current)
                current = 1
            }
            expected = date.plusDays(1)
        }
        return maxOf(max, current)
    }
}
