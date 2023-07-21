package com.icell.external.carlosformito.ui.util.extension

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import com.icell.external.carlosformito.core.api.model.FormFieldState
import com.icell.external.carlosformito.core.api.FormManager

fun <T> FormManager.getFieldValue(id: String): T? = getFieldStateFlow<T>(id).value.value

fun <T> FormManager.requireFieldValue(id: String): T = requireNotNull(getFieldValue(id))

@Composable
fun <T> FormManager.collectFieldState(id: String): State<FormFieldState<T>> =
    getFieldStateFlow<T>(id).collectAsState()
