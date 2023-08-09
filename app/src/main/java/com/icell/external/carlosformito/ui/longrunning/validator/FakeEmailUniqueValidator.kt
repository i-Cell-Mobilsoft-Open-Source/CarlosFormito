package com.icell.external.carlosformito.ui.longrunning.validator

import com.icell.external.carlosformito.R
import com.icell.external.carlosformito.core.api.validator.FormFieldValidationResult
import com.icell.external.carlosformito.core.api.validator.FormFieldValidator
import kotlinx.coroutines.delay
import java.io.IOException

@Suppress("MagicNumber")
class FakeEmailUniqueValidator(private val fakeNetworkError: Boolean = false) :
    FormFieldValidator<String> {

    // Fake invalid inputs
    private val notUniqueInputs = listOf(
        "test123@gmail.com",
        "user123@gmail.com",
        "test.user@gmail.com",
        "user.test@gmail.com"
    )

    override suspend fun validate(value: String?): FormFieldValidationResult {
        delay(2000) // Fake network request delay

        if (fakeNetworkError) {
            throw IOException("No internet connection")
        }

        return if (notUniqueInputs.contains(value.orEmpty())) {
            FormFieldValidationResult.Invalid.Message(R.string.email_not_unique_error)
        } else {
            FormFieldValidationResult.Valid
        }
    }
}
