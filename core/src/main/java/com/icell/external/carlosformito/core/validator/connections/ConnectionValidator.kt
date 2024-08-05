package com.icell.external.carlosformito.core.validator.connections

import com.icell.external.carlosformito.core.api.validator.CrossFormFieldValidator

/**
 * Abstract class representing a validator of a form field
 * that has a connection (in terms of validation) to another field.
 *
 * The [connectedFieldId] parameter is used for auto validating field connections in different use-cases like:
 * - validating whether a fields value is equal to another field value or not (eg.: password confirmation)
 * - validating a field which value is composed by another fields value (eg.: country code + phone number)
 *
 * @param T The type of value associated with the form field to be validated.
 */
abstract class ConnectionValidator<T> : CrossFormFieldValidator<T> {
    abstract val connectedFieldId: String
}
