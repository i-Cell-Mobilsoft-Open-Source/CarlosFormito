package com.icell.external.carlosformito.core

import com.google.common.truth.Truth.assertThat
import com.google.common.truth.Truth.assertWithMessage
import com.icell.external.carlosformito.core.api.validator.FormFieldValidationResult
import com.icell.external.carlosformito.core.util.ValidatorTestUtils.isValidationResultInvalid
import com.icell.external.carlosformito.core.validator.ValueRequiredValidator
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.time.LocalDate

/**
 * Unit tests for [ValueRequiredValidator].
 */
class ValueRequiredValidatorTest {

    /**
     * Tests validation with null input.
     * It should return [FormFieldValidationResult.Invalid].
     */
    @Test
    fun `validate null input`() = runTest {
        val validator = ValueRequiredValidator<String>()
        val validatorResult = validator.validate(null)

        assertThat(validatorResult).isValidationResultInvalid()
    }

    /**
     * Tests validation with an empty string input.
     * It should return [FormFieldValidationResult.Invalid].
     */
    @Test
    fun `validate empty string input`() = runTest {
        val validator = ValueRequiredValidator<String>()
        val validatorResult = validator.validate("")

        assertThat(validatorResult).isValidationResultInvalid()
    }

    /**
     * Tests validation with a blank string input.
     * It should return [FormFieldValidationResult.Invalid].
     */
    @Test
    fun `validate blank string input`() = runTest {
        val validator = ValueRequiredValidator<String>()
        val validatorResult = validator.validate("     ")

        assertThat(validatorResult).isValidationResultInvalid()
    }

    /**
     * Tests validation with a multiline blank string input.
     * It should return [FormFieldValidationResult.Invalid].
     */
    @Test
    fun `validate multiline blank string input`() = runTest {
        val validator = ValueRequiredValidator<String>()
        val validatorResult = validator.validate(
            """
                     
                        
                          
            """.trimIndent()
        )

        assertThat(validatorResult).isValidationResultInvalid()
    }

    /**
     * Tests validation with a simple non-empty string input.
     * It should return [FormFieldValidationResult.Valid].
     */
    @Test
    fun `validate simple string input`() = runTest {
        val validator = ValueRequiredValidator<String>()
        val validatorResult = validator.validate("text")

        assertThat(validatorResult).isEqualTo(FormFieldValidationResult.Valid)
    }

    /**
     * Tests validation with a multiline non-empty string input.
     * It should return [FormFieldValidationResult.Valid].
     */
    @Test
    fun `validate multiline string input`() = runTest {
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

    /**
     * Tests validation with a non-null Integer input.
     * It should return [FormFieldValidationResult.Valid].
     */
    @Test
    fun `validate Integer input`() = runTest {
        val validator = ValueRequiredValidator<Int>()
        val validatorResult = validator.validate(Int.MAX_VALUE)

        assertThat(validatorResult).isEqualTo(FormFieldValidationResult.Valid)
    }

    /**
     * Tests validation with a non-null LocalDate input.
     * It should return [FormFieldValidationResult.Valid].
     */
    @Test
    fun `validate LocalDate input`() = runTest {
        val validator = ValueRequiredValidator<LocalDate>()
        val validatorResult = validator.validate(LocalDate.MIN)

        assertThat(validatorResult).isEqualTo(FormFieldValidationResult.Valid)
    }

    /**
     * Tests validation with null input and expects a custom error message.
     * It should return a [FormFieldValidationResult.Invalid.Message] with the specified error message ID.
     */
    @Test
    fun `test invalid input returns custom error message`() = runTest {
        val validator = ValueRequiredValidator<Any>(R.string.carlos_lbl_test_invalid_input)
        val validationResult = validator.validate(null)

        assertThat(validationResult)
            .isInstanceOf(FormFieldValidationResult.Invalid.Message::class.java)

        assertWithMessage("Error message ID should be ${R.string.carlos_lbl_test_invalid_input}")
            .that((validationResult as FormFieldValidationResult.Invalid.Message).errorMessageId)
            .isEqualTo(R.string.carlos_lbl_test_invalid_input)
    }
}
