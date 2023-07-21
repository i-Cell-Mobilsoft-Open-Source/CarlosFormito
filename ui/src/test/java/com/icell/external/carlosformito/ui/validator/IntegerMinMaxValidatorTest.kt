package com.icell.external.carlosformito.ui.validator

import com.google.common.truth.Truth.assertThat
import com.google.common.truth.Truth.assertWithMessage
import com.icell.external.carlosformito.core.api.validator.FormFieldValidationResult
import com.icell.external.carlosformito.ui.R
import com.icell.external.carlosformito.ui.validator.util.ValidatorTestUtils.isValidationResultInvalid
import org.junit.Test

class IntegerMinMaxValidatorTest {

    @Test
    fun `validate null input`() {
        val validator = IntegerMinMaxValidator(5, 10)
        val validationResult = validator.validate(null)

        assertThat(validationResult).isEqualTo(FormFieldValidationResult.Valid)
    }

    @Test
    fun `validate input below min value`() {
        val validator = IntegerMinMaxValidator(5, 10)
        val validationResult = validator.validate(3)

        assertThat(validationResult).isValidationResultInvalid()
    }

    @Test
    fun `validate input equal to min value`() {
        val validator = IntegerMinMaxValidator(5, 10)
        val validationResult = validator.validate(5)

        assertThat(validationResult).isEqualTo(FormFieldValidationResult.Valid)
    }

    @Test
    fun `validate input between min and max values`() {
        val validator = IntegerMinMaxValidator(5, 10)
        val validationResult = validator.validate(7)

        assertThat(validationResult).isEqualTo(FormFieldValidationResult.Valid)
    }

    @Test
    fun `validate input equal to max value`() {
        val validator = IntegerMinMaxValidator(5, 10)
        val validationResult = validator.validate(10)

        assertThat(validationResult).isEqualTo(FormFieldValidationResult.Valid)
    }

    @Test
    fun `validate input above max value`() {
        val validator = IntegerMinMaxValidator(5, 10)
        val validationResult = validator.validate(15)

        assertThat(validationResult).isValidationResultInvalid()
    }

    @Test
    fun `test invalid input returns custom error message`() {
        val minValue = 5
        val maxValue = 10

        val validator = IntegerMinMaxValidator(minValue, maxValue, R.string.carlos_lbl_test_invalid_input)
        val validationResult = validator.validate(15)

        assertThat(validationResult)
            .isInstanceOf(FormFieldValidationResult.Invalid.MessageWithArgs::class.java)

        assertWithMessage("Error message ID should be ${R.string.carlos_lbl_test_invalid_input}")
            .that((validationResult as FormFieldValidationResult.Invalid.MessageWithArgs).errorMessageId)
            .isEqualTo(R.string.carlos_lbl_test_invalid_input)
    }

    @Test
    fun `test invalid input returns error message args`() {
        val minValue = 5
        val maxValue = 10

        val validator = IntegerMinMaxValidator(minValue, maxValue)
        val validationResult = validator.validate(15)

        assertThat(validationResult)
            .isInstanceOf(FormFieldValidationResult.Invalid.MessageWithArgs::class.java)

        assertWithMessage("Error message args should be exactly [$minValue, $maxValue]")
            .that((validationResult as FormFieldValidationResult.Invalid.MessageWithArgs).formatArgs)
            .containsExactly(minValue, maxValue)
    }
}
