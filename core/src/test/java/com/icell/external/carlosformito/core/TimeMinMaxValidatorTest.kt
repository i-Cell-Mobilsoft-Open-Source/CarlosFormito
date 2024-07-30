package com.icell.external.carlosformito.core

import com.google.common.truth.Truth.assertThat
import com.google.common.truth.Truth.assertWithMessage
import com.icell.external.carlosformito.core.api.validator.FormFieldValidationResult
import com.icell.external.carlosformito.core.util.ValidatorTestUtils.isValidationResultInvalid
import com.icell.external.carlosformito.core.validator.TimeMinMaxValidator
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.time.LocalTime

/**
 * Unit tests for [TimeMinMaxValidator].
 */
class TimeMinMaxValidatorTest {

    /**
     * Tests validation with null input.
     * It should always return [FormFieldValidationResult.Valid].
     */
    @Test
    fun `validate null input`() = runTest {
        val minValue = LocalTime.MIN
        val maxValue = LocalTime.MIN.plusHours(1)

        val validator = TimeMinMaxValidator(minValue, maxValue)
        val validationResult = validator.validate(null)

        assertThat(validationResult).isEqualTo(FormFieldValidationResult.Valid)
    }

    /**
     * Tests validation with a time before the min value.
     * It should return an invalid result.
     */
    @Test
    fun `validate time before min value`() = runTest {
        val minValue = LocalTime.NOON
        val maxValue = LocalTime.MAX
        val testValue = minValue.minusHours(1)

        val validator = TimeMinMaxValidator(minValue, maxValue)
        val validationResult = validator.validate(testValue)

        assertThat(validationResult).isValidationResultInvalid()
    }

    /**
     * Tests validation with a time after the max value.
     * It should return an invalid result.
     */
    @Test
    fun `validate time after max value`() = runTest {
        val minValue = LocalTime.NOON
        val maxValue = LocalTime.MAX
        val testValue = maxValue.plusHours(1)

        val validator = TimeMinMaxValidator(minValue, maxValue)
        val validationResult = validator.validate(testValue)

        assertThat(validationResult).isValidationResultInvalid()
    }

    /**
     * Tests validation with invalid input and expects a custom error message.
     * It should return a [FormFieldValidationResult.Invalid.MessageWithArgs] with the specified error message ID.
     */
    @Test
    fun `test invalid input returns custom error message`() = runTest {
        val minValue = LocalTime.NOON
        val maxValue = LocalTime.MAX
        val testValue = maxValue.plusHours(1)

        val validator =
            TimeMinMaxValidator(minValue, maxValue, R.string.carlos_lbl_test_invalid_input)
        val validationResult = validator.validate(testValue)

        assertThat(validationResult)
            .isInstanceOf(FormFieldValidationResult.Invalid.MessageWithArgs::class.java)

        assertWithMessage("Error message ID should be ${R.string.carlos_lbl_test_invalid_input}")
            .that((validationResult as FormFieldValidationResult.Invalid.MessageWithArgs).errorMessageId)
            .isEqualTo(R.string.carlos_lbl_test_invalid_input)
    }

    /**
     * Tests validation with invalid input and expects error message arguments.
     * It should return error message arguments containing the min and max values.
     */
    @Test
    fun `test invalid input returns error message args`() = runTest {
        val minValue = LocalTime.NOON
        val maxValue = LocalTime.MAX
        val testValue = maxValue.plusHours(1)

        val validator = TimeMinMaxValidator(minValue, maxValue, R.string.carlos_lbl_test_invalid_input)
        val validationResult = validator.validate(testValue)

        assertThat(validationResult)
            .isInstanceOf(FormFieldValidationResult.Invalid.MessageWithArgs::class.java)

        assertWithMessage("Error message args should be exactly [$minValue, $maxValue]")
            .that((validationResult as FormFieldValidationResult.Invalid.MessageWithArgs).formatArgs)
            .containsExactly(minValue, maxValue)
    }

    /**
     * Tests validation with a time within the valid range.
     * It should return [FormFieldValidationResult.Valid].
     */
    @Test
    fun `validate time within the valid range`() = runTest {
        val minValue = LocalTime.NOON
        val maxValue = LocalTime.MAX
        val testValue = LocalTime.of(20, 45)

        val validator = TimeMinMaxValidator(minValue, maxValue)
        val validationResult = validator.validate(testValue)

        assertThat(validationResult).isEqualTo(FormFieldValidationResult.Valid)
    }

    /**
     * Tests validation with a time equal to the min value.
     * It should return [FormFieldValidationResult.Valid].
     */
    @Test
    fun `validate time equal to min value`() = runTest {
        val minValue = LocalTime.NOON
        val maxValue = LocalTime.MAX

        val validator = TimeMinMaxValidator(minValue, maxValue)
        val validationResult = validator.validate(minValue)

        assertThat(validationResult).isEqualTo(FormFieldValidationResult.Valid)
    }

    /**
     * Tests validation with a time equal to the max value.
     * It should return [FormFieldValidationResult.Valid].
     */
    @Test
    fun `validate time equal to max value`() = runTest {
        val minValue = LocalTime.NOON
        val maxValue = LocalTime.MAX

        val validator = TimeMinMaxValidator(minValue, maxValue)
        val validationResult = validator.validate(maxValue)

        assertThat(validationResult).isEqualTo(FormFieldValidationResult.Valid)
    }
}
