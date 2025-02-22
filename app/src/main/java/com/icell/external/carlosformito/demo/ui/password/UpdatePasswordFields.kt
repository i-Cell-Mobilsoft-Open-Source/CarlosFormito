package com.icell.external.carlosformito.demo.ui.password

import com.icell.external.carlosformito.core.api.model.FormField
import com.icell.external.carlosformito.core.validator.ContainsNumberValidator
import com.icell.external.carlosformito.core.validator.ContainsSpecialCharacterValidator
import com.icell.external.carlosformito.core.validator.ContainsUpperAndLowercaseValidator
import com.icell.external.carlosformito.core.validator.TextMinLengthValidator
import com.icell.external.carlosformito.core.validator.ValueRequiredValidator
import com.icell.external.carlosformito.core.validator.connections.EqualsToValidator
import com.icell.external.carlosformito.core.validator.connections.NotEqualsToValidator
import com.icell.external.carlosformito.demo.R

object UpdatePasswordFields {

    private const val KEY_PASSWORD_MIN_LENGTH = 8
    const val KEY_PASSWORD_MAX_LENGTH = 32

    const val KEY_OLD_PASSWORD = "KEY_OLD_PASSWORD"
    const val KEY_NEW_PASSWORD = "KEY_NEW_PASSWORD"
    const val KEY_CONFIRM_PASSWORD = "KEY_CONFIRM_PASSWORD"

    fun build(): List<FormField<*>> {
        return listOf(
            FormField(
                id = KEY_OLD_PASSWORD,
                validators = listOf(
                    ValueRequiredValidator(R.string.value_required_error)
                )
            ),
            FormField(
                id = KEY_NEW_PASSWORD,
                validators = listOf(
                    ValueRequiredValidator(R.string.value_required_error),
                    TextMinLengthValidator(minLength = KEY_PASSWORD_MIN_LENGTH, R.string.min_length_error),
                    ContainsNumberValidator(R.string.password_not_contains_number_error),
                    ContainsSpecialCharacterValidator(R.string.password_not_contains_special_char_error),
                    ContainsUpperAndLowercaseValidator(R.string.password_not_contains_uppercase_or_lowercase_error),
                    NotEqualsToValidator(KEY_OLD_PASSWORD, R.string.new_password_equals_old_password_error)
                )
            ),
            FormField(
                id = KEY_CONFIRM_PASSWORD,
                validators = listOf(
                    ValueRequiredValidator(R.string.value_required_error),
                    EqualsToValidator(KEY_NEW_PASSWORD, R.string.confirm_password_not_match_error)
                )
            )
        )
    }
}
