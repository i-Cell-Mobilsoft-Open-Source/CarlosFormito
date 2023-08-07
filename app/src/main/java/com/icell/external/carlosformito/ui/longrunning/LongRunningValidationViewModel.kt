package com.icell.external.carlosformito.ui.longrunning

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.icell.external.carlosformito.core.FormManagerViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LongRunningValidationViewModel : FormManagerViewModel(LongRunningValidationFields.build()) {

    private val mutableCurrentEmail = MutableStateFlow<String?>(null)
    val currentEmail = mutableCurrentEmail.asStateFlow()

    private val mutableDataIsLoading = MutableStateFlow(false)
    val dataIsLoading = mutableDataIsLoading.asStateFlow()

    val showLoadingIndicator: StateFlow<Boolean> =
        combine(dataIsLoading, validationInProgress) { isLoading, inProgress ->
            isLoading || inProgress
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)

    private val mutableIsError = MutableSharedFlow<Throwable>(extraBufferCapacity = 1)
    val isError = mutableIsError.asSharedFlow()

    init {
        loadCurrentEmail()

        validationExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            mutableIsError.tryEmit(throwable)
        }
    }

    fun submit() {
        viewModelScope.launch {
            if (validateForm()) {
                Log.i("LongRunningValidationViewModel", "Form is valid")
            }
        }
    }

    @Suppress("MagicNumber")
    private fun loadCurrentEmail() {
        viewModelScope.launch {
            mutableDataIsLoading.value = true
            delay(2000)
            mutableCurrentEmail.value = "test123@gmail.com"
            mutableDataIsLoading.value = false
        }
    }
}
