package com.icell.external.carlosformito.core.api.validator

import androidx.annotation.StringRes

sealed interface FormFieldValidationResult {

    data object Valid : FormFieldValidationResult

    sealed class Invalid(
        @StringRes open val errorMessageId: Int? = null
    ) : FormFieldValidationResult {

        data object Unknown : Invalid()

        data class Message(
            @StringRes override val errorMessageId: Int
        ) : Invalid(errorMessageId)

        data class MessageWithArgs(
            @StringRes override val errorMessageId: Int,
            val formatArgs: List<Any>
        ) : Invalid(errorMessageId)

        companion object {
            fun of(@StringRes errorMessageId: Int?, formatArgs: List<Any>? = null): Invalid {
                return when {
                    errorMessageId == null -> Unknown
                    formatArgs.isNullOrEmpty() -> Message(errorMessageId)
                    else -> MessageWithArgs(errorMessageId, formatArgs)
                }
            }
        }
    }
}
