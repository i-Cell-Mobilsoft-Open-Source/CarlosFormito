package com.icell.external.carlosformito.demo.ui.email

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.icell.external.carlosformito.core.CarlosFormManager
import com.icell.external.carlosformito.core.api.FormManager
import com.icell.external.carlosformito.core.api.model.FormFieldValidationStrategy
import com.icell.external.carlosformito.core.api.valueState
import com.icell.external.carlosformito.demo.ui.email.ChangeEmailFields.KEY_NEW_EMAIL
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ChangeEmailViewModel(
    validationStrategy: FormFieldValidationStrategy
) : ViewModel(), FormManager by CarlosFormManager(ChangeEmailFields.build(), validationStrategy) {

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

    private val mutableSameOldAndNewEmail = MutableStateFlow(false)
    val sameOldAndNewEmail = mutableSameOldAndNewEmail.asStateFlow()

    val submitButtonEnabled: StateFlow<Boolean> = combine(
        allRequiredFieldFilled,
        sameOldAndNewEmail,
        showLoadingIndicator
    ) { allRequiredFilled, sameOldAndNew, showLoadingIndicator ->
        allRequiredFilled && !sameOldAndNew && !showLoadingIndicator
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)

    init {
        viewModelScope.launch {
            initFormManager()
        }
        viewModelScope.launch {
            observeEmailChanges()
        }
        loadCurrentEmail()
    }

    private suspend fun observeEmailChanges() {
        getFieldItem<String>(KEY_NEW_EMAIL).valueState.collectLatest { newEmail ->
            mutableSameOldAndNewEmail.value = newEmail != null && newEmail == currentEmail.value
        }
    }

    fun submit() {
        viewModelScope.launch {
            try {
                if (validateForm()) {
                    Log.i("ChangeEmailViewModel", "Form is valid")
                }
            } catch (throwable: Throwable) {
                mutableIsError.tryEmit(throwable)
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
