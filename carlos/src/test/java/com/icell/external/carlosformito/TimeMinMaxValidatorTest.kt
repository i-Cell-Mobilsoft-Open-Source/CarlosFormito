package com.icell.external.carlosformito

import com.google.common.truth.Truth
import com.icell.external.carlosformito.api.validator.FormFieldValidationResult
import com.icell.external.carlosformito.ui.validator.TimeMinMaxValidator
import com.icell.external.carlosformito.util.ValidatorTestUtils.isValidationResultInvalid
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.time.LocalTime

class TimeMinMaxValidatorTest {

    @Test
    fun `validate null input`() = runTest {
        val minValue = LocalTime.MIN
        val maxValue = LocalTime.MIN.plusHours(1)

        val validator = TimeMinMaxValidator(minValue, maxValue)
        val validationResult = validator.validate(null)

        Truth.assertThat(validationResult).isEqualTo(FormFieldValidationResult.Valid)
    }

    @Test
    fun `validate time before min value`() = runTest {
        val minValue = LocalTime.NOON
        val maxValue = LocalTime.MAX
        val testValue = minValue.minusHours(1)

        val validator = TimeMinMaxValidator(minValue, maxValue)
        val validationResult = validator.validate(testValue)

        Truth.assertThat(validationResult).isValidationResultInvalid()
    }

    @Test
    fun `validate time after max value`() = runTest {
        val minValue = LocalTime.NOON
        val maxValue = LocalTime.MAX
        val testValue = maxValue.plusHours(1)

        val validator = TimeMinMaxValidator(minValue, maxValue)
        val validationResult = validator.validate(testValue)

        Truth.assertThat(validationResult).isValidationResultInvalid()
    }

    @Test
    fun `test invalid input returns custom error message`() = runTest {
        val minValue = LocalTime.NOON
        val maxValue = LocalTime.MAX
        val testValue = maxValue.plusHours(1)

        val validator =
            TimeMinMaxValidator(minValue, maxValue, R.string.carlos_lbl_test_invalid_input)
        val validationResult = validator.validate(testValue)

        Truth.assertThat(validationResult)
            .isInstanceOf(FormFieldValidationResult.Invalid.MessageWithArgs::class.java)

        Truth.assertWithMessage("Error message ID should be ${R.string.carlos_lbl_test_invalid_input}")
            .that((validationResult as FormFieldValidationResult.Invalid.MessageWithArgs).errorMessageId)
            .isEqualTo(R.string.carlos_lbl_test_invalid_input)
    }

    @Test
    fun `test invalid input returns error message args`() = runTest {
        val minValue = LocalTime.NOON
        val maxValue = LocalTime.MAX
        val testValue = maxValue.plusHours(1)

        val validator = TimeMinMaxValidator(minValue, maxValue)
        val validationResult = validator.validate(testValue)

        Truth.assertThat(validationResult)
            .isInstanceOf(FormFieldValidationResult.Invalid.MessageWithArgs::class.java)

        Truth.assertWithMessage("Error message args should be exactly [$minValue, $maxValue]")
            .that((validationResult as FormFieldValidationResult.Invalid.MessageWithArgs).formatArgs)
            .containsExactly(minValue, maxValue)
    }

    @Test
    fun `validate time within the valid range`() = runTest {
        val minValue = LocalTime.NOON
        val maxValue = LocalTime.MAX
        val testValue = LocalTime.of(20, 45)
        val validator = TimeMinMaxValidator(minValue, maxValue)
        val validationResult = validator.validate(testValue)

        Truth.assertThat(validationResult).isEqualTo(FormFieldValidationResult.Valid)
    }

    @Test
    fun `validate time equal to min value`() = runTest {
        val minValue = LocalTime.NOON
        val maxValue = LocalTime.MAX
        val validator = TimeMinMaxValidator(minValue, maxValue)
        val validationResult = validator.validate(minValue)

        Truth.assertThat(validationResult).isEqualTo(FormFieldValidationResult.Valid)
    }

    @Test
    fun `validate time equal to max value`() = runTest {
        val minValue = LocalTime.NOON
        val maxValue = LocalTime.MAX
        val validator = TimeMinMaxValidator(minValue, maxValue)
        val validationResult = validator.validate(maxValue)

        Truth.assertThat(validationResult).isEqualTo(FormFieldValidationResult.Valid)
    }
}
