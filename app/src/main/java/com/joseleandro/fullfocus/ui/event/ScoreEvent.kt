package com.joseleandro.fullfocus.ui.event

sealed interface ScoreEvent {

    data object OnLoad : ScoreEvent
    data class OnDateSelected(val date: java.time.LocalDate?) : ScoreEvent
}