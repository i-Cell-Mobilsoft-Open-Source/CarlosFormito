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
 * Unit tests for [DateMinValidator].
 */
class DateMinValidatorTest {

    /**
     * Tests validation with null input, which should always return [FormFieldValidationResult.Valid].
     */
    @Test
    fun `validate null value`() = runTest {
        val validator = DateMinValidator(LocalDate(2025, 9, 23))
        val validationResult = validator.validate(null)

        assertThat(validationResult).isEqualTo(FormFieldValidationResult.Valid)
    }

    /**
     * Tests validation with a value before the min value, which should return an invalid result.
     */
    @Test
    fun `validate value before min value`() = runTest {
        val minValue = LocalDate(2023, 1, 1)
        val validator = DateMinValidator(minValue)
        val invalidValue = minValue.minus(1, DateTimeUnit.DAY)
        val validationResult = validator.validate(invalidValue)

        assertThat(validationResult).isValidationResultInvalid()
    }

    /**
     * Tests validation with a value equal to the min value, which should return [FormFieldValidationResult.Valid].
     */
    @Test
    fun `validate value equal to min value`() = runTest {
        val minValue = LocalDate(2023, 1, 1)
        val validator = DateMinValidator(minValue)
        val validationResult = validator.validate(minValue)

        assertThat(validationResult).isEqualTo(FormFieldValidationResult.Valid)
    }

    /**
     * Tests validation with a value after the min value, which should return [FormFieldValidationResult.Valid].
     */
    @Test
    fun `validate value after min value`() = runTest {
        val minValue = LocalDate(2023, 1, 1)
        val validator = DateMinValidator(minValue)
        val validValue = minValue.plus(1, DateTimeUnit.DAY)
        val validationResult = validator.validate(validValue)

        assertThat(validationResult).isEqualTo(FormFieldValidationResult.Valid)
    }

    /**
     * Tests validation with invalid input that returns a custom error message.
     */
    @Test
    fun `test invalid input returns custom error message`() = runTest {
        val minValue = LocalDate(2023, 1, 1)

        val validator = DateMinValidator(minValue, R.string.carlos_lbl_test_invalid_input)
        val invalidValue = minValue.minus(1, DateTimeUnit.DAY)
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
        val minValue = LocalDate(2023, 1, 1)

        val validator = DateMinValidator(minValue, R.string.carlos_lbl_test_invalid_input)
        val invalidValue = minValue.minus(1, DateTimeUnit.DAY)
        val validationResult = validator.validate(invalidValue)

        assertThat(validationResult)
            .isInstanceOf(FormFieldValidationResult.Invalid.MessageWithArgs::class.java)

        assertWithMessage("Error message arg should be $minValue")
            .that((validationResult as FormFieldValidationResult.Invalid.MessageWithArgs).formatArgs)
            .containsExactly(minValue)
    }
}
