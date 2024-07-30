package com.icell.external.carlosformito.core.util

import com.google.common.truth.Subject
import com.google.common.truth.Truth.assertThat
import com.icell.external.carlosformito.core.api.validator.FormFieldValidationResult
import com.icell.external.carlosformito.core.api.validator.FormFieldValidator

/**
 * Utility functions for testing form field validators.
 */
object ValidatorTestUtils {

    /**
     * Asserts that a form field validator returns an invalid result for the given input.
     */
    suspend fun <T> FormFieldValidator<T>.validateAssertInvalid(input: T?) {
        assertThat(validate(input)).isValidationResultInvalid()
    }

    /**
     * Asserts that a form field validator returns a valid result for the given input.
     */
    suspend fun <T> FormFieldValidator<T>.validateAssertValid(input: T?) {
        assertThat(validate(input)).isEqualTo(FormFieldValidationResult.Valid)
    }

    /**
     * Checks if the given subject is an instance of [FormFieldValidationResult.Invalid].
     */
    fun Subject.isValidationResultInvalid() =
        isInstanceOf(FormFieldValidationResult.Invalid::class.java)
}
