package hu.icellmobilsoft.carlosformito.demo.ui.fieldsamples.validator

import hu.icellmobilsoft.carlosformito.core.api.validator.FormFieldValidationResult
import hu.icellmobilsoft.carlosformito.core.api.validator.FormFieldValidator
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

class UsernameAvailabilityValidator(
    private val errorMessageId: Int
) : FormFieldValidator<String> {

    private val takenUsernames = listOf(
        "username",
        "demo",
        "testuser",
        "guest",
        "john_doe",
        "admin123",
        "sample_user",
        "user2025"
    )

    override suspend fun validate(value: String?): FormFieldValidationResult {
        val username = value ?: return FormFieldValidationResult.Valid

        // Faking network request delay
        delay(1.seconds)

        if (username in takenUsernames) {
            return FormFieldValidationResult.Invalid.of(errorMessageId)
        }

        return FormFieldValidationResult.Valid
    }
}
