package com.icell.external.carlosformito.core

import com.icell.external.carlosformito.core.util.ValidatorTestUtils.validateAssertInvalid
import com.icell.external.carlosformito.core.util.ValidatorTestUtils.validateAssertValid
import com.icell.external.carlosformito.core.validator.ContainsSpecialCharacterValidator
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class ContainsSpecialCharacterValidatorTest {

    private lateinit var validator: ContainsSpecialCharacterValidator

    @Before
    fun initValidator() {
        validator = ContainsSpecialCharacterValidator(R.string.carlos_lbl_test_invalid_input)
    }

    @Test
    fun `validate null input`() = runTest {
        validator.validateAssertValid(null)
    }

    @Test
    fun `validate empty input`() = runTest {
        validator.validateAssertValid("")
    }

    @Test
    fun `validate blank input`() = runTest {
        validator.validateAssertInvalid("     ")
    }

    @Test
    fun `validate multiline blank input`() = runTest {
        validator.validateAssertInvalid(
            """
                     
                        
                          
            """.trimIndent()
        )
    }

    @Test
    fun `validate simple no special char input`() = runTest {
        validator.validateAssertInvalid("Test simple")
    }

    @Test
    fun `validate long no special input`() = runTest {
        validator.validateAssertInvalid("Lorem Ipsum is simply dummy text of the printing and typesetting industry")
    }

    @Test
    fun `validate simple has special char input`() = runTest {
        validator.validateAssertValid("Test12345*_-")
    }

    @Test
    fun `validate special char only input`() = runTest {
        validator.validateAssertValid("~<>*()-_``'\"%#+.:{}§!@|")
    }

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

    @Test
    fun `validate has special char untrimmed input`() = runTest {
        validator.validateAssertValid("  test*   ")
    }
}
