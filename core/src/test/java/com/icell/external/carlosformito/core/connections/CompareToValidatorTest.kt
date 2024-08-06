package com.icell.external.carlosformito.core.connections

import com.icell.external.carlosformito.core.R
import com.icell.external.carlosformito.core.api.validator.FormFieldValidationResult
import com.icell.external.carlosformito.core.api.validator.FormValueContext
import com.icell.external.carlosformito.core.validator.connections.CompareToValidator
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

class CompareToValidatorTest {

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
    fun `validate should return Valid when field value is greater than connected field value`() = runTest {
        val validator = CompareToValidator<Int>(
            connectedFieldId = CONNECTED_FIELD_ID,
            compare = { fieldValue, connectedFieldValue -> fieldValue > connectedFieldValue }
        )

        every { context.getFieldValue<Int>(CONNECTED_FIELD_ID) } returns 10

        val result = validator.validate(20, context)
        assertEquals(FormFieldValidationResult.Valid, result)
    }

    @Test
    fun `validate should return Invalid when field value is not greater than connected field value`() = runTest {
        val validator = CompareToValidator<Int>(
            connectedFieldId = CONNECTED_FIELD_ID,
            errorMessageId = R.string.carlos_lbl_test_invalid_input,
            compare = { fieldValue, connectedFieldValue -> fieldValue > connectedFieldValue }
        )

        every { context.getFieldValue<Int>(CONNECTED_FIELD_ID) } returns 20

        val result = validator.validate(10, context)
        assertTrue(result is FormFieldValidationResult.Invalid)
        assertEquals(
            R.string.carlos_lbl_test_invalid_input,
            (result as FormFieldValidationResult.Invalid).errorMessageId
        )
    }

    @Test
    fun `validate should return Valid when field value is null`() = runTest {
        val validator = CompareToValidator<Int>(
            connectedFieldId = CONNECTED_FIELD_ID,
            compare = { fieldValue, connectedFieldValue -> fieldValue > connectedFieldValue }
        )

        every { context.getFieldValue<Int>(CONNECTED_FIELD_ID) } returns 10

        val result = validator.validate(null, context)
        assertEquals(FormFieldValidationResult.Valid, result)
    }

    @Test
    fun `validate should return Valid when connected field value is null`() = runTest {
        val validator = CompareToValidator<Int>(
            connectedFieldId = CONNECTED_FIELD_ID,
            compare = { fieldValue, connectedFieldValue -> fieldValue > connectedFieldValue }
        )

        every { context.getFieldValue<Int>(CONNECTED_FIELD_ID) } returns null

        val result = validator.validate(10, context)
        assertEquals(FormFieldValidationResult.Valid, result)
    }

    @Test
    fun `validate should return Valid when field value is less than connected field value`() = runTest {
        val validator = CompareToValidator<Int>(
            connectedFieldId = CONNECTED_FIELD_ID,
            compare = { fieldValue, connectedFieldValue -> fieldValue < connectedFieldValue }
        )

        every { context.getFieldValue<Int>(CONNECTED_FIELD_ID) } returns 20

        val result = validator.validate(10, context)
        assertEquals(FormFieldValidationResult.Valid, result)
    }

    @Test
    fun `validate should return Invalid when field value is not less than connected field value`() = runTest {
        val validator = CompareToValidator<Int>(
            connectedFieldId = CONNECTED_FIELD_ID,
            errorMessageId = R.string.carlos_lbl_test_invalid_input,
            compare = { fieldValue, connectedFieldValue -> fieldValue < connectedFieldValue }
        )

        every { context.getFieldValue<Int>(CONNECTED_FIELD_ID) } returns 10

        val result = validator.validate(20, context)
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
