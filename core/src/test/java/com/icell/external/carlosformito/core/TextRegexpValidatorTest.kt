package com.icell.external.carlosformito.core

import com.google.common.truth.Truth.assertThat
import com.google.common.truth.Truth.assertWithMessage
import com.icell.external.carlosformito.core.api.validator.FormFieldValidationResult
import com.icell.external.carlosformito.core.util.ValidatorTestUtils.isValidationResultInvalid
import com.icell.external.carlosformito.core.validator.TextRegexValidator
import kotlinx.coroutines.test.runTest
import org.junit.Test

/**
 * Unit tests for [TextRegexValidator].
 */
class TextRegexpValidatorTest {

    /**
     * Tests validation with null input when the regex pattern allows non-empty input.
     * It should always return [FormFieldValidationResult.Valid].
     */
    @Test
    fun `validate null input with non-empty input requiring regex`() = runTest {
        val validator = TextRegexValidator("[a-zA-Z]")
        val validationResult = validator.validate(null)

        assertThat(validationResult).isEqualTo(FormFieldValidationResult.Valid)
    }

    /**
     * Tests validation with null input when the regex pattern expects empty input (`^$`).
     * It should always return [FormFieldValidationResult.Valid].
     */
    @Test
    fun `validate null input with empty input requiring regex`() = runTest {
        val validator = TextRegexValidator("^\$")
        val validationResult = validator.validate(null)

        assertThat(validationResult).isEqualTo(FormFieldValidationResult.Valid)
    }

    /**
     * Tests validation with input matching the specified regex pattern (`\d{3}-\d{3}-\d{4}`).
     * It should return [FormFieldValidationResult.Valid].
     */
    @Test
    fun `validate input matching pattern`() = runTest {
        val validator = TextRegexValidator("\\d{3}-\\d{3}-\\d{4}")
        val validationResult = validator.validate("123-456-7890")

        assertThat(validationResult).isEqualTo(FormFieldValidationResult.Valid)
    }

    /**
     * Tests validation with input not matching the specified regex pattern (`\d{3}-\d{3}-\d{4}`).
     * It should return an invalid result.
     */
    @Test
    fun `validate input not matching pattern`() = runTest {
        val validator = TextRegexValidator("\\d{3}-\\d{3}-\\d{4}")
        val validationResult = validator.validate("kiskutya")

        assertThat(validationResult).isValidationResultInvalid()
    }

    /**
     * Tests validation with invalid input and expects a custom error message.
     * It should return a [FormFieldValidationResult.Invalid.Message] with the specified error message ID.
     */
    @Test
    fun `test invalid input returns custom error message`() = runTest {
        val validator = TextRegexValidator("\\s", R.string.carlos_lbl_test_invalid_input)
        val validationResult = validator.validate("non-whitespace-string")

        assertThat(validationResult)
            .isInstanceOf(FormFieldValidationResult.Invalid.Message::class.java)

        assertWithMessage("Error message ID should be ${R.string.carlos_lbl_test_invalid_input}")
            .that((validationResult as FormFieldValidationResult.Invalid.Message).errorMessageId)
            .isEqualTo(R.string.carlos_lbl_test_invalid_input)
    }
}
