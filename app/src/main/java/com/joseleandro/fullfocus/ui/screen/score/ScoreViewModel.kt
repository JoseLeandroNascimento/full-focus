package com.joseleandro.fullfocus.ui.screen.score

import androidx.lifecycle.ViewModel
import com.joseleandro.fullfocus.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ScoreViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ScoreUiState())
    val uiState: StateFlow<ScoreUiState> = _uiState.asStateFlow()

    init {
        _uiState.value = ScoreUiState(
            totalHoursMonth = "48h 32m",
            totalFocusTimeToday = "04:20",
            focusSessionsCompleted = 386,
            pomodorosCompleted = 386, // Seguindo exemplo da imagem
            dailyStreak = 12,
            highestStreak = 21,
            streakFreezes = 1,
            averageFocusTime = "3h 15m",
            weeklyGoalProgress = "20/35",
            monthlyGoalDays = 16,
            monthlyGoalTotal = 31,
            consistencyRate = 87,
            currentMonthLabel = "Maio 2025",
            calendarMonthName = "Julho",
            monthlyStrikeLabel = "12 dias",
            weeklyActivity = listOf(
                WeeklyHistoryData("Seg", "Abr", 0.4f),
                WeeklyHistoryData("Ter", "Abr", 0.6f),
                WeeklyHistoryData("Qua", "Abr", 0.85f),
                WeeklyHistoryData("Qui", "Mai", 0.55f),
                WeeklyHistoryData("Sex", "Mai", 0.65f),
                WeeklyHistoryData("Sáb", "Mai", 0.75f),
                WeeklyHistoryData("Dom", "Mai", 0.95f, "11h")
            ),
            achievements = listOf(
                AchievementUiState("1", "Primeira semana", "7 dias seguidos", R.drawable.fluent_emoji_flat_fire, true, 0xFFFF8C00),
                AchievementUiState("2", "Um mês focado", "30 dias seguidos", R.drawable.mynaui_coffee, true, 0xFFE91E63),
                AchievementUiState("3", "Foco extremo", "100 pomodoros", R.drawable.boxicons_timer, true, 0xFF9C27B0),
                AchievementUiState("4", "Disciplina total", "15 dias seguidos", R.drawable.lucide_lab_farm, false, 0xFF4CAF50)
            )
        )
    }

    fun onPreviousMonth() {
        // Lógica mockada para mudar mês
    }

    fun onNextMonth() {
        // Lógica mockada para mudar mês
    }

    fun onChartPeriodSelected(period: String) {
        val newActivity = if (period == "Por semana") {
            listOf(
                WeeklyHistoryData("Seg", "Abr", 0.4f),
                WeeklyHistoryData("Ter", "Abr", 0.6f),
                WeeklyHistoryData("Qua", "Abr", 0.85f),
                WeeklyHistoryData("Qui", "Mai", 0.55f),
                WeeklyHistoryData("Sex", "Mai", 0.65f),
                WeeklyHistoryData("Sáb", "Mai", 0.75f),
                WeeklyHistoryData("Dom", "Mai", 0.95f, "11h")
            )
        } else {
            listOf(
                WeeklyHistoryData("Sem 1", "Mai", 0.7f, "32h"),
                WeeklyHistoryData("Sem 2", "Mai", 0.5f, "24h"),
                WeeklyHistoryData("Sem 3", "Mai", 0.9f, "42h"),
                WeeklyHistoryData("Sem 4", "Mai", 0.8f, "38h")
            )
        }

        _uiState.value = _uiState.value.copy(
            selectedChartPeriod = period,
            weeklyActivity = newActivity
        )
    }
}
