package com.joseleandro.fullfocus.core.form_state

import com.joseleandro.fullfocus.R

private val MESSAGE_ERROR_DEFAULT =
    MessageFormError.MessageResource(message = R.string.informe_o_valor_m_nimo)

class MinValidator<T : Number>(
    private val min: Double,
    override val messageError: MessageFormError = MESSAGE_ERROR_DEFAULT
) : Validator<T> {

    override fun validate(value: T): Boolean {
        return value.toDouble() >= min
    }
}
