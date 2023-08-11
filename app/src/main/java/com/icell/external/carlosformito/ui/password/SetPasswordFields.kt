package com.icell.external.carlosformito.ui.password

import com.icell.external.carlosformito.R
import com.icell.external.carlosformito.core.api.model.FormField
import com.icell.external.carlosformito.ui.validator.ContainsNumberValidator
import com.icell.external.carlosformito.ui.validator.ContainsSpecialCharacterValidator
import com.icell.external.carlosformito.ui.validator.ContainsUpperAndLowercaseValidator
import com.icell.external.carlosformito.ui.validator.TextMinLengthValidator
import com.icell.external.carlosformito.ui.validator.ValueRequiredValidator

object SetPasswordFields {

    private const val KEY_PASSWORD_MIN_LENGTH = 8
    const val KEY_PASSWORD_MAX_LENGTH = 32

    const val KEY_PASSWORD = "KEY_PASSWORD"
    const val KEY_CONFIRM_PASSWORD = "KEY_CONFIRM_PASSWORD"

    fun build(): List<FormField<*>> {
        return listOf(
            FormField(
                id = KEY_PASSWORD,
                validators = listOf(
                    TextMinLengthValidator(minLength = KEY_PASSWORD_MIN_LENGTH),
                    ContainsNumberValidator(R.string.password_not_contains_number_error),
                    ContainsSpecialCharacterValidator(R.string.password_not_contains_special_char_error),
                    ContainsUpperAndLowercaseValidator(R.string.password_not_contains_uppercase_or_lowercase_error)
                )
            ),
            FormField(
                id = KEY_CONFIRM_PASSWORD,
                validators = listOf(
                    ValueRequiredValidator()
                )
            )
        )
    }
}
