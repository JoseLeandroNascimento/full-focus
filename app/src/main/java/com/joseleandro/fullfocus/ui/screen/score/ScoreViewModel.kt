package com.joseleandro.fullfocus.ui.screen.score

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joseleandro.fullfocus.domain.model.StatisticDomain
import com.joseleandro.fullfocus.domain.usecase.GetStatisticsUseCase
import com.joseleandro.fullfocus.ui.state.AchievementUiState
import com.joseleandro.fullfocus.ui.state.ScoreUiState
import com.joseleandro.fullfocus.ui.state.WeeklyHistoryData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.time.LocalDate
import java.time.YearMonth

@OptIn(ExperimentalCoroutinesApi::class)
class ScoreViewModel(
    private val getStatisticsUseCase: GetStatisticsUseCase
) : ViewModel() {

    private val _selectedDate = MutableStateFlow(LocalDate.now())
    private val _selectedChartPeriod = MutableStateFlow("Por semana")
    
    private val _uiState = MutableStateFlow(ScoreUiState())
    val uiState: StateFlow<ScoreUiState> = _uiState.asStateFlow()

    private var latestStats: StatisticDomain? = null

    init {
        loadStatistics()
    }

    private fun loadStatistics() {
        _selectedDate.flatMapLatest { date ->
            // Performance: Heavy calculations moved to Dispatchers.Default
            getStatisticsUseCase(date).flowOn(Dispatchers.Default)
        }.onEach { stats ->
            latestStats = stats
            updateUiStateWithStats(stats)
        }.launchIn(viewModelScope)
    }

    private fun updateUiStateWithStats(stats: StatisticDomain) {
        val currentPeriod = _selectedChartPeriod.value
        val activityData = if (currentPeriod == "Por semana") {
            stats.weeklyActivity
        } else {
            stats.monthlyActivity
        }

        _uiState.value = _uiState.value.copy(
            dailyStreak = stats.dailyStreak,
            highestStreak = stats.highestStreak,
            totalFocusTimeToday = stats.totalFocusTimeToday.formattedTime(),
            totalHoursMonth = stats.totalFocusTimeMonth.formattedTime(),
            focusSessionsCompleted = stats.focusSessionsCompleted,
            pomodorosCompleted = stats.pomodorosCompleted,
            averageFocusTime = stats.averageFocusTimePerDay.formattedTime(),
            weeklyGoalProgress = stats.weeklyGoalProgress,
            monthlyGoalDays = stats.monthlyGoalDays,
            monthlyGoalTotal = stats.monthlyGoalTotal,
            consistencyRate = stats.consistencyRate,
            currentMonthLabel = stats.currentMonthLabel,
            calendarMonthName = stats.calendarMonthName,
            monthlyStrikeLabel = "${stats.monthlyGoalDays} dias",
            focusedDates = stats.focusedDates,
            currentYearMonth = YearMonth.from(_selectedDate.value),
            selectedChartPeriod = currentPeriod,
            weeklyActivity = activityData.map { (label, progress, timeLabel) ->
                WeeklyHistoryData(
                    label = label,
                    subLabel = stats.calendarMonthName.take(3),
                    value = progress,
                    timeLabel = timeLabel
                )
            },
            achievements = stats.achievements.map { achievement ->
                AchievementUiState(
                    id = achievement.id,
                    title = achievement.title,
                    description = achievement.description,
                    iconRes = achievement.iconRes,
                    isUnlocked = achievement.isUnlocked,
                    colorHex = achievement.colorHex
                )
            }
        )
    }

    fun onPreviousMonth() {
        _selectedDate.value = _selectedDate.value.minusMonths(1)
    }

    fun onNextMonth() {
        val nextMonth = _selectedDate.value.plusMonths(1)
        if (!nextMonth.isAfter(LocalDate.now())) {
            _selectedDate.value = nextMonth
        }
    }

    fun onMonthChanged(yearMonth: YearMonth) {
        val currentDate = _selectedDate.value
        if (currentDate.year != yearMonth.year || currentDate.month != yearMonth.month) {
            _selectedDate.value = yearMonth.atDay(1)
        }
    }

    fun onChartPeriodSelected(period: String) {
        _selectedChartPeriod.value = period
        latestStats?.let { updateUiStateWithStats(it) }
    }

    private fun Long.formattedTime(): String {
        val totalMinutes = this / (1000 * 60)
        val hours = totalMinutes / 60
        val minutes = totalMinutes % 60
        return if (hours > 0) "${hours}h ${minutes}m" else "${minutes}m"
    }
}
