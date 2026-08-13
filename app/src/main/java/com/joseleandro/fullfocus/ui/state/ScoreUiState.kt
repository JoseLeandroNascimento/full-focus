package com.joseleandro.fullfocus.ui.state

import com.joseleandro.fullfocus.data.local.database.model.SessionEntity
import java.time.LocalDate

data class ScoreUiState(
    val dateTimeWithSessionGroup: Map<Long, List<SessionEntity>> = emptyMap(),
    val heatMapData: Map<LocalDate, Int> = emptyMap(),
    val heatMapMinutes: Map<LocalDate, Long> = emptyMap(),
    val selectedHeatMapDate: LocalDate? = null
)
