package com.joseleandro.fullfocus.ui.event

sealed interface ScoreEvent {

    data object OnLoad : ScoreEvent
}