package com.icell.external.carlosformito.demo.ui.phonenumber.validator

import androidx.annotation.StringRes
import com.icell.external.carlosformito.core.api.requireFieldValue
import com.icell.external.carlosformito.core.api.validator.FormFieldValidationResult
import com.icell.external.carlosformito.core.api.validator.FormValueContext
import com.icell.external.carlosformito.core.validator.connections.ConnectionValidator
import com.icell.external.carlosformito.demo.ui.phonenumber.model.Country

class InternationalPhoneNumberValidator(
    override val connectedFieldId: String,
    @StringRes private val errorMessageId: Int? = null
) : ConnectionValidator<String>() {

    override suspend fun validate(value: String?, context: FormValueContext): FormFieldValidationResult {
        val nonNullValue = value ?: ""

        val country = context.requireFieldValue<Country>(connectedFieldId)
        val phoneNumber = country.callCode + nonNullValue

        // Ignoring validation for test phone numbers
        if (country == Country.TST) {
            return FormFieldValidationResult.Valid
        }

        // Dummy phone number validation just for demo purposes
        if (phoneNumber.length != REQUIRED_INTERNATIONAL_PHONE_NUMBER_LENGTH) {
            return FormFieldValidationResult.Invalid.of(errorMessageId)
        }

        return FormFieldValidationResult.Valid
    }

    companion object {
        const val REQUIRED_INTERNATIONAL_PHONE_NUMBER_LENGTH = 12
    }
}
