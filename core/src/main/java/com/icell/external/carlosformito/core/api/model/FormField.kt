package com.icell.external.carlosformito.core.api.model

import com.icell.external.carlosformito.core.api.validator.FormFieldValidator

data class FormField<T>(
    val id: String,
    val initialState: FormFieldState<T> = FormFieldState(),
    val validators: List<FormFieldValidator<T>> = emptyList()
)
