package com.joseleandro.fullfocus.core.form_state

interface Validator<T> {
    val messageError: MessageFormError
    fun validate(value: T): Boolean
}