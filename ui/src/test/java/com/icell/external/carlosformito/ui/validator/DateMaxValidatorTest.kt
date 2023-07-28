package com.icell.external.carlosformito.ui.validator

import com.google.common.truth.Truth.assertThat
import com.google.common.truth.Truth.assertWithMessage
import com.icell.external.carlosformito.core.api.validator.FormFieldValidationResult
import com.icell.external.carlosformito.ui.R
import com.icell.external.carlosformito.ui.validator.util.ValidatorTestUtils.isValidationResultInvalid
import org.junit.Test
import java.time.LocalDate

class DateMaxValidatorTest {

    @Test
    fun `validate null input`() {
        val validator = DateMaxValidator(LocalDate.now())
        val validationResult = validator.validate(null)

        assertThat(validationResult).isEqualTo(FormFieldValidationResult.Valid)
    }

    @Test
    fun `validate date before max value`() {
        val maxValue = LocalDate.of(2023, 1, 1)
        val validator = DateMaxValidator(maxValue)
        val validationResult = validator.validate(maxValue.minusDays(1))

        assertThat(validationResult).isEqualTo(FormFieldValidationResult.Valid)
    }

    @Test
    fun `validate date equal to max value`() {
        val maxValue = LocalDate.of(2023, 1, 1)
        val validator = DateMaxValidator(maxValue)
        val validationResult = validator.validate(maxValue)

        assertThat(validationResult).isEqualTo(FormFieldValidationResult.Valid)
    }

    @Test
    fun `validate date after max value`() {
        val maxValue = LocalDate.of(2023, 1, 1)
        val validator = DateMaxValidator(maxValue)
        val validationResult = validator.validate(maxValue.plusDays(1))

        assertThat(validationResult).isValidationResultInvalid()
    }

    @Test
    fun `test invalid input returns custom error message`() {
        val maxValue = LocalDate.of(2023, 1, 1)
        val validator = DateMaxValidator(maxValue, R.string.carlos_lbl_test_invalid_input)
        val validationResult = validator.validate(maxValue.plusDays(1))

        assertThat(validationResult)
            .isInstanceOf(FormFieldValidationResult.Invalid.MessageWithArgs::class.java)

        assertWithMessage("Error message ID should be ${R.string.carlos_lbl_test_invalid_input}")
            .that((validationResult as FormFieldValidationResult.Invalid.MessageWithArgs).errorMessageId)
            .isEqualTo(R.string.carlos_lbl_test_invalid_input)
    }

    @Test
    fun `test invalid input returns error message args`() {
        val maxValue = LocalDate.of(2023, 1, 1)
        val validator = DateMaxValidator(maxValue)
        val validationResult = validator.validate(maxValue.plusDays(1))

        assertThat(validationResult)
            .isInstanceOf(FormFieldValidationResult.Invalid.MessageWithArgs::class.java)

        assertWithMessage("Error message arg should be $maxValue")
            .that((validationResult as FormFieldValidationResult.Invalid.MessageWithArgs).formatArgs)
            .containsExactly(maxValue)
    }
}
