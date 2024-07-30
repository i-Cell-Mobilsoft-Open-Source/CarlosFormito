package com.icell.external.carlosformito.core

import com.google.common.truth.Truth.assertThat
import com.google.common.truth.Truth.assertWithMessage
import com.icell.external.carlosformito.core.api.validator.FormFieldValidationResult
import com.icell.external.carlosformito.core.util.ValidatorTestUtils.isValidationResultInvalid
import com.icell.external.carlosformito.core.validator.DateMaxValidator
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.time.LocalDate

/**
 * Unit tests for [DateMaxValidator].
 */
class DateMaxValidatorTest {

    /**
     * Tests validation with null input, which should always return [FormFieldValidationResult.Valid].
     */
    @Test
    fun `validate null input`() = runTest {
        val validator = DateMaxValidator(LocalDate.now())
        val validationResult = validator.validate(null)

        assertThat(validationResult).isEqualTo(FormFieldValidationResult.Valid)
    }

    /**
     * Tests validation with a date before the max value, which should return [FormFieldValidationResult.Valid].
     */
    @Test
    fun `validate date before max value`() = runTest {
        val maxValue = LocalDate.of(2023, 1, 1)
        val validator = DateMaxValidator(maxValue)
        val validationResult = validator.validate(maxValue.minusDays(1))

        assertThat(validationResult).isEqualTo(FormFieldValidationResult.Valid)
    }

    /**
     * Tests validation with a date equal to the max value, which should return [FormFieldValidationResult.Valid].
     */
    @Test
    fun `validate date equal to max value`() = runTest {
        val maxValue = LocalDate.of(2023, 1, 1)
        val validator = DateMaxValidator(maxValue)
        val validationResult = validator.validate(maxValue)

        assertThat(validationResult).isEqualTo(FormFieldValidationResult.Valid)
    }

    /**
     * Tests validation with a date after the max value, which should return an invalid result.
     */
    @Test
    fun `validate date after max value`() = runTest {
        val maxValue = LocalDate.of(2023, 1, 1)
        val validator = DateMaxValidator(maxValue)
        val validationResult = validator.validate(maxValue.plusDays(1))

        assertThat(validationResult).isValidationResultInvalid()
    }

    /**
     * Tests validation with invalid input that returns a custom error message.
     */
    @Test
    fun `test invalid input returns custom error message`() = runTest {
        val maxValue = LocalDate.of(2023, 1, 1)
        val validator = DateMaxValidator(maxValue, R.string.carlos_lbl_test_invalid_input)
        val validationResult = validator.validate(maxValue.plusDays(1))

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
        val maxValue = LocalDate.of(2023, 1, 1)
        val validator = DateMaxValidator(maxValue, R.string.carlos_lbl_test_invalid_input)
        val validationResult = validator.validate(maxValue.plusDays(1))

        assertThat(validationResult)
            .isInstanceOf(FormFieldValidationResult.Invalid.MessageWithArgs::class.java)

        assertWithMessage("Error message arg should be $maxValue")
            .that((validationResult as FormFieldValidationResult.Invalid.MessageWithArgs).formatArgs)
            .containsExactly(maxValue)
    }
}
