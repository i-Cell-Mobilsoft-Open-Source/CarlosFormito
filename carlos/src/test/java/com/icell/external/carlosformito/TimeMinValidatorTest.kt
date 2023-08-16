package com.icell.external.carlosformito

import com.google.common.truth.Truth
import com.icell.external.carlosformito.api.validator.FormFieldValidationResult
import com.icell.external.carlosformito.ui.validator.TimeMinValidator
import com.icell.external.carlosformito.util.ValidatorTestUtils.isValidationResultInvalid
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.time.LocalTime

class TimeMinValidatorTest {

    @Test
    fun `validate null value`() = runTest {
        val validator = TimeMinValidator(LocalTime.MIN)
        val validationResult = validator.validate(null)

        Truth.assertThat(validationResult).isEqualTo(FormFieldValidationResult.Valid)
    }

    @Test
    fun `validate value before min value`() = runTest {
        val minValue = LocalTime.of(5, 30)
        val validator = TimeMinValidator(minValue)
        val invalidValue = minValue.minusHours(1)
        val validationResult = validator.validate(invalidValue)

        Truth.assertThat(validationResult).isValidationResultInvalid()
    }

    @Test
    fun `validate value equal to min value`() = runTest {
        val minValue = LocalTime.of(5, 30)
        val validator = TimeMinValidator(minValue)
        val validationResult = validator.validate(minValue)

        Truth.assertThat(validationResult).isEqualTo(FormFieldValidationResult.Valid)
    }

    @Test
    fun `validate value after min value`() = runTest {
        val minValue = LocalTime.of(5, 30)
        val validator = TimeMinValidator(minValue)
        val validValue = minValue.plusHours(1)
        val validationResult = validator.validate(validValue)

        Truth.assertThat(validationResult).isEqualTo(FormFieldValidationResult.Valid)
    }

    @Test
    fun `test invalid input returns custom error message`() = runTest {
        val minValue = LocalTime.of(5, 30)

        val validator = TimeMinValidator(minValue, R.string.carlos_lbl_test_invalid_input)
        val invalidValue = minValue.minusHours(1)
        val validationResult = validator.validate(invalidValue)

        Truth.assertThat(validationResult)
            .isInstanceOf(FormFieldValidationResult.Invalid.MessageWithArgs::class.java)

        Truth.assertWithMessage("Error message ID should be ${R.string.carlos_lbl_test_invalid_input}")
            .that((validationResult as FormFieldValidationResult.Invalid.MessageWithArgs).errorMessageId)
            .isEqualTo(R.string.carlos_lbl_test_invalid_input)
    }

    @Test
    fun `test invalid input returns error message args`() = runTest {
        val minValue = LocalTime.of(5, 30)

        val validator = TimeMinValidator(minValue)
        val invalidValue = minValue.minusHours(1)
        val validationResult = validator.validate(invalidValue)

        Truth.assertThat(validationResult)
            .isInstanceOf(FormFieldValidationResult.Invalid.MessageWithArgs::class.java)

        Truth.assertWithMessage("Error message arg should be $minValue")
            .that((validationResult as FormFieldValidationResult.Invalid.MessageWithArgs).formatArgs)
            .containsExactly(minValue)
    }
}
