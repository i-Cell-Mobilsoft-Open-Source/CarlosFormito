package com.icell.external.carlosformito.core.api.validator

import androidx.annotation.StringRes

/**
 * Sealed interface representing the result of a form field validation.
 */
sealed interface FormFieldValidationResult {

    /**
     * Represents a valid validation result.
     */
    data object Valid : FormFieldValidationResult

    /**
     * Represents an invalid validation result.
     */
    sealed class Invalid(
        /**
         * Optional resource ID of the error message.
         */
        @StringRes open val errorMessageId: Int? = null
    ) : FormFieldValidationResult {

        /**
         * Represents an unknown validation error.
         */
        data object Unknown : Invalid()

        /**
         * Represents a validation error with a specific error message resource ID.
         *
         * @property errorMessageId Resource ID of the error message.
         */
        data class Message(
            @StringRes override val errorMessageId: Int
        ) : Invalid(errorMessageId)

        /**
         * Represents a validation error with a specific error message resource ID and format arguments.
         *
         * @property errorMessageId Resource ID of the error message.
         * @property formatArgs List of format arguments for the error message.
         */
        data class MessageWithArgs(
            @StringRes override val errorMessageId: Int,
            val formatArgs: List<Any>
        ) : Invalid(errorMessageId)

        companion object {
            /**
             * Factory method to create an Invalid instance based on the error message ID and format arguments.
             *
             * @param errorMessageId Resource ID of the error message.
             * @param formatArgs List of format arguments for the error message.
             * @return Invalid instance representing the validation error.
             */
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
