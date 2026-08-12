package com.joseleandro.fullfocus.ui.state

import com.joseleandro.fullfocus.data.local.database.model.SessionEntity

data class ScoreUiState(
    val dateTimeWithSessionGroup: Map<Long, List<SessionEntity>> = emptyMap(),
)
