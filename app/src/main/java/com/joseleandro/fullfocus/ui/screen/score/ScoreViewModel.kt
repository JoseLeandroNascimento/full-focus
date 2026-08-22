package com.joseleandro.fullfocus.ui.screen.score

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joseleandro.fullfocus.data.local.database.model.PomodoroState
import com.joseleandro.fullfocus.data.local.database.model.PomodoroWithSessions
import com.joseleandro.fullfocus.data.local.database.model.SessionEntity
import com.joseleandro.fullfocus.data.local.database.model.SessionStatus
import com.joseleandro.fullfocus.domain.model.HeatMapDataDomain
import com.joseleandro.fullfocus.domain.model.HeatMapDomain
import com.joseleandro.fullfocus.domain.repository.StatisticRepository
import com.joseleandro.fullfocus.ui.event.ScoreEvent
import com.joseleandro.fullfocus.ui.state.ScoreUiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.TemporalAdjusters
import java.time.temporal.WeekFields
import java.util.Locale

@OptIn(ExperimentalCoroutinesApi::class)
class ScoreViewModel(
    private val statisticRepository: StatisticRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ScoreUiState())
    val uiState = _uiState.asStateFlow()

    private var allSessionsFocusCompleted: List<SessionEntity> = emptyList()

    fun onEvent(event: ScoreEvent) {
        when (event) {
            ScoreEvent.OnLoad -> load()
            is ScoreEvent.OnDateSelected -> {
                _uiState.update { it.copy(selectedDate = event.date) }
            }
            is ScoreEvent.OnWeekFocusTimeChange -> weekFocusTime(weekFocusTime = event.weekFocusTime )
        }
    }

    private fun load() {
        viewModelScope.launch {
            statisticRepository.getAllPomodorosWithSessions().collect { pomodoroWidthSessions ->
                val today = LocalDate.now(ZoneId.systemDefault())

                val allPomodoroCompleted = getAllPomodoroCompleted(data = pomodoroWidthSessions)
                allSessionsFocusCompleted =
                    getAllSessionsFocusCompleted(data = allPomodoroCompleted)

                updateChartData(uiState.value.weekFocusTime)

                val startOfMonth = today.withDayOfMonth(1)
                val endOfMonth = today.withDayOfMonth(today.lengthOfMonth())

                val monthSessions = getAllSessionFocusByMonth(
                    data = allSessionsFocusCompleted,
                    startOfMonth = startOfMonth,
                    endOfMonth = endOfMonth
                )

                val totalHours = monthSessions
                    .map { session -> session.elapsedTime }
                    .fold(0L) { acc, lng -> acc + lng }
                    .formatMillisToHours()

                val heatMapDataList = calcHeatMapDataList(data = allSessionsFocusCompleted)

                _uiState.update { state ->
                    state.copy(
                        heatMapData = heatMapDataList,
                        totalHours = totalHours
                    )
                }
            }
        }
    }

    private fun weekFocusTime(weekFocusTime: WeekFocusTime) {
        _uiState.update { it.copy(weekFocusTime = weekFocusTime) }
        updateChartData(weekFocusTime)
    }

    private fun updateChartData(mode: WeekFocusTime) {
        val today = LocalDate.now(ZoneId.systemDefault())
        val chartData = when (mode) {
            WeekFocusTime.WEEKLY -> {
                val startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY))
                val endOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY))

                val weekSessions = allSessionsFocusCompleted.filter { session ->
                    val sessionDate = Instant.ofEpochMilli(session.createdAt)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                    !sessionDate.isBefore(startOfWeek) && !sessionDate.isAfter(endOfWeek)
                }

                val grouped = weekSessions.groupBy { session ->
                    Instant.ofEpochMilli(session.createdAt)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                        .dayOfWeek
                }

                val daysSorted = listOf(
                    DayOfWeek.SUNDAY, DayOfWeek.MONDAY, DayOfWeek.TUESDAY,
                    DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY
                )

                daysSorted.associate { day ->
                    day.getDisplayName(java.time.format.TextStyle.SHORT, Locale.getDefault()) to
                            (grouped[day]?.sumOf { it.elapsedTime } ?: 0L)
                }
            }

            WeekFocusTime.MONTHLY -> {
                val startOfMonth = today.withDayOfMonth(1)
                val endOfMonth = today.withDayOfMonth(today.lengthOfMonth())

                val monthSessions = allSessionsFocusCompleted.filter { session ->
                    val sessionDate = Instant.ofEpochMilli(session.createdAt)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                    !sessionDate.isBefore(startOfMonth) && !sessionDate.isAfter(endOfMonth)
                }

                val weekFields = WeekFields.of(Locale.getDefault())
                val grouped = monthSessions.groupBy { session ->
                    val sessionDate = Instant.ofEpochMilli(session.createdAt)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                    sessionDate.get(weekFields.weekOfMonth())
                }

                // Determine number of weeks in the current month
                val maxWeek = endOfMonth.get(weekFields.weekOfMonth())

                (1..maxWeek).associate { weekNumber ->
                    "Sem $weekNumber" to (grouped[weekNumber]?.sumOf { it.elapsedTime } ?: 0L)
                }
            }
        }

        _uiState.update { it.copy(chartData = chartData) }
    }

    fun Long.formatMillisToHours(): String {
        val totalMinutes = this / 60_000
        val hours = totalMinutes / 60
        val minutes = totalMinutes % 60

        return when {
            hours > 0 && minutes > 0 -> "${hours}:${minutes}h"
            hours > 0 -> "${hours}h"
            else -> "${minutes}min"
        }
    }

    private fun getAllPomodoroCompleted(data: List<PomodoroWithSessions>) =
        data.filter { it.pomodoro.completed }

    private fun getAllSessionsFocusCompleted(data: List<PomodoroWithSessions>) =
        data.flatMap { it.sessions }
            .filter { it.state == PomodoroState.FOCUS && it.status == SessionStatus.COMPLETED }

    private fun getAllSessionFocusByMonth(
        data: List<SessionEntity>,
        startOfMonth: LocalDate,
        endOfMonth: LocalDate
    ) =
        data.filter { session ->
            val sessionDate = Instant.ofEpochMilli(session.createdAt)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
            !sessionDate.isBefore(startOfMonth) && !sessionDate.isAfter(endOfMonth)
        }

    private fun calcHeatMapDataList(data: List<SessionEntity>): List<HeatMapDataDomain> =
        data.groupBy { session ->
            Instant.ofEpochMilli(session.createdAt)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
        }.mapValues { (_, sessions) ->
            sessions.sumOf { it.elapsedTime } / 60_000
        }.map { (date, totalMinutes) ->
            val heatMap = when {
                totalMinutes <= 0 -> HeatMapDomain.HEAT_MAP_0
                totalMinutes <= 25 -> HeatMapDomain.HEAT_MAP_1
                totalMinutes <= 50 -> HeatMapDomain.HEAT_MAP_2
                totalMinutes <= 75 -> HeatMapDomain.HEAT_MAP_3
                else -> HeatMapDomain.HEAT_MAP_4
            }
            HeatMapDataDomain(
                date = date,
                timeFocus = totalMinutes,
                heatMap = heatMap
            )
        }

    companion object {
    }
}
