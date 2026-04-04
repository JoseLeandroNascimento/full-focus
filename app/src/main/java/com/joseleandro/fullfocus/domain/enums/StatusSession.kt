package com.joseleandro.fullfocus.domain.enums

import androidx.annotation.StringRes
import com.joseleandro.fullfocus.R

enum class StatusSession(
    @get:StringRes val description: Int
) {
    FOCUS(description = R.string.foco),
    PAUSE_SHORT(description = R.string.pausa_curta),
    PAUSE_LONG(description = R.string.pausa_longa)
}