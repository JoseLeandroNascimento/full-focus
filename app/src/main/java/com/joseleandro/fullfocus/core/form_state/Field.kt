package com.joseleandro.fullfocus.core.form_state

data class Field<T>(
    val value: T,
    val isValid: Boolean = true,
    val messageError: MessageFormError? = null,
    val validators: List<Validator<T>> = emptyList()
) {
    fun validate(): Field<T> {

        val error = validators.find { validator ->
            !validator.validate(value)
        }

        return copy(
            isValid = error == null,
            messageError = error?.messageError
        )
    }

    fun update(value: T): Field<T> {
        return copy(value = value).validate()
    }
}
