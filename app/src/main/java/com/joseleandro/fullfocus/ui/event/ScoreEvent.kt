package com.joseleandro.fullfocus.ui.event

import com.joseleandro.fullfocus.ui.screen.score.WeekFocusTime

sealed interface ScoreEvent {

    data object OnLoad : ScoreEvent
    data class OnDateSelected(val date: java.time.LocalDate?) : ScoreEvent
    data class OnWeekFocusTimeChange(val weekFocusTime: WeekFocusTime) : ScoreEvent
}