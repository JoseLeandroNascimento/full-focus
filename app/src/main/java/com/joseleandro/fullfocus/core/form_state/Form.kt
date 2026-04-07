package com.joseleandro.fullfocus.core.form_state

interface Form {

    fun isValid(): Boolean

    fun validateForm(): Form
}