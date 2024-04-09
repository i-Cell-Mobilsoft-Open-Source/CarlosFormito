package com.icell.external.carlosformito.demo.ui.email

import com.icell.external.carlosformito.core.api.model.FormField
import com.icell.external.carlosformito.core.validator.TextRegexValidator
import com.icell.external.carlosformito.core.validator.ValueRequiredValidator
import com.icell.external.carlosformito.core.validator.regex.Regexp
import com.icell.external.carlosformito.demo.R
import com.icell.external.carlosformito.demo.ui.email.validator.FakeEmailUniqueValidator

object ChangeEmailFields {

    const val KEY_NEW_EMAIL = "KEY_NEW_EMAIL"

    fun build(): List<FormField<*>> {
        return listOf(
            FormField(
                id = KEY_NEW_EMAIL,
                validators = listOf(
                    ValueRequiredValidator(R.string.value_required_error),
                    TextRegexValidator(Regexp.EMAIL_REGEXP, R.string.invalid_format_error),
                    FakeEmailUniqueValidator(fakeNetworkError = false)
                )
            )
        )
    }
}
