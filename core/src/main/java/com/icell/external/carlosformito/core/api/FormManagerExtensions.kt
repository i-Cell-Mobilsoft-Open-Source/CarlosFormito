package com.icell.external.carlosformito.core.api

/**
 * Retrieves the current value of the form field associated with the given ID.
 *
 * @param id The unique identifier of the form field.
 * @return The current value of the form field, or `null` if the field is not found or its value is `null`.
 * @throws NoSuchElementException If the field with the specified ID does not exist.
 */
fun <T> FormManager.getFieldValue(id: String): T? = getFieldItem<T>(id).fieldState.value.value

/**
 * Retrieves the current value of the form field associated with the given ID, ensuring it is not null.
 *
 * @param id The unique identifier of the form field.
 * @return The current value of the form field.
 * @throws IllegalArgumentException If the retrieved value is `null`.
 * @throws NoSuchElementException If the field with the specified ID does not exist.
 */
fun <T> FormManager.requireFieldValue(id: String): T = requireNotNull(getFieldValue(id))
