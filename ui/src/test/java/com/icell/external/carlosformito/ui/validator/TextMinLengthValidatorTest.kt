package com.icell.external.carlosformito.ui.validator

import com.google.common.truth.Truth.assertThat
import com.icell.external.carlosformito.core.api.validator.FormFieldValidationResult
import com.icell.external.carlosformito.ui.validator.util.ValidatorTestUtils.isValidationResultInvalid
import kotlinx.coroutines.test.runTest
import org.junit.Test

class TextMinLengthValidatorTest {

    @Test
    fun `validate null input`() = runTest {
        val validator = TextMinLengthValidator(1)
        val validationResult = validator.validate(null)

        assertThat(validationResult).isValidationResultInvalid()
    }

    @Test
    fun `validate empty input`() = runTest {
        val validator = TextMinLengthValidator(1)
        val validationResult = validator.validate("")

        assertThat(validationResult).isValidationResultInvalid()
    }

    @Test(expected = IllegalStateException::class)
    fun `test invalid minLength range behaviour`() {
        TextMinLengthValidator(-1)
    }

    @Test
    fun `validate blank input`() = runTest {
        val validator = TextMinLengthValidator(1)
        val validationResult = validator.validate("")

        assertThat(validationResult).isValidationResultInvalid()
    }

    @Test
    fun `validate input length below min length`() = runTest {
        val validator = TextMinLengthValidator(5)
        val validationResult = validator.validate("abcd")

        assertThat(validationResult).isValidationResultInvalid()
    }

    @Test
    fun `validate input length equal to min length`() = runTest {
        val validator = TextMinLengthValidator(5)
        val validationResult = validator.validate("abcde")

        assertThat(validationResult).isEqualTo(FormFieldValidationResult.Valid)
    }

    @Test
    fun `validate input length above min length`() = runTest {
        val validator = TextMinLengthValidator(5)
        val validationResult = validator.validate("abcdef")

        assertThat(validationResult).isEqualTo(FormFieldValidationResult.Valid)
    }
}
