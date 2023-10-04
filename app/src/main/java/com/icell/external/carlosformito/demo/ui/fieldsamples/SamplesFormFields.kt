package com.icell.external.carlosformito.demo.ui.fieldsamples

import com.icell.external.carlosformito.core.api.model.FormField
import com.icell.external.carlosformito.core.validator.DateMinMaxValidator
import com.icell.external.carlosformito.core.validator.IntegerMinMaxValidator
import com.icell.external.carlosformito.core.validator.TextMinLengthValidator
import com.icell.external.carlosformito.core.validator.TimeMinMaxValidator
import com.icell.external.carlosformito.core.validator.ValueRequiredValidator
import com.icell.external.carlosformito.demo.ui.fieldsamples.model.PackageType
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
                    ValueRequiredValidator(),
                    TextMinLengthValidator(minLength = 3)
                )
            ),
            FormField(
                id = KEY_FORM_FIELD_SECRET,
                validators = listOf(
                    TextMinLengthValidator(minLength = 8)
                )
            ),
            FormField(
                id = KEY_FORM_FIELD_DATE,
                validators = listOf(
                    ValueRequiredValidator(),
                    DateMinMaxValidator(
                        minValue = LocalDate.now(),
                        maxValue = LocalDate.now().plusWeeks(1)
                    )
                )
            ),
            FormField(
                id = KEY_FORM_FIELD_TIME,
                validators = listOf(
                    ValueRequiredValidator(),
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
