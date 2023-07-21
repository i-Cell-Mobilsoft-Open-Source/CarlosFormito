package com.icell.external.carlosformito.ui.validator

import com.google.common.truth.Truth.assertThat
import com.google.common.truth.Truth.assertWithMessage
import com.icell.external.carlosformito.core.api.validator.FormFieldValidationResult
import com.icell.external.carlosformito.ui.R
import com.icell.external.carlosformito.ui.validator.util.ValidatorTestUtils.isValidationResultInvalid
import org.junit.Test
import java.time.LocalDate

class DateMinMaxValidatorTest {

    @Test
    fun `validate null input`() {
        val minValue = LocalDate.MIN
        val maxValue = LocalDate.MIN.plusDays(1)

        val validator = DateMinMaxValidator(minValue, maxValue)
        val validationResult = validator.validate(null)

        assertThat(validationResult).isEqualTo(FormFieldValidationResult.Valid)
    }

    @Test
    fun `validate date before min value`() {
        val minValue = LocalDate.of(2022, 1, 1)
        val maxValue = LocalDate.of(2023, 12, 31)
        val testValue = minValue.minusDays(1)

        val validator = DateMinMaxValidator(minValue, maxValue)
        val validationResult = validator.validate(testValue)

        assertThat(validationResult).isValidationResultInvalid()
    }

    @Test
    fun `validate date after max value`() {
        val minValue = LocalDate.of(2022, 1, 1)
        val maxValue = LocalDate.of(2023, 12, 31)
        val testValue = maxValue.plusDays(1)

        val validator = DateMinMaxValidator(minValue, maxValue)
        val validationResult = validator.validate(testValue)

        assertThat(validationResult).isValidationResultInvalid()
    }

    @Test
    fun `test invalid input returns custom error message`() {
        val minValue = LocalDate.of(2022, 1, 1)
        val maxValue = LocalDate.of(2023, 12, 31)
        val testValue = maxValue.plusDays(1)

        val validator = DateMinMaxValidator(minValue, maxValue, R.string.formular_lbl_test_invalid_input)
        val validationResult = validator.validate(testValue)

        assertThat(validationResult)
            .isInstanceOf(FormFieldValidationResult.Invalid.MessageWithArgs::class.java)

        assertWithMessage("Error message ID should be ${R.string.formular_lbl_test_invalid_input}")
            .that((validationResult as FormFieldValidationResult.Invalid.MessageWithArgs).errorMessageId)
            .isEqualTo(R.string.formular_lbl_test_invalid_input)
    }

    @Test
    fun `test invalid input returns error message args`() {
        val minValue = LocalDate.of(2022, 1, 1)
        val maxValue = LocalDate.of(2023, 12, 31)
        val testValue = maxValue.plusDays(1)

        val validator = DateMinMaxValidator(minValue, maxValue)
        val validationResult = validator.validate(testValue)

        assertThat(validationResult)
            .isInstanceOf(FormFieldValidationResult.Invalid.MessageWithArgs::class.java)

        assertWithMessage("Error message args should be exactly [$minValue, $maxValue]")
            .that((validationResult as FormFieldValidationResult.Invalid.MessageWithArgs).formatArgs)
            .containsExactly(minValue, maxValue)
    }

    @Test
    fun `validate date within the valid range`() {
        val minValue = LocalDate.of(2023, 1, 1)
        val maxValue = LocalDate.of(2023, 12, 31)
        val testValue = LocalDate.of(2023, 6, 1)
        val validator = DateMinMaxValidator(minValue, maxValue)
        val validationResult = validator.validate(testValue)

        assertThat(validationResult).isEqualTo(FormFieldValidationResult.Valid)
    }

    @Test
    fun `validate date equal to min value`() {
        val minValue = LocalDate.of(2023, 1, 1)
        val maxValue = LocalDate.of(2023, 12, 31)
        val validator = DateMinMaxValidator(minValue, maxValue)
        val validationResult = validator.validate(minValue)

        assertThat(validationResult).isEqualTo(FormFieldValidationResult.Valid)
    }

    @Test
    fun `validate date equal to max value`() {
        val minValue = LocalDate.of(2022, 1, 1)
        val maxValue = LocalDate.of(2023, 12, 31)
        val validator = DateMinMaxValidator(minValue, maxValue)
        val validationResult = validator.validate(maxValue)

        assertThat(validationResult).isEqualTo(FormFieldValidationResult.Valid)
    }
}
