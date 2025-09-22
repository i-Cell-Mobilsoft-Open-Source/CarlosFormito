package hu.icellmobilsoft.carlosformito.core.validator

import com.google.common.truth.Truth.assertThat
import com.google.common.truth.Truth.assertWithMessage
import hu.icellmobilsoft.carlosformito.core.R
import hu.icellmobilsoft.carlosformito.core.api.validator.FormFieldValidationResult
import hu.icellmobilsoft.carlosformito.core.util.ValidatorTestUtils.isValidationResultInvalid
import kotlinx.coroutines.test.runTest
import org.junit.Test

/**
 * Unit tests for [IntegerMaxValidator].
 */
class IntegerMaxValidatorTest {

    /**
     * Tests validation with null input, which should always return [FormFieldValidationResult.Valid].
     */
    @Test
    fun `validate null input`() = runTest {
        val validator = IntegerMaxValidator(10)
        val validationResult = validator.validate(null)

        assertThat(validationResult).isEqualTo(FormFieldValidationResult.Valid)
    }

    /**
     * Tests validation with input above the max value, which should return an invalid result.
     */
    @Test
    fun `validate input above max value`() = runTest {
        val validator = IntegerMaxValidator(10)
        val validationResult = validator.validate(15)

        assertThat(validationResult).isValidationResultInvalid()
    }

    /**
     * Tests validation with input equal to the max value, which should return [FormFieldValidationResult.Valid].
     */
    @Test
    fun `validate input equal to max value`() = runTest {
        val validator = IntegerMaxValidator(10)
        val validationResult = validator.validate(10)

        assertThat(validationResult).isEqualTo(FormFieldValidationResult.Valid)
    }

    /**
     * Tests validation with input below the max value, which should return [FormFieldValidationResult.Valid].
     */
    @Test
    fun `validate input below max value`() = runTest {
        val validator = IntegerMaxValidator(10)
        val validationResult = validator.validate(7)

        assertThat(validationResult).isEqualTo(FormFieldValidationResult.Valid)
    }

    /**
     * Tests validation with invalid input that returns a custom error message.
     */
    @Test
    fun `test invalid input returns custom error message`() = runTest {
        val maxValue = 10
        val validator = IntegerMaxValidator(maxValue, R.string.carlos_lbl_test_invalid_input)
        val validationResult = validator.validate(15)

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
        val maxValue = 10
        val validator = IntegerMaxValidator(maxValue, R.string.carlos_lbl_test_invalid_input)
        val validationResult = validator.validate(15)

        assertThat(validationResult)
            .isInstanceOf(FormFieldValidationResult.Invalid.MessageWithArgs::class.java)

        assertWithMessage("Error message arg should be $maxValue")
            .that((validationResult as FormFieldValidationResult.Invalid.MessageWithArgs).formatArgs)
            .containsExactly(maxValue)
    }
}
