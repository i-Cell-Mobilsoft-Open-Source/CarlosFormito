package com.icell.external.carlosformito.ui.email

import com.icell.external.carlosformito.api.model.FormField
import com.icell.external.carlosformito.ui.email.validator.FakeEmailUniqueValidator
import com.icell.external.carlosformito.ui.validator.TextRegexValidator
import com.icell.external.carlosformito.ui.validator.ValueRequiredValidator

object ChangeEmailFields {

    const val KEY_NEW_EMAIL = "KEY_NEW_EMAIL"
    private const val EMAIL_REGEXP =
        "[A-Za-z0-9_%+-][A-Za-z0-9._%+-]*@([A-Za-z0-9_-]+\\.)+[A-Za-z]{2,4}"

    fun build(): List<FormField<*>> {
        return listOf(
            FormField(
                id = KEY_NEW_EMAIL,
                validators = listOf(
                    ValueRequiredValidator(),
                    TextRegexValidator(EMAIL_REGEXP),
                    FakeEmailUniqueValidator(fakeNetworkError = false)
                )
            )
        )
    }
}
