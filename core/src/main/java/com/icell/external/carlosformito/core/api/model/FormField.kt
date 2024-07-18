package com.icell.external.carlosformito.core.api.model

import com.icell.external.carlosformito.core.api.validator.FormFieldValidator

/**
 * Represents a form field with an identifier, initial state, and validators.
 *
 * @property id The unique identifier of the form field.
 * @property initialState The initial state of the form field, including its value and validation result.
 * @property validators A list of validators used to validate the form field's value.
 */
data class FormField<T>(
    val id: String,
    val initialState: FormFieldState<T> = FormFieldState(),
    val validators: List<FormFieldValidator<T>> = emptyList()
)
