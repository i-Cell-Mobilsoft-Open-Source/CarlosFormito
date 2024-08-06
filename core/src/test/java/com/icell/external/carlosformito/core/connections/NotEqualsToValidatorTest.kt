package com.icell.external.carlosformito.core.connections

import com.icell.external.carlosformito.core.R
import com.icell.external.carlosformito.core.api.validator.FormFieldValidationResult
import com.icell.external.carlosformito.core.api.validator.FormValueContext
import com.icell.external.carlosformito.core.validator.connections.NotEqualsToValidator
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

class NotEqualsToValidatorTest {

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
    fun `validate should return Valid when field value does not equal connected field value`() = runTest {
        val validator = NotEqualsToValidator<String>(
            connectedFieldId = CONNECTED_FIELD_ID
        )

        every { context.getFieldValue<String>(CONNECTED_FIELD_ID) } returns "expectedValue"

        val result = validator.validate("actualValue", context)
        assertEquals(FormFieldValidationResult.Valid, result)
    }

    @Test
    fun `validate should return Invalid when field value equals connected field value`() = runTest {
        val validator = NotEqualsToValidator<String>(
            connectedFieldId = CONNECTED_FIELD_ID,
            errorMessageId = R.string.carlos_lbl_test_invalid_input
        )

        every { context.getFieldValue<String>(CONNECTED_FIELD_ID) } returns "expectedValue"

        val result = validator.validate("expectedValue", context)
        assertTrue(result is FormFieldValidationResult.Invalid)
        assertEquals(
            R.string.carlos_lbl_test_invalid_input,
            (result as FormFieldValidationResult.Invalid).errorMessageId
        )
    }

    @Test
    fun `validate should return Invalid when field value is null and connected field value is null`() = runTest {
        val validator = NotEqualsToValidator<String>(
            connectedFieldId = CONNECTED_FIELD_ID,
            errorMessageId = R.string.carlos_lbl_test_invalid_input
        )

        every { context.getFieldValue<String>(CONNECTED_FIELD_ID) } returns null

        val result = validator.validate(null, context)
        assertTrue(result is FormFieldValidationResult.Invalid)
        assertEquals(
            R.string.carlos_lbl_test_invalid_input,
            (result as FormFieldValidationResult.Invalid).errorMessageId
        )
    }

    @Test
    fun `validate should return Valid when field value is null and connected field value is not`() = runTest {
        val validator = NotEqualsToValidator<String>(
            connectedFieldId = CONNECTED_FIELD_ID
        )

        every { context.getFieldValue<String>(CONNECTED_FIELD_ID) } returns "expectedValue"

        val result = validator.validate(null, context)
        assertEquals(FormFieldValidationResult.Valid, result)
    }

    @Test
    fun `validate should return Valid when field value is not null and connected field value is null`() = runTest {
        val validator = NotEqualsToValidator<String>(
            connectedFieldId = CONNECTED_FIELD_ID
        )

        every { context.getFieldValue<String>(CONNECTED_FIELD_ID) } returns null

        val result = validator.validate("actualValue", context)
        assertEquals(FormFieldValidationResult.Valid, result)
    }

    companion object {
        private const val CONNECTED_FIELD_ID = "CONNECTED_FIELD_ID"
    }
}
