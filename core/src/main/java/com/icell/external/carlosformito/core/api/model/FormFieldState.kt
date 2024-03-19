package com.icell.external.carlosformito.core.api.model

import com.icell.external.carlosformito.core.api.validator.FormFieldValidationResult

data class FormFieldState<T>(
    val value: T? = null,
    val validationResult: FormFieldValidationResult? = null
) {

    val isError: Boolean
        get() = validationResult is FormFieldValidationResult.Invalid
}
