package com.icell.external.carlosformito.core.validator

import androidx.annotation.IntRange
import androidx.annotation.StringRes
import com.icell.external.carlosformito.core.api.validator.FormFieldValidationResult
import com.icell.external.carlosformito.core.api.validator.FormFieldValidator

/**
 * `TextMinLengthValidator` validates that a String value meets a minimum length requirement.
 *
 * @param minLength The minimum length that the validated value should have.
 * @param errorMessageId Optional resource ID for an error message to be displayed if validation fails.
 */
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

    /**
     * Validates the given String value against the minimum length requirement.
     *
     * @param value The String value to be validated.
     * @return [FormFieldValidationResult.Valid] if the value meets the minimum length requirement or is empty,
     *         [FormFieldValidationResult.Invalid] otherwise.
     */
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
