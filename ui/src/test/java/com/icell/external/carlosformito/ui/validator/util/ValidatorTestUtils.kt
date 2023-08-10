package com.icell.external.carlosformito.ui.validator.util

import com.google.common.truth.Subject
import com.google.common.truth.Truth.assertThat
import com.icell.external.carlosformito.core.api.validator.FormFieldValidationResult
import com.icell.external.carlosformito.core.api.validator.FormFieldValidator

object ValidatorTestUtils {

    suspend fun <T> FormFieldValidator<T>.validateAssertInvalid(input: T?) {
        assertThat(validate(input)).isValidationResultInvalid()
    }

    suspend fun <T> FormFieldValidator<T>.validateAssertValid(input: T?) {
        assertThat(validate(input)).isEqualTo(FormFieldValidationResult.Valid)
    }

    fun Subject.isValidationResultInvalid() =
        isInstanceOf(FormFieldValidationResult.Invalid::class.java)
}
