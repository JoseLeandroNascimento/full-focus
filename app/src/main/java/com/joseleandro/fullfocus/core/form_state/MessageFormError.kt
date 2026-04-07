package com.joseleandro.fullfocus.core.form_state

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

sealed interface MessageFormError {

    data class MessageString(val message: String) : MessageFormError {
        @Composable
        override fun getMessage(): String =
            message

    }

    data class MessageResource(@get:StringRes val message: Int) : MessageFormError {
        @Composable
        override fun getMessage(): String =
            stringResource(id = message)

    }

    @Composable
    fun getMessage(): String
}

