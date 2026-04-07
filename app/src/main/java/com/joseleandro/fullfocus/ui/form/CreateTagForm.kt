package com.joseleandro.fullfocus.ui.form

import androidx.compose.ui.graphics.Color
import com.joseleandro.fullfocus.R
import com.joseleandro.fullfocus.core.form_state.Field
import com.joseleandro.fullfocus.core.form_state.Form
import com.joseleandro.fullfocus.core.form_state.MessageFormError
import com.joseleandro.fullfocus.core.form_state.RequiredValidator

data class CreateTagForm(
    val id: Field<Int> = Field(
        value = 0
    ),
    val name: Field<String> = Field(
        value = "",
        validators = listOf(
            RequiredValidator(
                messageError = MessageFormError.MessageResource(message = R.string.nome_da_tag_obrigat_rio)
            )
        )
    ),
    val color: Field<Color> = Field(
        value = Color.Unspecified,
    )
) : Form {

    override fun isValid(): Boolean =
        name.validate().isValid

    override fun validateForm(): CreateTagForm {
        return copy(
            name = name.validate(),
        )
    }
}
