package com.icell.external.carlosformito.core.validator

import com.icell.external.carlosformito.core.R
import com.icell.external.carlosformito.core.util.ValidatorTestUtils.validateAssertInvalid
import com.icell.external.carlosformito.core.util.ValidatorTestUtils.validateAssertValid
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for [ContainsNumberValidator].
 */
class ContainsNumberValidatorTest {

    private lateinit var validator: ContainsNumberValidator

    /**
     * Initializes the [ContainsNumberValidator] with a specific error message ID before each test.
     */
    @Before
    fun initValidator() {
        validator = ContainsNumberValidator(R.string.carlos_lbl_test_invalid_input)
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
     * Tests validation with multiline blank input.
     */
    @Test
    fun `validate multiline line blank input`() = runTest {
        validator.validateAssertInvalid(
            """
                     
                        
                          
            """.trimIndent()
        )
    }

    /**
     * Tests validation with simple input that does not contain any digit.
     */
    @Test
    fun `validate simple no digit input`() = runTest {
        validator.validateAssertInvalid("Test simple")
    }

    /**
     * Tests validation with input that contains no digit but has special characters.
     */
    @Test
    fun `validate no digit input with special chars`() = runTest {
        validator.validateAssertInvalid("testLongLoooooonginput*_.()**~~```</>")
    }

    /**
     * Tests validation with simple input that contains at least one digit.
     */
    @Test
    fun `validate simple has digit input`() = runTest {
        validator.validateAssertValid("test1")
    }

    /**
     * Tests validation with input that contains only one digit.
     */
    @Test
    fun `validate one digit input`() = runTest {
        validator.validateAssertValid("1")
    }

    /**
     * Tests validation with input that contains only digits.
     */
    @Test
    fun `validate digit only input`() = runTest {
        validator.validateAssertValid("23586298")
    }

    /**
     * Tests validation with input that contains at least one digit and special characters.
     */
    @Test
    fun `validate has digit input with special chars`() = runTest {
        validator.validateAssertValid("testLongLoooooonginput*_.()**~~```</>9")
    }

    /**
     * Tests validation with multiline input that contains at least one digit.
     */
    @Test
    fun `validate has digit multiline input`() = runTest {
        validator.validateAssertValid(
            """
                test
                1
                2
                3
            """.trimIndent()
        )
    }

    /**
     * Tests validation with untrimmed input that contains at least one digit.
     */
    @Test
    fun `validate has digit untrimmed input`() = runTest {
        validator.validateAssertValid("  test1   ")
    }
}
