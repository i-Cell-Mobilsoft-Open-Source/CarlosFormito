package com.icell.external.carlosformito.core.api.validator

import androidx.annotation.StringRes

sealed interface FormFieldValidationResult {

    object Valid : FormFieldValidationResult

    sealed class Invalid(
        @StringRes open val errorMessageId: Int? = null
    ) : FormFieldValidationResult {

        object Unknown : Invalid()

        data class Message(
            @StringRes override val errorMessageId: Int
        ) : Invalid(errorMessageId)

        data class MessageWithArgs(
            @StringRes override val errorMessageId: Int,
            val formatArgs: List<Any>
        ) : Invalid(errorMessageId)
    }
}
