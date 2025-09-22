package hu.icellmobilsoft.carlosformito.core.validator.connections

import hu.icellmobilsoft.carlosformito.core.R
import hu.icellmobilsoft.carlosformito.core.api.validator.FormFieldValidationResult
import hu.icellmobilsoft.carlosformito.core.api.validator.FormValueContext
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class EqualsToValidatorTest {

    @MockK
    lateinit var context: FormValueContext

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxed = true)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `validate should return Valid when field value equals connected field value`() = runTest {
        val validator = EqualsToValidator<String>(
            connectedFieldId = CONNECTED_FIELD_ID
        )

        every { context.getFieldValue<String>(CONNECTED_FIELD_ID) } returns "testValue"

        val result = validator.validate("testValue", context)
        assertEquals(FormFieldValidationResult.Valid, result)
    }

    @Test
    fun `validate should return Invalid when field value does not equal connected field value`() = runTest {
        val validator = EqualsToValidator<String>(
            connectedFieldId = CONNECTED_FIELD_ID,
            errorMessageId = R.string.carlos_lbl_test_invalid_input
        )

        every { context.getFieldValue<String>(CONNECTED_FIELD_ID) } returns "expectedValue"

        val result = validator.validate("actualValue", context)
        assertTrue(result is FormFieldValidationResult.Invalid)
        assertEquals(
            R.string.carlos_lbl_test_invalid_input,
            (result as FormFieldValidationResult.Invalid).errorMessageId
        )
    }

    @Test
    fun `validate should return Invalid when field value is null and connected field value is not`() = runTest {
        val validator = EqualsToValidator<String>(
            connectedFieldId = CONNECTED_FIELD_ID,
            errorMessageId = R.string.carlos_lbl_test_invalid_input
        )

        every { context.getFieldValue<String>(CONNECTED_FIELD_ID) } returns "expectedValue"

        val result = validator.validate(null, context)
        assertTrue(result is FormFieldValidationResult.Invalid)
        assertEquals(
            R.string.carlos_lbl_test_invalid_input,
            (result as FormFieldValidationResult.Invalid).errorMessageId
        )
    }

    @Test
    fun `validate should return Valid when both field value and connected field value are null`() = runTest {
        val validator = EqualsToValidator<String>(
            connectedFieldId = CONNECTED_FIELD_ID
        )

        every { context.getFieldValue<String>(CONNECTED_FIELD_ID) } returns null

        val result = validator.validate(null, context)
        assertEquals(FormFieldValidationResult.Valid, result)
    }

    @Test
    fun `validate should return Invalid when field value is not null and connected field value is null`() = runTest {
        val validator = EqualsToValidator<String>(
            connectedFieldId = CONNECTED_FIELD_ID,
            errorMessageId = R.string.carlos_lbl_test_invalid_input
        )

        every { context.getFieldValue<String>(CONNECTED_FIELD_ID) } returns null

        val result = validator.validate("actualValue", context)
        assertTrue(result is FormFieldValidationResult.Invalid)
        assertEquals(
            R.string.carlos_lbl_test_invalid_input,
            (result as FormFieldValidationResult.Invalid).errorMessageId
        )
    }

    companion object {
        private const val CONNECTED_FIELD_ID = "CONNECTED_FIELD_ID"
    }
}
