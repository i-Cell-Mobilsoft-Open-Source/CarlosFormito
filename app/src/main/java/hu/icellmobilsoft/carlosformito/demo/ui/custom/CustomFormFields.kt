package hu.icellmobilsoft.carlosformito.demo.ui.custom

import hu.icellmobilsoft.carlosformito.commondemo.now
import hu.icellmobilsoft.carlosformito.commondemo.plus
import hu.icellmobilsoft.carlosformito.core.api.model.FormField
import hu.icellmobilsoft.carlosformito.core.validator.ValueRequiredValidator
import hu.icellmobilsoft.carlosformito.demo.R
import hu.icellmobilsoft.carlosformito.demo.ui.custom.fields.model.PaymentMethod
import hu.icellmobilsoft.carlosformito.demo.ui.custom.fields.validator.ValidityStartValidator
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDateTime

object CustomFormFields {

    const val KEY_VALIDITY_START = "KEY_FORM_FIELD_VALIDITY_START"
    const val KEY_QUANTITY = "KEY_QUANTITY"
    const val KEY_PAYMENT_METHOD_TYPE = "KEY_PAYMENT_METHOD_TYPE"
    const val KEY_DEBIT_CARD_NUMBER = "KEY_DEBIT_CARD_NUMBER"
    const val KEY_SAVE_PAYMENT_METHOD_CHECKED = "KEY_CHECKED"

    fun build(): List<FormField<*>> {
        return listOf(
            FormField(
                id = KEY_QUANTITY,
                initialValue = 1,
                validators = emptyList()
            ),
            FormField(
                id = KEY_VALIDITY_START,
                validators = listOf(
                    ValueRequiredValidator(R.string.value_required_error),
                    ValidityStartValidator(
                        minDateTime = LocalDateTime.now(),
                        maxDateTime = LocalDateTime.now().plus(1, DateTimeUnit.WEEK)
                    )
                )
            ),
            FormField(
                id = KEY_PAYMENT_METHOD_TYPE,
                initialValue = PaymentMethod.Balance,
                validators = emptyList()
            ),
            FormField(
                id = KEY_DEBIT_CARD_NUMBER,
                validators = listOf(
                    ValueRequiredValidator(R.string.value_required_error)
                )
            ),
            FormField(
                id = KEY_SAVE_PAYMENT_METHOD_CHECKED,
                initialValue = false,
                validators = emptyList()
            )
        )
    }
}
