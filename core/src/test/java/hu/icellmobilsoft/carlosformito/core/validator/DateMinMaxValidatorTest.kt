package hu.icellmobilsoft.carlosformito.core.validator

import com.google.common.truth.Truth.assertThat
import com.google.common.truth.Truth.assertWithMessage
import hu.icellmobilsoft.carlosformito.core.R
import hu.icellmobilsoft.carlosformito.core.api.validator.FormFieldValidationResult
import hu.icellmobilsoft.carlosformito.core.util.ValidatorTestUtils.isValidationResultInvalid
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import org.junit.Test

/**
 * Unit tests for [DateMinMaxValidator].
 */
class DateMinMaxValidatorTest {

    /**
     * Tests validation with null input, which should always return [FormFieldValidationResult.Valid].
     */
    @Test
    fun `validate null input`() = runTest {
        val minValue = LocalDate(2025, 9, 23)
        val maxValue = LocalDate(2025, 9, 23).plus(1, DateTimeUnit.DAY)

        val validator = DateMinMaxValidator(minValue, maxValue)
        val validationResult = validator.validate(null)

        assertThat(validationResult).isEqualTo(FormFieldValidationResult.Valid)
    }

    /**
     * Tests validation with a date before the min value, which should return an invalid result.
     */
    @Test
    fun `validate date before min value`() = runTest {
        val minValue = LocalDate(2022, 1, 1)
        val maxValue = LocalDate(2023, 12, 31)
        val testValue = minValue.minus(1, DateTimeUnit.DAY)

        val validator = DateMinMaxValidator(minValue, maxValue)
        val validationResult = validator.validate(testValue)

        assertThat(validationResult).isValidationResultInvalid()
    }

    /**
     * Tests validation with a date after the max value, which should return an invalid result.
     */
    @Test
    fun `validate date after max value`() = runTest {
        val minValue = LocalDate(2022, 1, 1)
        val maxValue = LocalDate(2023, 12, 31)
        val testValue = maxValue.plus(1, DateTimeUnit.DAY)

        val validator = DateMinMaxValidator(minValue, maxValue)
        val validationResult = validator.validate(testValue)

        assertThat(validationResult).isValidationResultInvalid()
    }

    /**
     * Tests validation with invalid input that returns a custom error message.
     */
    @Test
    fun `test invalid input returns custom error message`() = runTest {
        val minValue = LocalDate(2022, 1, 1)
        val maxValue = LocalDate(2023, 12, 31)
        val testValue = maxValue.plus(1, DateTimeUnit.DAY)

        val validator =
            DateMinMaxValidator(minValue, maxValue, R.string.carlos_lbl_test_invalid_input)
        val validationResult = validator.validate(testValue)

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
        val minValue = LocalDate(2022, 1, 1)
        val maxValue = LocalDate(2023, 12, 31)
        val testValue = maxValue.plus(1, DateTimeUnit.DAY)

        val validator = DateMinMaxValidator(minValue, maxValue, R.string.carlos_lbl_test_invalid_input)
        val validationResult = validator.validate(testValue)

        assertThat(validationResult)
            .isInstanceOf(FormFieldValidationResult.Invalid.MessageWithArgs::class.java)

        assertWithMessage("Error message args should be exactly [$minValue, $maxValue]")
            .that((validationResult as FormFieldValidationResult.Invalid.MessageWithArgs).formatArgs)
            .containsExactly(minValue, maxValue)
    }

    /**
     * Tests validation with a date within the valid range, which should return [FormFieldValidationResult.Valid].
     */
    @Test
    fun `validate date within the valid range`() = runTest {
        val minValue = LocalDate(2023, 1, 1)
        val maxValue = LocalDate(2023, 12, 31)
        val testValue = LocalDate(2023, 6, 1)
        val validator = DateMinMaxValidator(minValue, maxValue)
        val validationResult = validator.validate(testValue)

        assertThat(validationResult).isEqualTo(FormFieldValidationResult.Valid)
    }

    /**
     * Tests validation with a date equal to the min value, which should return [FormFieldValidationResult.Valid].
     */
    @Test
    fun `validate date equal to min value`() = runTest {
        val minValue = LocalDate(2023, 1, 1)
        val maxValue = LocalDate(2023, 12, 31)
        val validator = DateMinMaxValidator(minValue, maxValue)
        val validationResult = validator.validate(minValue)

        assertThat(validationResult).isEqualTo(FormFieldValidationResult.Valid)
    }

    /**
     * Tests validation with a date equal to the max value, which should return [FormFieldValidationResult.Valid].
     */
    @Test
    fun `validate date equal to max value`() = runTest {
        val minValue = LocalDate(2022, 1, 1)
        val maxValue = LocalDate(2023, 12, 31)
        val validator = DateMinMaxValidator(minValue, maxValue)
        val validationResult = validator.validate(maxValue)

        assertThat(validationResult).isEqualTo(FormFieldValidationResult.Valid)
    }
}
