package com.icell.external.carlosformito.ui.extension

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import com.icell.external.carlosformito.core.api.FormFieldItem
import com.icell.external.carlosformito.core.api.FormManager
import com.icell.external.carlosformito.core.api.model.FormFieldState

@Composable
fun <T> FormManager.collectFieldState(id: String): State<FormFieldState<T>> =
    getFieldItem<T>(id).collectFieldState()

@Composable
fun <T> FormFieldItem<T>.collectFieldState(): State<FormFieldState<T>> = fieldState.collectAsState()
