package com.icell.external.carlosformito.core

import com.google.common.truth.Truth.assertThat
import com.google.common.truth.Truth.assertWithMessage
import com.icell.external.carlosformito.core.api.validator.FormFieldValidationResult
import com.icell.external.carlosformito.core.util.ValidatorTestUtils.isValidationResultInvalid
import com.icell.external.carlosformito.core.validator.DateMinValidator
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.time.LocalDate

/**
 * Unit tests for [DateMinValidator].
 */
class DateMinValidatorTest {

    /**
     * Tests validation with null input, which should always return [FormFieldValidationResult.Valid].
     */
    @Test
    fun `validate null value`() = runTest {
        val validator = DateMinValidator(LocalDate.MIN)
        val validationResult = validator.validate(null)

        assertThat(validationResult).isEqualTo(FormFieldValidationResult.Valid)
    }

    /**
     * Tests validation with a value before the min value, which should return an invalid result.
     */
    @Test
    fun `validate value before min value`() = runTest {
        val minValue = LocalDate.of(2023, 1, 1)
        val validator = DateMinValidator(minValue)
        val invalidValue = minValue.minusDays(1)
        val validationResult = validator.validate(invalidValue)

        assertThat(validationResult).isValidationResultInvalid()
    }

    /**
     * Tests validation with a value equal to the min value, which should return [FormFieldValidationResult.Valid].
     */
    @Test
    fun `validate value equal to min value`() = runTest {
        val minValue = LocalDate.of(2023, 1, 1)
        val validator = DateMinValidator(minValue)
        val validationResult = validator.validate(minValue)

        assertThat(validationResult).isEqualTo(FormFieldValidationResult.Valid)
    }

    /**
     * Tests validation with a value after the min value, which should return [FormFieldValidationResult.Valid].
     */
    @Test
    fun `validate value after min value`() = runTest {
        val minValue = LocalDate.of(2023, 1, 1)
        val validator = DateMinValidator(minValue)
        val validValue = minValue.plusDays(1)
        val validationResult = validator.validate(validValue)

        assertThat(validationResult).isEqualTo(FormFieldValidationResult.Valid)
    }

    /**
     * Tests validation with invalid input that returns a custom error message.
     */
    @Test
    fun `test invalid input returns custom error message`() = runTest {
        val minValue = LocalDate.of(2023, 1, 1)

        val validator = DateMinValidator(minValue, R.string.carlos_lbl_test_invalid_input)
        val invalidValue = minValue.minusDays(1)
        val validationResult = validator.validate(invalidValue)

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
        val minValue = LocalDate.of(2023, 1, 1)

        val validator = DateMinValidator(minValue, R.string.carlos_lbl_test_invalid_input)
        val invalidValue = minValue.minusDays(1)
        val validationResult = validator.validate(invalidValue)

        assertThat(validationResult)
            .isInstanceOf(FormFieldValidationResult.Invalid.MessageWithArgs::class.java)

        assertWithMessage("Error message arg should be $minValue")
            .that((validationResult as FormFieldValidationResult.Invalid.MessageWithArgs).formatArgs)
            .containsExactly(minValue)
    }
}
