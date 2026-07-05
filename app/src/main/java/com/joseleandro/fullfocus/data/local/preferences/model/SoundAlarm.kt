package com.joseleandro.fullfocus.data.local.preferences.model

import androidx.annotation.RawRes
import androidx.annotation.StringRes
import com.joseleandro.fullfocus.R

enum class SoundAlarm(
    val index: Int,
    @get:RawRes val soundRes: Int,
    @get:StringRes val title: Int
) {
    ALARM_1(index = 1, soundRes = R.raw.alert_notification, title = R.string.alarme_padrao),
    ALARM_2(index = 2, soundRes = R.raw.alert_new_notification, title = R.string.alarme_novo),
    ALARM_3(
        index = 3,
        soundRes = R.raw.alert_universfield_new_notification,
        title = R.string.alarme_universitario
    )
}