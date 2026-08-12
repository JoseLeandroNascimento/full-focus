package com.joseleandro.fullfocus.ui.screen.score

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joseleandro.fullfocus.data.local.database.model.PomodoroState
import com.joseleandro.fullfocus.data.local.database.model.SessionStatus
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

@OptIn(ExperimentalCoroutinesApi::class)
class ScoreViewModel(
    private val statisticRepository: StatisticRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ScoreUiState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: ScoreEvent) {
        when (event) {
            ScoreEvent.OnLoad -> load()
        }
    }

    private fun load() {
        viewModelScope.launch {
            statisticRepository.getAllPomodorosWithSessions().collect { pomodoroWidthSessions ->

                val today = LocalDate.now(ZoneId.systemDefault())
                val startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY))
                val endOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY))

                val allPomodoroCompleted = pomodoroWidthSessions.filter { it.pomodoro.completed }
                
                val allSessionsFocusCompleted = allPomodoroCompleted.flatMap { it.sessions }
                    .filter { it.state == PomodoroState.FOCUS && it.status == SessionStatus.COMPLETED }

                val currentWeekSessions = allSessionsFocusCompleted.filter { session ->
                    val sessionDate = Instant.ofEpochMilli(session.createdAt)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                    !sessionDate.isBefore(startOfWeek) && !sessionDate.isAfter(endOfWeek)
                }

                val dateTimeWithSessionsGroup =
                    currentWeekSessions
                        .groupBy { session ->
                            Instant.ofEpochMilli(session.createdAt)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                                .atStartOfDay(ZoneId.systemDefault())
                                .toInstant()
                                .toEpochMilli()
                        }

                _uiState.update { state ->
                    state.copy(
                        dateTimeWithSessionGroup = dateTimeWithSessionsGroup
                    )
                }
            }
        }
    }



    companion object {
    }
}
