package com.icell.external.carlosformito.core.api.validator

interface FormFieldValidator<T> {
    fun validate(value: T?): FormFieldValidationResult
}
