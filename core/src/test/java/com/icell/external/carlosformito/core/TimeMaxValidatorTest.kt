package com.icell.external.carlosformito.core

import com.google.common.truth.Truth.assertThat
import com.google.common.truth.Truth.assertWithMessage
import com.icell.external.carlosformito.core.api.validator.FormFieldValidationResult
import com.icell.external.carlosformito.core.util.ValidatorTestUtils.isValidationResultInvalid
import com.icell.external.carlosformito.core.validator.TimeMaxValidator
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.time.LocalTime

class TimeMaxValidatorTest {

    @Test
    fun `validate null input`() = runTest {
        val validator = TimeMaxValidator(LocalTime.now())
        val validationResult = validator.validate(null)

        assertThat(validationResult).isEqualTo(FormFieldValidationResult.Valid)
    }

    @Test
    fun `validate time before max value`() = runTest {
        val maxValue = LocalTime.of(5, 30)
        val validator = TimeMaxValidator(maxValue)
        val validationResult = validator.validate(maxValue.minusHours(1))

        assertThat(validationResult).isEqualTo(FormFieldValidationResult.Valid)
    }

    @Test
    fun `validate time equal to max value`() = runTest {
        val maxValue = LocalTime.of(5, 30)
        val validator = TimeMaxValidator(maxValue)
        val validationResult = validator.validate(maxValue)

        assertThat(validationResult).isEqualTo(FormFieldValidationResult.Valid)
    }

    @Test
    fun `validate time after max value`() = runTest {
        val maxValue = LocalTime.of(5, 30)
        val validator = TimeMaxValidator(maxValue)
        val validationResult = validator.validate(maxValue.plusHours(1))

        assertThat(validationResult).isValidationResultInvalid()
    }

    @Test
    fun `test invalid input returns custom error message`() = runTest {
        val maxValue = LocalTime.of(5, 30)
        val validator = TimeMaxValidator(maxValue, R.string.carlos_lbl_test_invalid_input)
        val validationResult = validator.validate(maxValue.plusHours(1))

        assertThat(validationResult)
            .isInstanceOf(FormFieldValidationResult.Invalid.MessageWithArgs::class.java)

        assertWithMessage("Error message ID should be ${R.string.carlos_lbl_test_invalid_input}")
            .that((validationResult as FormFieldValidationResult.Invalid.MessageWithArgs).errorMessageId)
            .isEqualTo(R.string.carlos_lbl_test_invalid_input)
    }

    @Test
    fun `test invalid input returns error message args`() = runTest {
        val maxValue = LocalTime.of(5, 30)
        val validator = TimeMaxValidator(maxValue)
        val validationResult = validator.validate(maxValue.plusHours(1))

        assertThat(validationResult)
            .isInstanceOf(FormFieldValidationResult.Invalid.MessageWithArgs::class.java)

        assertWithMessage("Error message arg should be $maxValue")
            .that((validationResult as FormFieldValidationResult.Invalid.MessageWithArgs).formatArgs)
            .containsExactly(maxValue)
    }
}
