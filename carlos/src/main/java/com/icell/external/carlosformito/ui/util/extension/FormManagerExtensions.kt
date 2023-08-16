package com.icell.external.carlosformito.ui.util.extension

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import com.icell.external.carlosformito.api.FormFieldItem
import com.icell.external.carlosformito.api.FormManager
import com.icell.external.carlosformito.api.model.FormFieldState

fun <T> FormManager.getFieldValue(id: String): T? = getFieldItem<T>(id).fieldState.value.value

fun <T> FormManager.requireFieldValue(id: String): T = requireNotNull(getFieldValue(id))

@Composable
fun <T> FormManager.collectFieldState(id: String): State<FormFieldState<T>> =
    getFieldItem<T>(id).collectFieldState()

@Composable
fun <T> FormFieldItem<T>.collectFieldState(): State<FormFieldState<T>> = fieldState.collectAsState()
