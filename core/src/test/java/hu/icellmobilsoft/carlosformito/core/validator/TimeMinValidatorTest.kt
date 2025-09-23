package hu.icellmobilsoft.carlosformito.core.validator

import com.google.common.truth.Truth.assertThat
import com.google.common.truth.Truth.assertWithMessage
import hu.icellmobilsoft.carlosformito.core.R
import hu.icellmobilsoft.carlosformito.core.api.validator.FormFieldValidationResult
import hu.icellmobilsoft.carlosformito.core.util.ValidatorTestUtils.isValidationResultInvalid
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalTime
import org.junit.Test

/**
 * Unit tests for [TimeMinValidator].
 */
class TimeMinValidatorTest {

    /**
     * Tests validation with null input.
     * It should always return [FormFieldValidationResult.Valid].
     */
    @Test
    fun `validate null value`() = runTest {
        val validator = TimeMinValidator(LocalTime(12, 0))
        val validationResult = validator.validate(null)

        assertThat(validationResult).isEqualTo(FormFieldValidationResult.Valid)
    }

    /**
     * Tests validation with a time before the specified min value.
     * It should return an invalid result.
     */
    @Test
    fun `validate value before min value`() = runTest {
        val minValue = LocalTime(5, 30)
        val validator = TimeMinValidator(minValue)
        val invalidValue = LocalTime(4, 30)
        val validationResult = validator.validate(invalidValue)

        assertThat(validationResult).isValidationResultInvalid()
    }

    /**
     * Tests validation with a time equal to the specified min value.
     * It should return [FormFieldValidationResult.Valid].
     */
    @Test
    fun `validate value equal to min value`() = runTest {
        val minValue = LocalTime(5, 30)
        val validator = TimeMinValidator(minValue)
        val validationResult = validator.validate(minValue)

        assertThat(validationResult).isEqualTo(FormFieldValidationResult.Valid)
    }

    /**
     * Tests validation with a time after the specified min value.
     * It should return [FormFieldValidationResult.Valid].
     */
    @Test
    fun `validate value after min value`() = runTest {
        val minValue = LocalTime(5, 30)
        val validator = TimeMinValidator(minValue)
        val validValue = LocalTime(6, 30)
        val validationResult = validator.validate(validValue)

        assertThat(validationResult).isEqualTo(FormFieldValidationResult.Valid)
    }

    /**
     * Tests validation with invalid input and expects a custom error message.
     * It should return a [FormFieldValidationResult.Invalid.MessageWithArgs] with the specified error message ID.
     */
    @Test
    fun `test invalid input returns custom error message`() = runTest {
        val minValue = LocalTime(5, 30)

        val validator = TimeMinValidator(minValue, R.string.carlos_lbl_test_invalid_input)
        val invalidValue = LocalTime(4, 30)
        val validationResult = validator.validate(invalidValue)

        assertThat(validationResult)
            .isInstanceOf(FormFieldValidationResult.Invalid.MessageWithArgs::class.java)

        assertWithMessage("Error message ID should be ${R.string.carlos_lbl_test_invalid_input}")
            .that((validationResult as FormFieldValidationResult.Invalid.MessageWithArgs).errorMessageId)
            .isEqualTo(R.string.carlos_lbl_test_invalid_input)
    }

    /**
     * Tests validation with invalid input and expects error message arguments.
     * It should return error message arguments containing the min value.
     */
    @Test
    fun `test invalid input returns error message args`() = runTest {
        val minValue = LocalTime(5, 30)

        val validator = TimeMinValidator(minValue, R.string.carlos_lbl_test_invalid_input)
        val invalidValue = LocalTime(4, 30)
        val validationResult = validator.validate(invalidValue)

        assertThat(validationResult)
            .isInstanceOf(FormFieldValidationResult.Invalid.MessageWithArgs::class.java)

        assertWithMessage("Error message arg should be $minValue")
            .that((validationResult as FormFieldValidationResult.Invalid.MessageWithArgs).formatArgs)
            .containsExactly(minValue)
    }
}
