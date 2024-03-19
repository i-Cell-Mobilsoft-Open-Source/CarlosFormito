package com.icell.external.carlosformito.core

import com.icell.external.carlosformito.core.util.ValidatorTestUtils.validateAssertInvalid
import com.icell.external.carlosformito.core.util.ValidatorTestUtils.validateAssertValid
import com.icell.external.carlosformito.core.validator.ContainsUpperAndLowercaseValidator
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class ContainsUpperAndLowercaseValidatorTest {

    private lateinit var validator: ContainsUpperAndLowercaseValidator

    @Before
    fun initValidator() {
        validator = ContainsUpperAndLowercaseValidator(R.string.carlos_lbl_test_invalid_input)
    }

    @Test
    fun `validate null input`() = runTest {
        validator.validateAssertInvalid(null)
    }

    @Test
    fun `validate empty input`() = runTest {
        validator.validateAssertInvalid("")
    }

    @Test
    fun `validate blank input`() = runTest {
        validator.validateAssertInvalid("     ")
    }

    @Test
    fun `validate input without uppercase and lowercase characters`() = runTest {
        validator.validateAssertInvalid("12345*.")
    }

    @Test
    fun `validate input with only uppercase characters`() = runTest {
        validator.validateAssertInvalid("ABCDEF")
    }

    @Test
    fun `validate input with only lowercase characters`() = runTest {
        validator.validateAssertInvalid("abcdef")
    }

    @Test
    fun `validate input with both uppercase and lowercase characters`() = runTest {
        validator.validateAssertValid("Abcdef")
    }
}
