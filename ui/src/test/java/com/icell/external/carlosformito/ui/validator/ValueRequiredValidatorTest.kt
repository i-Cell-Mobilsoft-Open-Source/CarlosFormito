package com.icell.external.carlosformito.ui.validator

import com.google.common.truth.Truth.assertThat
import com.google.common.truth.Truth.assertWithMessage
import com.icell.external.carlosformito.core.api.validator.FormFieldValidationResult
import com.icell.external.carlosformito.ui.R
import com.icell.external.carlosformito.ui.validator.util.ValidatorTestUtils.isValidationResultInvalid
import org.junit.Test
import java.time.LocalDate

class ValueRequiredValidatorTest {

    @Test
    fun `validate null input`() {
        val validator = ValueRequiredValidator<String>()
        val validatorResult = validator.validate(null)

        assertThat(validatorResult).isValidationResultInvalid()
    }

    @Test
    fun `validate empty string input`() {
        val validator = ValueRequiredValidator<String>()
        val validatorResult = validator.validate("")

        assertThat(validatorResult).isValidationResultInvalid()
    }

    @Test
    fun `validate blank string input`() {
        val validator = ValueRequiredValidator<String>()
        val validatorResult = validator.validate("     ")

        assertThat(validatorResult).isValidationResultInvalid()
    }

    @Test
    fun `validate multiline blank string input`() {
        val validator = ValueRequiredValidator<String>()
        val validatorResult = validator.validate(
            """
                     
                        
                          
            """.trimIndent()
        )

        assertThat(validatorResult).isValidationResultInvalid()
    }

    @Test
    fun `validate simple string input`() {
        val validator = ValueRequiredValidator<String>()
        val validatorResult = validator.validate("text")

        assertThat(validatorResult).isEqualTo(FormFieldValidationResult.Valid)
    }

    @Test
    fun `validate multiline string input`() {
        val validator = ValueRequiredValidator<String>()
        val validatorResult = validator.validate(
            """
                text
                1
                2
                3
            """.trimIndent()
        )

        assertThat(validatorResult).isEqualTo(FormFieldValidationResult.Valid)
    }

    @Test
    fun `validate Integer input`() {
        val validator = ValueRequiredValidator<Int>()
        val validatorResult = validator.validate(Int.MAX_VALUE)

        assertThat(validatorResult).isEqualTo(FormFieldValidationResult.Valid)
    }

    @Test
    fun `validate LocalDate input`() {
        val validator = ValueRequiredValidator<LocalDate>()
        val validatorResult = validator.validate(LocalDate.MIN)

        assertThat(validatorResult).isEqualTo(FormFieldValidationResult.Valid)
    }

    @Test
    fun `test invalid input returns custom error message`() {
        val validator = ValueRequiredValidator<Any>(R.string.carlos_lbl_test_invalid_input)
        val validationResult = validator.validate(null)

        assertThat(validationResult)
            .isInstanceOf(FormFieldValidationResult.Invalid.Message::class.java)

        assertWithMessage("Error message ID should be ${R.string.carlos_lbl_test_invalid_input}")
            .that((validationResult as FormFieldValidationResult.Invalid.Message).errorMessageId)
            .isEqualTo(R.string.carlos_lbl_test_invalid_input)
    }
}
