package com.icell.external.carlosformito.core.validator.connections

import com.icell.external.carlosformito.core.api.validator.CrossFormFieldValidator

/**
 * Abstract base class for validators that depend on a single related form field.
 *
 * Unlike field-level validators that only validate a field in isolation,
 * this validator establishes a connection to another field (identified by [connectedFieldId])
 * and uses its value during validation.
 *
 * This enables scenarios where the validity of a field cannot be determined
 * without referencing another field. Examples include:
 * - **Equality checks**: Ensuring that two fields have the same value
 *   (e.g., password and confirm password).
 * - **Composite value validation**: Validating a field whose value is derived from another field
 *   (e.g., validating a full phone number composed of country code + number).
 *
 * @param T The type of value associated with the form field being validated.
 */
abstract class ConnectionValidator<T> : CrossFormFieldValidator<T> {
    abstract val connectedFieldId: String
}
