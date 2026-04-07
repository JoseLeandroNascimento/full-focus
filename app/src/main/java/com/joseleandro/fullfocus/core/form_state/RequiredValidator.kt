package com.joseleandro.fullfocus.core.form_state

import com.joseleandro.fullfocus.R

class RequiredValidator(
    override val messageError: MessageFormError = MessageFormError.MessageResource(message = R.string.campo_obrigat_rio)
) : Validator<String> {

    override fun validate(value: String): Boolean {
        return value.trim().isNotEmpty()
    }
}