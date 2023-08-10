package com.icell.external.carlosformito.ui.validator

import com.icell.external.carlosformito.ui.R
import com.icell.external.carlosformito.ui.validator.util.ValidatorTestUtils.validateAssertInvalid
import com.icell.external.carlosformito.ui.validator.util.ValidatorTestUtils.validateAssertValid
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class ContainsNumberValidatorTest {

    private lateinit var validator: ContainsNumberValidator

    @Before
    fun initValidator() {
        validator = ContainsNumberValidator(R.string.carlos_lbl_test_invalid_input)
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
    fun `validate multiline line blank input`() = runTest {
        validator.validateAssertInvalid(
            """
                     
                        
                          
            """.trimIndent()
        )
    }

    @Test
    fun `validate simple no digit input`() = runTest {
        validator.validateAssertInvalid("Test simple")
    }

    @Test
    fun `validate no digit input with special chars`() = runTest {
        validator.validateAssertInvalid("testLongLoooooonginput*_.()**~~```</>")
    }

    @Test
    fun `validate simple has digit input`() = runTest {
        validator.validateAssertValid("test1")
    }

    @Test
    fun `validate one digit input`() = runTest {
        validator.validateAssertValid("1")
    }

    @Test
    fun `validate digit only input`() = runTest {
        validator.validateAssertValid("23586298")
    }

    @Test
    fun `validate has digit input with special chars`() = runTest {
        validator.validateAssertValid("testLongLoooooonginput*_.()**~~```</>9")
    }

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

    @Test
    fun `validate has digit untrimmed input`() = runTest {
        validator.validateAssertValid("  test1   ")
    }
}
