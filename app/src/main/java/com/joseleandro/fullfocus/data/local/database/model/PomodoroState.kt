package com.joseleandro.fullfocus.data.local.database.model

import androidx.annotation.StringRes
import com.joseleandro.fullfocus.R

enum class PomodoroState(
    @get:StringRes val labelRes: Int
) {
    FOCUS(labelRes = R.string.foco),
    SHORT_PAUSE(labelRes = R.string.pausa_curta),
    LONG_PAUSE(labelRes = R.string.pausa_longa)
}