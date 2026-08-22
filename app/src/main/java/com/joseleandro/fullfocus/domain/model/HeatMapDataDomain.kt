package com.joseleandro.fullfocus.domain.model

import androidx.compose.runtime.Immutable
import java.time.LocalDate

@Immutable
data class HeatMapDataDomain(
    val date: LocalDate,
    val timeFocus: Long,
    val heatMap: HeatMapDomain
)