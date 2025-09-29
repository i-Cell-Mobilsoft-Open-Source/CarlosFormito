package hu.icellmobilsoft.carlosformito.core

import com.google.common.truth.Truth.assertThat
import hu.icellmobilsoft.carlosformito.core.api.model.FormField
import hu.icellmobilsoft.carlosformito.core.api.model.FormFieldValidationStrategy
import hu.icellmobilsoft.carlosformito.core.api.validator.FormFieldValidationResult
import hu.icellmobilsoft.carlosformito.core.api.validator.FormFieldValidator
import hu.icellmobilsoft.carlosformito.core.validator.ValueRequiredValidator
import hu.icellmobilsoft.carlosformito.core.validator.connections.EqualsToValidator
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.cancellation.CancellationException
import kotlin.jvm.Throws
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

@Suppress("MaxLineLength")
@OptIn(ExperimentalCoroutinesApi::class)
class CarlosFormManagerTest {

    @MockK
    lateinit var mockValidator: FormFieldValidator<String>

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxed = true)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `AutoOnFocusClear strategy validates on focus clear`() = runTest {
        val formField = FormField(
            id = TEST_FORM_FIELD_ID,
            validators = listOf(mockValidator)
        )

        coEvery { mockValidator.validate(any()) } returns FormFieldValidationResult.Invalid.Unknown

        val manager = CarlosFormManager(
            formFields = listOf(formField),
            validationStrategy = FormFieldValidationStrategy.AutoOnFocusClear,
        )

        manager.initFormManager()
        advanceTimeBy(1.seconds)

        // Ensure field is visible for validation
        manager.onFieldVisibilityChanged(TEST_FORM_FIELD_ID, true)
        manager.onFieldFocusCleared(TEST_FORM_FIELD_ID)
        advanceTimeBy(1.seconds)

        coVerify {
            mockValidator.validate(any())
        }
    }

    @Test
    fun `AutoInline strategy validates with delay on value change`() = runTest {
        val formField = FormField(
            id = TEST_FORM_FIELD_ID,
            initialValue = "Initial value",
            validators = listOf(mockValidator)
        )

        coEvery { mockValidator.validate(any()) } returns FormFieldValidationResult.Invalid.Unknown

        val manager = CarlosFormManager(
            formFields = listOf(formField),
            validationStrategy = FormFieldValidationStrategy.AutoInline(delay = 400.milliseconds)
        )

        manager.initFormManager()
        advanceTimeBy(1.seconds)

        // Ensure field is visible for validation
        manager.onFieldVisibilityChanged(TEST_FORM_FIELD_ID, true)
        manager.onFieldValueChanged(TEST_FORM_FIELD_ID, "New value")
        advanceTimeBy(300)

        val validationResult: FormFieldValidationResult? =
            manager.getFieldItem<String>(TEST_FORM_FIELD_ID).fieldState.value.validationResult

        assertThat(validationResult).isNull()

        advanceTimeBy(200)
        val newValidationResult: FormFieldValidationResult? =
            manager.getFieldItem<String>(TEST_FORM_FIELD_ID).fieldState.value.validationResult

        assertThat(newValidationResult).isNotNull()
    }

    @Test
    fun `Field uses custom validation strategy instead of global`() = runTest {
        val formField = FormField(
            id = TEST_FORM_FIELD_ID,
            initialValue = "Initial value",
            validators = listOf(mockValidator),
            customValidationStrategy = FormFieldValidationStrategy.AutoOnFocusClear
        )

        coEvery { mockValidator.validate(any()) } returns FormFieldValidationResult.Invalid.Unknown

        val manager = CarlosFormManager(
            formFields = listOf(formField),
            validationStrategy = FormFieldValidationStrategy.Manual
        )

        manager.initFormManager()
        advanceTimeBy(1.seconds)

        manager.onFieldVisibilityChanged(TEST_FORM_FIELD_ID, true)
        manager.onFieldFocusCleared(TEST_FORM_FIELD_ID)
        advanceTimeBy(1.seconds)

        val validationResult: FormFieldValidationResult? =
            manager.getFieldItem<String>(TEST_FORM_FIELD_ID).fieldState.value.validationResult

        assertThat(validationResult).isNotNull()
    }

    @Test
    fun `Manual strategy - Clears the validation results for the connected field when the fields value changes`() =
        runTest {
            val passwordId = "password"
            val confirmPasswordId = "confirmPassword"
            val passwordField = FormField(
                id = passwordId,
                initialValue = "Password123*",
                validators = listOf(mockValidator)
            )

            coEvery { mockValidator.validate(any()) } returns FormFieldValidationResult.Invalid.Unknown

            val equalsToValidator = EqualsToValidator<String>(connectedFieldId = passwordId)
            val confirmField = FormField(
                id = confirmPasswordId,
                initialValue = "Password12",
                initialValidationResult = FormFieldValidationResult.Invalid.Unknown,
                validators = listOf(equalsToValidator)
            )

            val manager = CarlosFormManager(
                formFields = listOf(passwordField, confirmField),
                validationStrategy = FormFieldValidationStrategy.Manual
            )

            manager.initFormManager()
            advanceTimeBy(1.seconds)

            manager.onFieldVisibilityChanged(passwordId, true)
            manager.onFieldVisibilityChanged(confirmPasswordId, true)
            advanceTimeBy(1.seconds)

            val confirmPasswordValidationResult: FormFieldValidationResult? =
                manager.getFieldItem<String>(confirmPasswordId).fieldState.value.validationResult

            assertTrue(confirmPasswordValidationResult is FormFieldValidationResult.Invalid)

            manager.onFieldValueChanged(passwordId, "Password123")
            advanceTimeBy(1.seconds)

            val newConfirmPasswordValidationResult: FormFieldValidationResult? =
                manager.getFieldItem<String>(confirmPasswordId).fieldState.value.validationResult

            assertThat(newConfirmPasswordValidationResult)
                .isNull()
        }

    @Test
    fun `AutoOnFocusClear strategy - If the connected field has a value triggers auto-validation on the connected field when the primary field loses focus`() =
        runTest {
            val passwordId = "password"
            val confirmPasswordId = "confirmPassword"
            val passwordField = FormField(
                id = passwordId,
                validators = listOf(mockValidator)
            )

            coEvery { mockValidator.validate(any()) } returns FormFieldValidationResult.Invalid.Unknown

            val equalsToValidator = EqualsToValidator<String>(connectedFieldId = passwordId)
            val confirmField = FormField(
                id = confirmPasswordId,
                validators = listOf(equalsToValidator)
            )

            val manager = CarlosFormManager(
                formFields = listOf(passwordField, confirmField),
                validationStrategy = FormFieldValidationStrategy.AutoOnFocusClear
            )

            manager.initFormManager()
            advanceTimeBy(1.seconds)
            manager.onFieldVisibilityChanged(passwordId, true)
            manager.onFieldVisibilityChanged(confirmPasswordId, true)

            val passwordInput = "Password value"
            manager.onFieldValueChanged(passwordId, passwordInput)
            manager.onFieldFocusCleared(passwordId)
            advanceTimeBy(1.seconds)

            val confirmPasswordInput = "Password val"
            manager.onFieldValueChanged(confirmPasswordId, confirmPasswordInput)
            advanceTimeBy(1.seconds)

            coVerify {
                mockValidator.validate(passwordInput)
                equalsToValidator.validate(confirmPasswordId, manager)
            }
        }

    @Test
    fun `Auto inline strategy - If the connected field has a value runs auto-validation on the connected field when the primary fields value changes`() =
        runTest {
            val passwordId = "password"
            val confirmPasswordId = "confirmPassword"

            val passwordField = FormField(
                id = passwordId,
                initialValue = "Password123"
            )

            val equalsToValidator = EqualsToValidator<String>(connectedFieldId = passwordId)
            val confirmField = FormField(
                id = confirmPasswordId,
                initialValue = "Password123*",
                validators = listOf(equalsToValidator)
            )

            val manager = CarlosFormManager(
                formFields = listOf(passwordField, confirmField),
                validationStrategy = FormFieldValidationStrategy.AutoInline()
            )

            manager.initFormManager()
            advanceTimeBy(1.seconds)

            manager.onFieldVisibilityChanged(passwordId, true)
            manager.onFieldVisibilityChanged(confirmPasswordId, true)
            manager.validateForm()

            val confirmPasswordValidationResult: FormFieldValidationResult? =
                manager.getFieldItem<String>(confirmPasswordId).fieldState.value.validationResult

            assertTrue(confirmPasswordValidationResult is FormFieldValidationResult.Invalid)

            manager.onFieldValueChanged(passwordId, "Password123*")
            advanceTimeBy(1.seconds)

            val newConfirmPasswordValidationResult: FormFieldValidationResult? =
                manager.getFieldItem<String>(confirmPasswordId).fieldState.value.validationResult

            assertTrue(newConfirmPasswordValidationResult is FormFieldValidationResult.Valid)
        }

    @Test
    fun `AutoInline strategy - runs validations for multiple fields in parallel`() = runTest {
        val field1Key = "field1"
        val field2Key = "field2"

        val field1 = FormField(
            id = field1Key,
            initialValue = "value1",
            validators = listOf(mockValidator)
        )

        val field2 = FormField(
            id = field2Key,
            initialValue = "value2",
            validators = listOf(mockValidator)
        )

        coEvery { mockValidator.validate(any()) } returns FormFieldValidationResult.Valid

        val manager = CarlosFormManager(
            formFields = listOf(field1, field2),
            validationStrategy = FormFieldValidationStrategy.AutoInline(delay = 200.milliseconds)
        )

        manager.initFormManager()
        advanceTimeBy(1.seconds)

        manager.onFieldVisibilityChanged(field1Key, true)
        manager.onFieldVisibilityChanged(field2Key, true)

        // Trigger both fields to validate
        manager.onFieldValueChanged(field1Key, "new1")
        manager.onFieldValueChanged(field2Key, "new2")

        advanceTimeBy(500)

        val field1Result = manager.getFieldItem<String>(field1Key).fieldState.value.validationResult
        val field2Result = manager.getFieldItem<String>(field2Key).fieldState.value.validationResult

        assertThat(field1Result).isInstanceOf(FormFieldValidationResult.Valid::class.java)
        assertThat(field2Result).isInstanceOf(FormFieldValidationResult.Valid::class.java)

        coVerify(exactly = 1) { mockValidator.validate("new1") }
        coVerify(exactly = 1) { mockValidator.validate("new2") }
    }

    @Test
    fun `AutoInline strategy - long validation on one field does not block other fields`() = runTest {
        val validationStarted = CompletableDeferred<Unit>()

        val slowValidator = object : FormFieldValidator<String> {
            override suspend fun validate(value: String?): FormFieldValidationResult {
                validationStarted.complete(Unit)
                awaitCancellation() // never finishes
            }
        }

        val field1Key = "field1"
        val field2Key = "field2"

        val field1 = FormField(
            id = field1Key,
            validators = listOf(slowValidator)
        )

        val field2 = FormField(
            id = field2Key,
            validators = listOf(mockValidator)
        )

        coEvery { mockValidator.validate(any()) } returns FormFieldValidationResult.Valid

        val manager = CarlosFormManager(
            formFields = listOf(field1, field2),
            validationStrategy = FormFieldValidationStrategy.AutoInline(delay = 200.milliseconds)
        )

        manager.initFormManager()
        advanceTimeBy(1.seconds)

        manager.onFieldVisibilityChanged(field1Key, true)
        manager.onFieldVisibilityChanged(field2Key, true)

        // Start long-running validation on field1
        manager.onFieldValueChanged(field1Key, "slow-value")
        validationStarted.await()

        // Trigger validation on field2
        manager.onFieldValueChanged(field2Key, "fast-value")
        advanceTimeBy(500)

        val field2Result = manager.getFieldItem<String>(field2Key).fieldState.value.validationResult
        assertThat(field2Result).isInstanceOf(FormFieldValidationResult.Valid::class.java)

        coVerify(exactly = 1) { mockValidator.validate("fast-value") }
    }

    @Test
    fun `AutoInline strategy - overlapping slow and fast validations, both results are preserved`() = runTest {
        val slowValidationStarted = CompletableDeferred<Unit>()
        val slowValidationFinished = CompletableDeferred<Unit>()

        val slowValidator = object : FormFieldValidator<String> {
            override suspend fun validate(value: String?): FormFieldValidationResult {
                slowValidationStarted.complete(Unit)
                // Simulate long-running work
                advanceTimeBy(1.seconds)
                slowValidationFinished.complete(Unit)
                return FormFieldValidationResult.Valid
            }
        }

        val fastValidator = object : FormFieldValidator<String> {
            override suspend fun validate(value: String?): FormFieldValidationResult {
                return FormFieldValidationResult.Invalid.Unknown
            }
        }

        val field1Key = "field1"
        val field2Key = "field2"

        val field1 = FormField(
            id = field1Key,
            validators = listOf(slowValidator)
        )

        val field2 = FormField(
            id = field2Key,
            validators = listOf(fastValidator)
        )

        val manager = CarlosFormManager(
            formFields = listOf(field1, field2),
            validationStrategy = FormFieldValidationStrategy.AutoInline(delay = 100.milliseconds)
        )

        manager.initFormManager()
        advanceTimeBy(1.seconds)

        manager.onFieldVisibilityChanged(field1Key, true)
        manager.onFieldVisibilityChanged(field2Key, true)

        // Trigger both validations close to each other
        manager.onFieldValueChanged(field1Key, "slow")
        slowValidationStarted.await()

        manager.onFieldValueChanged(field2Key, "fast")

        // Advance enough for fast validation to complete but not slow one
        advanceTimeBy(200)

        val field1State = manager.getFieldItem<String>(field1Key).fieldState
        val field2State = manager.getFieldItem<String>(field2Key).fieldState

        val field1Result = field1State.value.validationResult
        val field2Result = field2State.value.validationResult

        assertThat(field1Result).isNull() // slow still running
        assertThat(field2Result).isInstanceOf(FormFieldValidationResult.Invalid::class.java)

        // Now let the slow validation finish
        slowValidationFinished.await()

        val finalField1Result = field1State.value.validationResult
        val finalField2Result = field2State.value.validationResult

        assertThat(finalField1Result).isInstanceOf(FormFieldValidationResult.Valid::class.java)
        assertThat(finalField2Result).isInstanceOf(FormFieldValidationResult.Invalid::class.java)
    }

    @Test
    fun `Required field affects allRequiredFieldFilled`() = runTest {
        val requiredField = FormField<String>(
            id = TEST_FORM_FIELD_ID,
            validators = listOf(ValueRequiredValidator())
        )

        val manager = CarlosFormManager(
            formFields = listOf(requiredField)
        )

        manager.initFormManager()
        advanceTimeBy(1.seconds)

        manager.onFieldVisibilityChanged(TEST_FORM_FIELD_ID, true)
        advanceTimeBy(1.seconds)

        assertThat(manager.allRequiredFieldFilled.value)
            .isFalse()

        manager.onFieldValueChanged(TEST_FORM_FIELD_ID, "Some value")
        advanceTimeBy(1.seconds)

        assertThat(manager.allRequiredFieldFilled.value)
            .isTrue()
    }

    @Test
    fun `Invisible required fields are ignored`() = runTest {
        val requiredField = FormField<String>(
            id = TEST_FORM_FIELD_ID,
            validators = listOf(ValueRequiredValidator())
        )
        val manager = CarlosFormManager(
            formFields = listOf(requiredField)
        )
        manager.initFormManager()
        advanceTimeBy(1.seconds)

        // Field is invisible
        manager.onFieldVisibilityChanged(TEST_FORM_FIELD_ID, visible = false)
        advanceTimeBy(1.seconds)

        assertThat(manager.allRequiredFieldFilled.value)
            .isTrue()
    }

    @Test
    fun `Clear form resets all fields`() = runTest {
        val field = FormField(
            id = TEST_FORM_FIELD_ID,
            initialValue = "initial"
        )
        val manager = CarlosFormManager(listOf(field))
        manager.initFormManager()
        advanceTimeBy(1.seconds)

        manager.onFieldValueChanged(TEST_FORM_FIELD_ID, "changed")
        manager.clearForm()
        advanceTimeBy(1.seconds)

        val fieldValue: String? =
            manager.getFieldItem<String>(TEST_FORM_FIELD_ID).fieldState.value.value

        assertThat(fieldValue)
            .isEqualTo("initial")
    }

    @Test
    fun `SetFormInvalid marks all fields invalid`() = runTest {
        val field1 = FormField<String>(id = "field1")
        val field2 = FormField<String>(id = "field2")

        val manager = CarlosFormManager(formFields = listOf(field1, field2))
        manager.initFormManager()
        advanceTimeBy(1.seconds)

        manager.setFormInvalid()

        val field1ValidationResult: FormFieldValidationResult? =
            manager.getFieldItem<String>("field1").fieldState.value.validationResult

        assertTrue(field1ValidationResult is FormFieldValidationResult.Invalid)

        val field2ValidationResult: FormFieldValidationResult? =
            manager.getFieldItem<String>("field2").fieldState.value.validationResult

        assertThat(field2ValidationResult is FormFieldValidationResult.Invalid)
    }

    @Test
    fun `Verify that long-running validation is cancelled when caller scope is cancelled`() = runTest {
        val validationStarted = CompletableDeferred<Boolean>()
        val validationCancelled = CompletableDeferred<Boolean>()

        val testField = FormField(
            id = TEST_FORM_FIELD_ID,
            validators = listOf(
                object : FormFieldValidator<String> {
                    override suspend fun validate(value: String?): FormFieldValidationResult {
                        validationStarted.complete(true)
                        println("Validation started!")
                        try {
                            awaitCancellation()
                        } catch (e: CancellationException) {
                            validationCancelled.complete(true)
                            println("Validation was cancelled!")
                            throw e
                        }
                    }
                }
            )
        )

        val manager = CarlosFormManager(
            formFields = listOf(testField),
            validationStrategy = FormFieldValidationStrategy.AutoInline()
        )

        val callerScope = CoroutineScope(EmptyCoroutineContext + Job())
        callerScope.launch {
            manager.initFormManager()

            // Ensure field is visible for validation
            manager.onFieldVisibilityChanged(TEST_FORM_FIELD_ID, true)
            manager.onFieldValueChanged(TEST_FORM_FIELD_ID, "New value")
        }

        validationStarted.await()
        callerScope.cancel()
        assertThat(validationCancelled.await()).isTrue()
    }

    @Throws(IllegalStateException::class)
    fun `Throws error if used before initialization`() {
        val manager = CarlosFormManager(emptyList())
        manager.onFieldValueChanged(TEST_FORM_FIELD_ID, "")
    }

    companion object {
        const val TEST_FORM_FIELD_ID = "TEST_FORM_FIELD_ID"
    }
}
