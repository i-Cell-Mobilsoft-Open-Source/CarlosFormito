package com.icell.external.carlosformito.ui.extension

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import com.icell.external.carlosformito.core.api.FormFieldItem
import com.icell.external.carlosformito.core.api.FormManager
import com.icell.external.carlosformito.core.api.model.FormFieldState

/**
 * Extension function for [FormManager] that collects the state of a form field with the given ID.
 *
 * This function retrieves the field item of type [T] by its ID and then collects its state.
 *
 * @param T The type of the form field.
 * @param id The ID of the form field.
 * @return A [State] object containing the [FormFieldState] of the specified form field.
 */
@Composable
fun <T> FormManager.collectFieldState(id: String): State<FormFieldState<T>> =
    getFieldItem<T>(id).collectFieldState()

/**
 * Extension function for [FormFieldItem] that collects the state of the form field.
 *
 * This function collects the [FormFieldState] of the form field as a [State] object
 * using the [collectAsState] function.
 *
 * @param T The type of the form field.
 * @return A [State] object containing the [FormFieldState] of the form field.
 */
@Composable
fun <T> FormFieldItem<T>.collectFieldState(): State<FormFieldState<T>> = fieldState.collectAsState()
