package com.icell.external.carlosformito.core.api.model

enum class FormFieldValidationStrategy {
    /**
     * Validate all fields together manually
     */
    MANUAL,

    /**
     * Validating each field automatically by focus clear events
     */
    ON_FOCUS_CLEAR,

    /**
     * Validating each field automatically by field value change events
     */
    INLINE
}
