package hu.icellmobilsoft.carlosformito.demo.ui.phonenumber

import hu.icellmobilsoft.carlosformito.core.api.model.FormField
import hu.icellmobilsoft.carlosformito.core.validator.ValueRequiredValidator
import hu.icellmobilsoft.carlosformito.demo.R
import hu.icellmobilsoft.carlosformito.demo.ui.phonenumber.model.Country
import hu.icellmobilsoft.carlosformito.demo.ui.phonenumber.validator.InternationalPhoneNumberValidator

object SetPhoneNumberFields {

    const val PHONE_NUMBER_MAX_LENGTH = 24
    const val KEY_COUNTRY_CODE = "KEY_COUNTRY_CODE"
    const val KEY_LOCAL_NUMBER = "KEY_LOCAL_NUMBER"

    fun build(): List<FormField<*>> {
        return listOf(
            FormField(
                id = KEY_COUNTRY_CODE,
                initialValue = Country.TST,
                validators = listOf(
                    ValueRequiredValidator(R.string.value_required_error)
                )
            ),
            FormField<String>(
                id = KEY_LOCAL_NUMBER,
                validators = listOf(
                    ValueRequiredValidator(R.string.value_required_error),
                    InternationalPhoneNumberValidator(KEY_COUNTRY_CODE, R.string.invalid_phone_number_error)
                )
            )
        )
    }
}
