package hu.icellmobilsoft.carlosformito.core.validator

import hu.icellmobilsoft.carlosformito.core.R
import hu.icellmobilsoft.carlosformito.core.util.ValidatorTestUtils.validateAssertInvalid
import hu.icellmobilsoft.carlosformito.core.util.ValidatorTestUtils.validateAssertValid
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for [ContainsSpecialCharacterValidator].
 */
class ContainsSpecialCharacterValidatorTest {

    private lateinit var validator: ContainsSpecialCharacterValidator

    /**
     * Initializes the [ContainsSpecialCharacterValidator] with a specific error message ID before each test.
     */
    @Before
    fun initValidator() {
        validator = ContainsSpecialCharacterValidator(R.string.carlos_lbl_test_invalid_input)
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
    fun `validate multiline blank input`() = runTest {
        validator.validateAssertInvalid(
            """
                     
                        
                          
            """.trimIndent()
        )
    }

    /**
     * Tests validation with simple input that contains no special characters.
     */
    @Test
    fun `validate simple no special char input`() = runTest {
        validator.validateAssertInvalid("Test simple")
    }

    /**
     * Tests validation with long input that contains no special characters.
     */
    @Test
    fun `validate long no special input`() = runTest {
        validator.validateAssertInvalid("Lorem Ipsum is simply dummy text of the printing and typesetting industry")
    }

    /**
     * Tests validation with simple input that contains at least one special character.
     */
    @Test
    fun `validate simple has special char input`() = runTest {
        validator.validateAssertValid("Test12345*_-")
    }

    /**
     * Tests validation with input that contains only special characters.
     */
    @Test
    fun `validate special char only input`() = runTest {
        validator.validateAssertValid("~<>*()-_``'\"%#+.:{}§!@|")
    }

    /**
     * Tests validation with multiline input that contains at least one special character.
     */
    @Test
    fun `validate has special char multiline input`() = runTest {
        validator.validateAssertValid(
            """
                test
                
                *
                ~~
            """.trimIndent()
        )
    }

    /**
     * Tests validation with untrimmed input that contains at least one special character.
     */
    @Test
    fun `validate has special char untrimmed input`() = runTest {
        validator.validateAssertValid("  test*   ")
    }
}
