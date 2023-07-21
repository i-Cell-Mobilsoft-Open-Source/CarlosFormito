package com.icell.external.carlosformito.ui.validator

import com.google.common.truth.Truth.assertThat
import com.google.common.truth.Truth.assertWithMessage
import com.icell.external.carlosformito.core.api.validator.FormFieldValidationResult
import com.icell.external.carlosformito.ui.R
import com.icell.external.carlosformito.ui.validator.util.ValidatorTestUtils.isValidationResultInvalid
import org.junit.Test
import java.time.LocalDate

class DateMinValidatorTest {

    @Test
    fun `validate null value`() {
        val validator = DateMinValidator(LocalDate.MIN)
        val validationResult = validator.validate(null)

        assertThat(validationResult).isEqualTo(FormFieldValidationResult.Valid)
    }

    @Test
    fun `validate value before min value`() {
        val minValue = LocalDate.of(2023, 1, 1)
        val validator = DateMinValidator(minValue)
        val invalidValue = minValue.minusDays(1)
        val validationResult = validator.validate(invalidValue)

        assertThat(validationResult).isValidationResultInvalid()
    }

    @Test
    fun `validate value equal to min value`() {
        val minValue = LocalDate.of(2023, 1, 1)
        val validator = DateMinValidator(minValue)
        val validationResult = validator.validate(minValue)

        assertThat(validationResult).isEqualTo(FormFieldValidationResult.Valid)
    }

    @Test
    fun `validate value after min value`() {
        val minValue = LocalDate.of(2023, 1, 1)
        val validator = DateMinValidator(minValue)
        val validValue = minValue.plusDays(1)
        val validationResult = validator.validate(validValue)

        assertThat(validationResult).isEqualTo(FormFieldValidationResult.Valid)
    }

    @Test
    fun `test invalid input returns custom error message`() {
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

    @Test
    fun `test invalid input returns error message args`() {
        val minValue = LocalDate.of(2023, 1, 1)

        val validator = DateMinValidator(minValue)
        val invalidValue = minValue.minusDays(1)
        val validationResult = validator.validate(invalidValue)

        assertThat(validationResult)
            .isInstanceOf(FormFieldValidationResult.Invalid.MessageWithArgs::class.java)

        assertWithMessage("Error message arg should be $minValue")
            .that((validationResult as FormFieldValidationResult.Invalid.MessageWithArgs).formatArgs)
            .containsExactly(minValue)
    }
}
