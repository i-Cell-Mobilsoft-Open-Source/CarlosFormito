package com.icell.external.carlosformito.demo.ui.fieldsamples

import com.icell.external.carlosformito.core.api.model.FormField
import com.icell.external.carlosformito.core.validator.ContainsNumberValidator
import com.icell.external.carlosformito.core.validator.ContainsSpecialCharacterValidator
import com.icell.external.carlosformito.core.validator.ContainsUpperAndLowercaseValidator
import com.icell.external.carlosformito.core.validator.DateMinMaxValidator
import com.icell.external.carlosformito.core.validator.IntegerMinMaxValidator
import com.icell.external.carlosformito.core.validator.TextMinLengthValidator
import com.icell.external.carlosformito.core.validator.TimeMinMaxValidator
import com.icell.external.carlosformito.core.validator.ValueRequiredValidator
import com.icell.external.carlosformito.demo.R
import com.icell.external.carlosformito.demo.ui.fieldsamples.model.PackageType
import com.icell.external.carlosformito.demo.ui.fieldsamples.validator.UsernameAvailabilityValidator
import java.time.LocalDate
import java.time.LocalTime

@Suppress("MagicNumber")
object SamplesFormFields {

    const val KEY_FORM_FIELD_USERNAME = "KEY_FORM_FIELD_USERNAME"
    const val KEY_FORM_FIELD_DATE = "KEY_FORM_FIELD_DATE"
    const val KEY_FORM_FIELD_TIME = "KEY_FORM_FIELD_TIME"
    const val KEY_FORM_FIELD_SIZE = "KEY_FORM_FIELD_SIZE"
    const val KEY_FORM_FIELD_SECRET = "KEY_FORM_FIELD_SECRET"
    const val KEY_FORM_FIELD_PACKAGE = "KEY_FORM_FIELD_PACKAGE"

    fun build(): List<FormField<*>> {
        return listOf(
            FormField(
                id = KEY_FORM_FIELD_USERNAME,
                validators = listOf(
                    ValueRequiredValidator(R.string.value_required_error),
                    TextMinLengthValidator(minLength = 3, R.string.min_length_error),
                    UsernameAvailabilityValidator(errorMessageId = R.string.username_is_already_taken_error)
                ),
                initialValue = "guest"
            ),
            FormField(
                id = KEY_FORM_FIELD_SECRET,
                validators = listOf(
                    ValueRequiredValidator(R.string.value_required_error),
                    ContainsSpecialCharacterValidator(R.string.password_not_contains_special_char_error),
                    ContainsNumberValidator(R.string.password_not_contains_number_error),
                    ContainsUpperAndLowercaseValidator(R.string.password_not_contains_uppercase_or_lowercase_error),
                    TextMinLengthValidator(minLength = 8, R.string.min_length_error)
                )
            ),
            FormField(
                id = KEY_FORM_FIELD_DATE,
                validators = listOf(
                    ValueRequiredValidator(R.string.value_required_error),
                    DateMinMaxValidator(
                        minValue = LocalDate.now(),
                        maxValue = LocalDate.now().plusWeeks(1),
                        R.string.date_min_max_error
                    )
                )
            ),
            FormField(
                id = KEY_FORM_FIELD_TIME,
                validators = listOf(
                    ValueRequiredValidator(R.string.value_required_error),
                    TimeMinMaxValidator(
                        minValue = LocalTime.NOON,
                        maxValue = LocalTime.of(18, 0)
                    )
                )
            ),
            FormField(
                id = KEY_FORM_FIELD_SIZE,
                validators = listOf(
                    IntegerMinMaxValidator(
                        minValue = 100,
                        maxValue = 200
                    ),
                    ValueRequiredValidator()
                )
            ),
            FormField<PackageType>(
                id = KEY_FORM_FIELD_PACKAGE,
                validators = listOf(
                    ValueRequiredValidator()
                )
            )
        )
    }
}
