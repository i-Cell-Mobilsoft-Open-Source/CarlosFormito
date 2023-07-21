package com.icell.external.carlosformito.ui.validator

import com.google.common.truth.Truth.assertThat
import com.google.common.truth.Truth.assertWithMessage
import com.icell.external.carlosformito.core.api.validator.FormFieldValidationResult
import com.icell.external.carlosformito.ui.R
import com.icell.external.carlosformito.ui.validator.util.ValidatorTestUtils.isValidationResultInvalid
import org.junit.Test

class IntegerMinValidatorTest {

    @Test
    fun `validate null input`() {
        val validator = IntegerMinValidator(5)
        val validationResult = validator.validate(null)

        assertThat(validationResult).isEqualTo(FormFieldValidationResult.Valid)
    }

    @Test
    fun `validate input below min value`() {
        val validator = IntegerMinValidator(5)
        val validationResult = validator.validate(3)

        assertThat(validationResult).isValidationResultInvalid()
    }

    @Test
    fun `validate input equal to min value`() {
        val validator = IntegerMinValidator(5)
        val validationResult = validator.validate(5)

        assertThat(validationResult).isEqualTo(FormFieldValidationResult.Valid)
    }

    @Test
    fun `validate input above min value`() {
        val validator = IntegerMinValidator(5)
        val validationResult = validator.validate(7)

        assertThat(validationResult).isEqualTo(FormFieldValidationResult.Valid)
    }

    @Test
    fun `test invalid input returns custom error message`() {
        val minValue = 5
        val validator = IntegerMinValidator(minValue, R.string.formular_lbl_test_invalid_input)
        val validationResult = validator.validate(3)

        assertThat(validationResult)
            .isInstanceOf(FormFieldValidationResult.Invalid.MessageWithArgs::class.java)

        assertWithMessage("Error message ID should be ${R.string.formular_lbl_test_invalid_input}")
            .that((validationResult as FormFieldValidationResult.Invalid.MessageWithArgs).errorMessageId)
            .isEqualTo(R.string.formular_lbl_test_invalid_input)
    }

    @Test
    fun `test invalid input returns error message args`() {
        val minValue = 5
        val validator = IntegerMinValidator(minValue)
        val validationResult = validator.validate(3)

        assertThat(validationResult)
            .isInstanceOf(FormFieldValidationResult.Invalid.MessageWithArgs::class.java)

        assertWithMessage("Error message arg should be $minValue")
            .that((validationResult as FormFieldValidationResult.Invalid.MessageWithArgs).formatArgs)
            .containsExactly(minValue)
    }
}
