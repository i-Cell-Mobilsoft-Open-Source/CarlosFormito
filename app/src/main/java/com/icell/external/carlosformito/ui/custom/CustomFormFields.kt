package com.icell.external.carlosformito.ui.custom

import com.icell.external.carlosformito.core.api.model.FormField
import com.icell.external.carlosformito.core.api.model.FormFieldState
import com.icell.external.carlosformito.ui.custom.fields.model.PaymentMethod
import com.icell.external.carlosformito.ui.custom.fields.validator.ValidityStartValidator
import com.icell.external.carlosformito.ui.validator.ValueRequiredValidator
import java.time.ZonedDateTime

object CustomFormFields {

    const val KEY_VALIDITY_START = "KEY_FORM_FIELD_VALIDITY_START"
    const val KEY_QUANTITY = "KEY_QUANTITY"
    const val KEY_PAYMENT_METHOD_TYPE = "KEY_PAYMENT_METHOD_TYPE"
    const val KEY_SAVE_PAYMENT_METHOD_CHECKED = "KEY_CHECKED"

    fun build(): List<FormField<*>> {
        return listOf(
            FormField(
                id = KEY_VALIDITY_START,
                validators = listOf(
                    ValueRequiredValidator(),
                    ValidityStartValidator(
                        minDateTime = ZonedDateTime.now(),
                        maxDateTime = ZonedDateTime.now().plusWeeks(1)
                    )
                )
            ),
            FormField(
                id = KEY_QUANTITY,
                initialState = FormFieldState(1),
                validators = emptyList()
            ),
            FormField(
                id = KEY_PAYMENT_METHOD_TYPE,
                initialState = FormFieldState(PaymentMethod.DebitCard),
                validators = emptyList()
            ),
            FormField(
                id = KEY_SAVE_PAYMENT_METHOD_CHECKED,
                initialState = FormFieldState(false),
                validators = emptyList()
            )
        )
    }
}
