package com.icell.external.carlosformito.core.api.validator

interface FormFieldValidator<T> {
    suspend fun validate(value: T?): FormFieldValidationResult
}
