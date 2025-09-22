package hu.icellmobilsoft.carlosformito.material3demo.ui.fieldsamples

import hu.icellmobilsoft.carlosformito.core.api.model.FormField
import hu.icellmobilsoft.carlosformito.core.validator.DateMinMaxValidator
import hu.icellmobilsoft.carlosformito.core.validator.IntegerMinMaxValidator
import hu.icellmobilsoft.carlosformito.core.validator.TextMinLengthValidator
import hu.icellmobilsoft.carlosformito.core.validator.TimeMinMaxValidator
import hu.icellmobilsoft.carlosformito.core.validator.ValueRequiredValidator
import hu.icellmobilsoft.carlosformito.material3demo.R
import hu.icellmobilsoft.carlosformito.material3demo.ui.fieldsamples.model.PackageType
import java.time.LocalDate
import java.time.LocalTime

@Suppress("MagicNumber")
object SamplesFormFields {

    const val KEY_FORM_FIELD_NAME = "KEY_FORM_FIELD_NAME"
    const val KEY_FORM_FIELD_DATE = "KEY_FORM_FIELD_DATE"
    const val KEY_FORM_FIELD_TIME = "KEY_FORM_FIELD_TIME"
    const val KEY_FORM_FIELD_SIZE = "KEY_FORM_FIELD_SIZE"
    const val KEY_FORM_FIELD_SECRET = "KEY_FORM_FIELD_SECRET"
    const val KEY_FORM_FIELD_PACKAGE = "KEY_FORM_FIELD_PACKAGE"

    fun build(): List<FormField<*>> {
        return listOf(
            FormField(
                id = KEY_FORM_FIELD_NAME,
                validators = listOf(
                    ValueRequiredValidator(R.string.value_required_error),
                    TextMinLengthValidator(minLength = 3, R.string.min_length_error)
                )
            ),
            FormField(
                id = KEY_FORM_FIELD_SECRET,
                validators = listOf(
                    ValueRequiredValidator(R.string.value_required_error),
                    TextMinLengthValidator(minLength = 8)
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
                    )
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
