package com.icell.external.carlosformito.demo.ui.email.validator

import com.icell.external.carlosformito.core.api.validator.FormFieldValidationResult
import com.icell.external.carlosformito.core.api.validator.FormFieldValidator
import com.icell.external.carlosformito.demo.R
import kotlinx.coroutines.delay
import java.io.IOException
import kotlin.time.Duration.Companion.seconds

class FakeEmailAvailableValidator(
    private val fakeNetworkError: Boolean = false
) : FormFieldValidator<String> {

    // Mocked unavailable emails for demo purposes
    private val unavailableEmails = listOf(
        "user123@gmail.com",
        "test.user@gmail.com",
        "user.test@gmail.com"
    )

    override suspend fun validate(value: String?): FormFieldValidationResult {
        delay(2.seconds) // Fake network request delay

        if (fakeNetworkError) {
            throw IOException("No internet connection")
        }

        return if (unavailableEmails.contains(value.orEmpty())) {
            FormFieldValidationResult.Invalid.Message(R.string.email_not_unique_error)
        } else {
            FormFieldValidationResult.Valid
        }
    }
}
