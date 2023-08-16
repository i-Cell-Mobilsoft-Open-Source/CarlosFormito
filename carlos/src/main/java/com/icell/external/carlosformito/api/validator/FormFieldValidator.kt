package com.icell.external.carlosformito.api.validator

interface FormFieldValidator<T> {
    suspend fun validate(value: T?): FormFieldValidationResult
}
