package com.icell.external.carlosformito.core

import com.google.common.truth.Truth.assertThat
import com.icell.external.carlosformito.core.api.validator.FormFieldValidationResult
import com.icell.external.carlosformito.core.util.ValidatorTestUtils.isValidationResultInvalid
import com.icell.external.carlosformito.core.validator.TextMinLengthValidator
import kotlinx.coroutines.test.runTest
import org.junit.Test

/**
 * Unit tests for [TextMinLengthValidator].
 */
class TextMinLengthValidatorTest {

    /**
     * Tests validation with null input, which should always return [FormFieldValidationResult.Valid].
     */
    @Test
    fun `validate null input`() = runTest {
        val validator = TextMinLengthValidator(1)
        val validationResult = validator.validate(null)

        assertThat(validationResult).isEqualTo(FormFieldValidationResult.Valid)
    }

    /**
     * Tests validation with empty input, which should always return [FormFieldValidationResult.Valid].
     */
    @Test
    fun `validate empty input`() = runTest {
        val validator = TextMinLengthValidator(1)
        val validationResult = validator.validate("")

        assertThat(validationResult).isEqualTo(FormFieldValidationResult.Valid)
    }

    /**
     * Tests initialization with an invalid `minLength` value, which should throw an [IllegalStateException].
     */
    @Test(expected = IllegalStateException::class)
    fun `test invalid minLength range behaviour`() {
        TextMinLengthValidator(-1)
    }

    /**
     * Tests validation with blank input (spaces only), which should return [FormFieldValidationResult.Valid].
     */
    @Test
    fun `validate blank input`() = runTest {
        val validator = TextMinLengthValidator(1)
        val validationResult = validator.validate(" ")

        assertThat(validationResult).isEqualTo(FormFieldValidationResult.Valid)
    }

    /**
     * Tests validation with input length below the min length, which should return an invalid result.
     */
    @Test
    fun `validate input length below min length`() = runTest {
        val validator = TextMinLengthValidator(5)
        val validationResult = validator.validate("abcd")

        assertThat(validationResult).isValidationResultInvalid()
    }

    /**
     * Tests validation with input length equal to the min length,
     * which should return [FormFieldValidationResult.Valid].
     */
    @Test
    fun `validate input length equal to min length`() = runTest {
        val validator = TextMinLengthValidator(5)
        val validationResult = validator.validate("abcde")

        assertThat(validationResult).isEqualTo(FormFieldValidationResult.Valid)
    }

    /**
     * Tests validation with input length above the min length, which should return [FormFieldValidationResult.Valid].
     */
    @Test
    fun `validate input length above min length`() = runTest {
        val validator = TextMinLengthValidator(5)
        val validationResult = validator.validate("abcdef")

        assertThat(validationResult).isEqualTo(FormFieldValidationResult.Valid)
    }
}
