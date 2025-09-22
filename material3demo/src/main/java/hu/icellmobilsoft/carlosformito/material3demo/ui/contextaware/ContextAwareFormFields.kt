package hu.icellmobilsoft.carlosformito.material3demo.ui.contextaware

import hu.icellmobilsoft.carlosformito.core.api.model.FormField
import hu.icellmobilsoft.carlosformito.core.api.validator.FormFieldValidationResult
import hu.icellmobilsoft.carlosformito.core.api.validator.FormValueContext
import hu.icellmobilsoft.carlosformito.core.validator.connections.MultiConnectionValidator
import hu.icellmobilsoft.carlosformito.material3demo.R
import hu.icellmobilsoft.carlosformito.material3demo.ui.contextaware.model.AppleType
import hu.icellmobilsoft.carlosformito.material3demo.ui.contextaware.model.SizeType

object ContextAwareFormFields {

    const val KEY_APPLE_TYPE = "KEY_APPLE_TYPE"
    const val KEY_SIZE_TYPE = "KEY_SIZE_TYPE"
    const val KEY_QUANTITY = "KEY_QUANTITY"

    val APPLE_TYPE_DEFAULT = AppleType.Jonagold
    val SIZE_TYPE_DEFAULT = SizeType.Medium

    fun build(): List<FormField<*>> {
        return listOf(
            FormField<AppleType>(
                id = KEY_APPLE_TYPE,
                initialValue = APPLE_TYPE_DEFAULT
            ),
            FormField<SizeType>(
                id = KEY_SIZE_TYPE,
                initialValue = SIZE_TYPE_DEFAULT
            ),
            FormField<Int>(
                id = KEY_QUANTITY,
                validators = listOf(
                    AppleQuantityValidator()
                )
            ),
        )
    }
}

class AppleQuantityValidator : MultiConnectionValidator<Int>() {

    override val connectedFieldIds: List<String> = listOf(
        ContextAwareFormFields.KEY_APPLE_TYPE,
        ContextAwareFormFields.KEY_SIZE_TYPE
    )

    @Suppress("MagicNumber")
    override suspend fun validate(
        value: Int?,
        context: FormValueContext
    ): FormFieldValidationResult {
        val quantity = value ?: return FormFieldValidationResult.Valid

        val appleType: AppleType = context.getFieldValue(ContextAwareFormFields.KEY_APPLE_TYPE)
            ?: ContextAwareFormFields.APPLE_TYPE_DEFAULT

        val sizeType: SizeType = context.getFieldValue(ContextAwareFormFields.KEY_SIZE_TYPE)
            ?: ContextAwareFormFields.SIZE_TYPE_DEFAULT

        // Invalid cases just for demo purposes
        return when {
            sizeType == SizeType.Small && quantity > 1 -> {
                FormFieldValidationResult.Invalid.of(
                    R.string.apple_quantity_error,
                    listOf(appleType.name, sizeType.name, 1)
                )
            }

            appleType == AppleType.Golden && quantity > 15 -> {
                FormFieldValidationResult.Invalid.of(
                    R.string.apple_quantity_error,
                    listOf(appleType.name, sizeType.name, 15)
                )
            }

            appleType == AppleType.Jonagold && sizeType == SizeType.Big -> {
                FormFieldValidationResult.Invalid.of(
                    R.string.apple_quantity_error,
                    listOf(appleType.name, sizeType.name, 0)
                )
            }

            quantity > 20 -> {
                FormFieldValidationResult.Invalid.of(
                    R.string.apple_quantity_error,
                    listOf(appleType.name, sizeType.name, 20)
                )
            }

            else -> FormFieldValidationResult.Valid
        }
    }
}
