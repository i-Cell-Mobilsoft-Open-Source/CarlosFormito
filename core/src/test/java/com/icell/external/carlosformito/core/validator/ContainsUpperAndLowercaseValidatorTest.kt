package com.icell.external.carlosformito.core.validator

import com.icell.external.carlosformito.core.R
import com.icell.external.carlosformito.core.util.ValidatorTestUtils.validateAssertInvalid
import com.icell.external.carlosformito.core.util.ValidatorTestUtils.validateAssertValid
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for [ContainsUpperAndLowercaseValidator].
 */
class ContainsUpperAndLowercaseValidatorTest {

    private lateinit var validator: ContainsUpperAndLowercaseValidator

    /**
     * Initializes the [ContainsUpperAndLowercaseValidator] with a specific error message ID before each test.
     */
    @Before
    fun initValidator() {
        validator = ContainsUpperAndLowercaseValidator(R.string.carlos_lbl_test_invalid_input)
    }

    /**
     * Tests validation with null input.
     */
    @Test
    fun `validate null input`() = runTest {
        validator.validateAssertValid(null)
    }

    /**
     * Tests validation with empty input.
     */
    @Test
    fun `validate empty input`() = runTest {
        validator.validateAssertValid("")
    }

    /**
     * Tests validation with blank input.
     */
    @Test
    fun `validate blank input`() = runTest {
        validator.validateAssertInvalid("     ")
    }

    /**
     * Tests validation with input that lacks both uppercase and lowercase characters.
     */
    @Test
    fun `validate input without uppercase and lowercase characters`() = runTest {
        validator.validateAssertInvalid("12345*.")
    }

    /**
     * Tests validation with input that contains only uppercase characters.
     */
    @Test
    fun `validate input with only uppercase characters`() = runTest {
        validator.validateAssertInvalid("ABCDEF")
    }

    /**
     * Tests validation with input that contains only lowercase characters.
     */
    @Test
    fun `validate input with only lowercase characters`() = runTest {
        validator.validateAssertInvalid("abcdef")
    }

    /**
     * Tests validation with input that contains both uppercase and lowercase characters.
     */
    @Test
    fun `validate input with both uppercase and lowercase characters`() = runTest {
        validator.validateAssertValid("Abcdef")
    }
}
