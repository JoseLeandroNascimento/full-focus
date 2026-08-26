package com.joseleandro.fullfocus.ui.state

import androidx.compose.runtime.Immutable

@Immutable
data class MetaUiState(
    val dailyGoal: Int = 4,
    val weeklyGoal: Int = 5,
    val selectedTab: MetaTab = MetaTab.OVERVIEW,
    val modal: MetaModalUiState = MetaModalUiState.None,
    val competencies: List<Competency> = emptyList(),
    val technologies: List<Technology> = emptyList(),
    val achievements: List<Achievement> = emptyList(),
    val journeySteps: List<JourneyStep> = emptyList(),
    val currentLevelTasks: List<Task> = emptyList()
)

enum class MetaTab {
    OVERVIEW, JOURNEY_GUIDE, ACHIEVEMENTS
}

data class Competency(
    val name: String,
    val progress: Float,
    val timeLabel: String,
    val icon: Int
)

data class Technology(
    val name: String,
    val timeLabel: String,
    val icon: Int,
    val color: Long
)

data class Achievement(
    val title: String,
    val icon: Int,
    val isUnlocked: Boolean,
    val category: String,
    val color: Long
)

data class JourneyStep(
    val level: Int,
    val icon: Int,
    val isCompleted: Boolean,
    val isCurrent: Boolean
)

data class Task(
    val title: String,
    val isCompleted: Boolean,
    val date: String? = null,
    val hasLink: Boolean = false
)

sealed interface MetaModalUiState {
    data object None : MetaModalUiState
    data object DailyGoal : MetaModalUiState
    data object WeeklyGoal : MetaModalUiState
}
