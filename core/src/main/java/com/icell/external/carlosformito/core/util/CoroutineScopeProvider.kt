package com.icell.external.carlosformito.core.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope

/**
 * Marker interface for a class that provides a coroutineScope
 */
internal interface CoroutineScopeProvider

internal val CoroutineScopeProvider.coroutineScope: CoroutineScope?
    get() {
        return if (this is ViewModel) {
            viewModelScope
        } else {
            null
        }
    }
