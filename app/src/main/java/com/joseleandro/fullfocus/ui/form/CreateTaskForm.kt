package com.joseleandro.fullfocus.ui.form

import com.joseleandro.fullfocus.core.form_state.Field
import com.joseleandro.fullfocus.core.form_state.Form
import com.joseleandro.fullfocus.core.form_state.RequiredValidator

data class CreateTaskForm(
    val title: Field<String> = Field(
        value = "",
        validators = listOf(
            RequiredValidator()
        )
    ),
    val pomodoros: Field<Int> = Field(
        value = 0
    )
) : Form {
    override fun isValid(): Boolean {
        return title.validate().isValid
    }

    override fun validateForm(): CreateTaskForm {
        return copy(
            title = title.validate(),
            pomodoros = pomodoros.validate()
        )
    }
}