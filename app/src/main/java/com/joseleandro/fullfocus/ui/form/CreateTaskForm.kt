package com.joseleandro.fullfocus.ui.form

import com.joseleandro.fullfocus.R
import com.joseleandro.fullfocus.core.form_state.Field
import com.joseleandro.fullfocus.core.form_state.Form
import com.joseleandro.fullfocus.core.form_state.MessageFormError
import com.joseleandro.fullfocus.core.form_state.MinValidator
import com.joseleandro.fullfocus.core.form_state.RequiredValidator

data class CreateTaskForm(
    val title: Field<String> = Field(
        value = "",
        validators = listOf(
            RequiredValidator()
        )
    ),
    val pomodoros: Field<Int> = Field(
        value = 0,
        validators = listOf(
            MinValidator(
                min = 1.0,
                messageError = MessageFormError.MessageResource(
                    message = R.string.informe_o_numero_de_pomodoros
                )
            )
        )
    ),
    val tag: Field<Int?> = Field(
        value = null
    )
) : Form {
    override fun isValid(): Boolean {
        return title.validate().isValid && pomodoros.validate().isValid
    }

    override fun validateForm(): CreateTaskForm {
        return copy(
            title = title.validate(),
            pomodoros = pomodoros.validate(),
            tag = tag.validate()
        )
    }
}