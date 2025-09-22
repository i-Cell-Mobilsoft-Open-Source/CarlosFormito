package hu.icellmobilsoft.carlosformito.demo.ui.email

import hu.icellmobilsoft.carlosformito.core.api.model.FormField
import hu.icellmobilsoft.carlosformito.core.validator.TextRegexValidator
import hu.icellmobilsoft.carlosformito.core.validator.ValueRequiredValidator
import hu.icellmobilsoft.carlosformito.core.validator.regex.Regexp
import hu.icellmobilsoft.carlosformito.demo.R
import hu.icellmobilsoft.carlosformito.demo.ui.email.validator.FakeEmailAvailableValidator

object ChangeEmailFields {

    const val KEY_NEW_EMAIL = "KEY_NEW_EMAIL"

    fun build(): List<FormField<*>> {
        return listOf(
            FormField(
                id = KEY_NEW_EMAIL,
                validators = listOf(
                    ValueRequiredValidator(R.string.value_required_error),
                    TextRegexValidator(Regexp.EMAIL_REGEXP, R.string.invalid_format_error),
                    FakeEmailAvailableValidator(fakeNetworkError = false)
                )
            )
        )
    }
}
