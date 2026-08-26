package com.joseleandro.fullfocus.ui.event

import com.joseleandro.fullfocus.ui.state.MetaModalUiState
import com.joseleandro.fullfocus.ui.state.MetaTab

sealed interface MetaEvent {
    data object LoadData : MetaEvent
    data class UpdateDailyGoal(val goal: Int) : MetaEvent
    data class UpdateWeeklyGoal(val goal: Int) : MetaEvent
    data class AdjustDailyGoal(val delta: Int) : MetaEvent
    data class AdjustWeeklyGoal(val delta: Int) : MetaEvent
    data class ShowModal(val modal: MetaModalUiState) : MetaEvent
    data object CloseModal : MetaEvent
    data class SelectTab(val tab: MetaTab) : MetaEvent
}
