package com.joseleandro.fullfocus.ui.state

import androidx.compose.runtime.Immutable
import com.joseleandro.fullfocus.domain.model.HeatMapDataDomain
import com.joseleandro.fullfocus.ui.screen.score.WeekFocusTime
import java.time.LocalDate

@Immutable
data class ScoreUiState(
    val chartData: Map<String, Long> = emptyMap(),
    val heatMapData: List<HeatMapDataDomain> = emptyList(),
    val totalHours: String = "00h",
    val weekFocusTime: WeekFocusTime = WeekFocusTime.WEEKLY,
    val selectedDate: LocalDate? = null
)
