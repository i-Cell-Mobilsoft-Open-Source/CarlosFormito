package com.icell.external.carlosformito.core.validator

import com.google.common.truth.Truth.assertThat
import com.google.common.truth.Truth.assertWithMessage
import com.icell.external.carlosformito.core.R
import com.icell.external.carlosformito.core.api.validator.FormFieldValidationResult
import com.icell.external.carlosformito.core.util.ValidatorTestUtils.isValidationResultInvalid
import kotlinx.coroutines.test.runTest
import org.junit.Test

/**
 * Unit tests for [IntegerMinValidator].
 */
class IntegerMinValidatorTest {

    /**
     * Tests validation with null input, which should always return [FormFieldValidationResult.Valid].
     */
    @Test
    fun `validate null input`() = runTest {
        val validator = IntegerMinValidator(5)
        val validationResult = validator.validate(null)

        assertThat(validationResult).isEqualTo(FormFieldValidationResult.Valid)
    }

    /**
     * Tests validation with input below the min value, which should return an invalid result.
     */
    @Test
    fun `validate input below min value`() = runTest {
        val validator = IntegerMinValidator(5)
        val validationResult = validator.validate(3)

        assertThat(validationResult).isValidationResultInvalid()
    }

    /**
     * Tests validation with input equal to the min value, which should return [FormFieldValidationResult.Valid].
     */
    @Test
    fun `validate input equal to min value`() = runTest {
        val validator = IntegerMinValidator(5)
        val validationResult = validator.validate(5)

        assertThat(validationResult).isEqualTo(FormFieldValidationResult.Valid)
    }

    /**
     * Tests validation with input above the min value, which should return [FormFieldValidationResult.Valid].
     */
    @Test
    fun `validate input above min value`() = runTest {
        val validator = IntegerMinValidator(5)
        val validationResult = validator.validate(7)

        assertThat(validationResult).isEqualTo(FormFieldValidationResult.Valid)
    }

    /**
     * Tests validation with invalid input that returns a custom error message.
     */
    @Test
    fun `test invalid input returns custom error message`() = runTest {
        val minValue = 5
        val validator = IntegerMinValidator(minValue, R.string.carlos_lbl_test_invalid_input)
        val validationResult = validator.validate(3)

        assertThat(validationResult)
            .isInstanceOf(FormFieldValidationResult.Invalid.MessageWithArgs::class.java)

        assertWithMessage("Error message ID should be ${R.string.carlos_lbl_test_invalid_input}")
            .that((validationResult as FormFieldValidationResult.Invalid.MessageWithArgs).errorMessageId)
            .isEqualTo(R.string.carlos_lbl_test_invalid_input)
    }

    /**
     * Tests validation with invalid input that returns error message arguments.
     */
    @Test
    fun `test invalid input returns error message args`() = runTest {
        val minValue = 5
        val validator = IntegerMinValidator(minValue, R.string.carlos_lbl_test_invalid_input)
        val validationResult = validator.validate(3)

        assertThat(validationResult)
            .isInstanceOf(FormFieldValidationResult.Invalid.MessageWithArgs::class.java)

        assertWithMessage("Error message arg should be $minValue")
            .that((validationResult as FormFieldValidationResult.Invalid.MessageWithArgs).formatArgs)
            .containsExactly(minValue)
    }
}
