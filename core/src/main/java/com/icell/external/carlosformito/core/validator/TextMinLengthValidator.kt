package com.icell.external.carlosformito.core.validator

import androidx.annotation.IntRange
import androidx.annotation.StringRes
import com.icell.external.carlosformito.core.api.validator.FormFieldValidationResult
import com.icell.external.carlosformito.core.api.validator.FormFieldValidator

class TextMinLengthValidator(
    @IntRange(from = MIN_LENGTH_RANGE_FROM)
    private val minLength: Int,
    @StringRes
    private val errorMessageId: Int? = null
) : FormFieldValidator<String> {

    init {
        check(minLength >= MIN_LENGTH_RANGE_FROM) {
            "minLength param must be greater than or equal to $MIN_LENGTH_RANGE_FROM"
        }
    }

    override suspend fun validate(value: String?): FormFieldValidationResult {
        val nonNullValue = value.orEmpty()
        if (nonNullValue.isEmpty()) {
            return FormFieldValidationResult.Valid
        }
        return if (nonNullValue.length < minLength) {
            FormFieldValidationResult.Invalid.of(
                errorMessageId,
                listOf(minLength)
            )
        } else {
            FormFieldValidationResult.Valid
        }
    }

    companion object {
        private const val MIN_LENGTH_RANGE_FROM = 1L
    }
}
