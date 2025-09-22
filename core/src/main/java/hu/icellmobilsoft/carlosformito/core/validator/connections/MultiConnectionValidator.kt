package hu.icellmobilsoft.carlosformito.core.validator.connections

import hu.icellmobilsoft.carlosformito.core.api.validator.CrossFormFieldValidator

/**
 * Abstract base class for validators that require awareness of multiple form fields.
 *
 * Unlike simple validators that only validate a single field in isolation, this type
 * of validator establishes connections to other fields and performs cross-field validation.
 * These connections are defined by [connectedFieldIds], which specify the IDs of fields
 * whose values should be taken into account during validation.
 *
 * Typical use cases include:
 * - **Equality checks**: Ensuring that two fields have the same value
 *   (e.g., password and confirm password).
 * - **Composite value validation**: Validating a field whose value depends on other fields
 *   (e.g., full phone number assembled from country code + local number).
 * - **Context-aware validation**: Validating a field in relation to other field values
 *   (e.g., checking that a price is correct based on selected unit type, VAT rate, discounts, etc.).
 *
 * @param T The type of value associated with the form field being validated.
 */
abstract class MultiConnectionValidator<T> : CrossFormFieldValidator<T> {
    abstract val connectedFieldIds: List<String>
}
