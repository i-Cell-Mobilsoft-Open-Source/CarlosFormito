package com.icell.external.carlosformito.core.api.model

import kotlin.time.Duration

/**
 * Supported strategies for form field validation.
 */
sealed interface FormFieldValidationStrategy {
    /**
     * Validate all fields together manually
     */
    data object Manual : FormFieldValidationStrategy

    /**
     * Validating each field automatically by focus clear events
     */
    data object AutoOnFocusClear : FormFieldValidationStrategy

    /**
     * Validating each field automatically by field value change events
     * @param delay An optional delay parameter for delaying the fields validation
     */
    data class AutoInline(val delay: Duration? = null) : FormFieldValidationStrategy
}
