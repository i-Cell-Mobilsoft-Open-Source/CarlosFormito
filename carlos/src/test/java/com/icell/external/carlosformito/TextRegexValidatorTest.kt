package com.icell.external.carlosformito

import com.google.common.truth.Truth.assertThat
import com.google.common.truth.Truth.assertWithMessage
import com.icell.external.carlosformito.api.validator.FormFieldValidationResult
import com.icell.external.carlosformito.ui.validator.TextRegexValidator
import com.icell.external.carlosformito.util.ValidatorTestUtils.isValidationResultInvalid
import kotlinx.coroutines.test.runTest
import org.junit.Test

class TextRegexValidatorTest {

    @Test
    fun `validate null input with non-empty input requiring regex`() = runTest {
        val validator = TextRegexValidator("[a-zA-Z]")
        val validationResult = validator.validate(null)

        assertThat(validationResult).isValidationResultInvalid()
    }

    @Test
    fun `validate null input with empty input requiring regex`() = runTest {
        val validator = TextRegexValidator("^\$")
        val validationResult = validator.validate(null)

        assertThat(validationResult).isEqualTo(FormFieldValidationResult.Valid)
    }

    @Test
    fun `validate input matching pattern`() = runTest {
        val validator = TextRegexValidator("\\d{3}-\\d{3}-\\d{4}")
        val validationResult = validator.validate("123-456-7890")

        assertThat(validationResult).isEqualTo(FormFieldValidationResult.Valid)
    }

    @Test
    fun `validate input not matching pattern`() = runTest {
        val validator = TextRegexValidator("\\d{3}-\\d{3}-\\d{4}")
        val validationResult = validator.validate("kiskutya")

        assertThat(validationResult).isValidationResultInvalid()
    }

    @Test
    fun `test invalid input returns custom error message`() = runTest {
        val validator = TextRegexValidator("\\s", R.string.carlos_lbl_test_invalid_input)
        val validationResult = validator.validate(null)

        assertThat(validationResult)
            .isInstanceOf(FormFieldValidationResult.Invalid.Message::class.java)

        assertWithMessage("Error message ID should be ${R.string.carlos_lbl_test_invalid_input}")
            .that((validationResult as FormFieldValidationResult.Invalid.Message).errorMessageId)
            .isEqualTo(R.string.carlos_lbl_test_invalid_input)
    }
}
