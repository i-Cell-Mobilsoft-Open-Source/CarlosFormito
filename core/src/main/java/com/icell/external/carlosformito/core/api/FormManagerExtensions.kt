package com.icell.external.carlosformito.core.api

import com.icell.external.carlosformito.core.api.validator.FormValueContext

/**
 * Retrieves the current value of the form field associated with the given ID, ensuring it is not null.
 *
 * @param id The unique identifier of the form field.
 * @return The current value of the form field.
 * @throws IllegalArgumentException If the retrieved value is `null`.
 * @throws NoSuchElementException If the field with the specified ID does not exist.
 */
fun <T> FormValueContext.requireFieldValue(id: String): T = requireNotNull(getFieldValue(id))
