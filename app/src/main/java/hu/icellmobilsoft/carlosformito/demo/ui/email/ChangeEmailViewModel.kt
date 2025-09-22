package hu.icellmobilsoft.carlosformito.demo.ui.email

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.icellmobilsoft.carlosformito.core.CarlosFormManager
import hu.icellmobilsoft.carlosformito.core.api.FormManager
import hu.icellmobilsoft.carlosformito.core.api.model.FormFieldValidationStrategy
import hu.icellmobilsoft.carlosformito.core.api.valueState
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

    // Mocked current email for demo purposes
    val currentEmail = "test123@gmail.com"

    private val mutableIsError = MutableSharedFlow<Throwable>(extraBufferCapacity = 1)
    val isError = mutableIsError.asSharedFlow()

    private val mutableSameOldAndNewEmail = MutableStateFlow(false)
    val sameOldAndNewEmail = mutableSameOldAndNewEmail.asStateFlow()

    val submitButtonEnabled: StateFlow<Boolean> = combine(
        allRequiredFieldFilled,
        sameOldAndNewEmail,
        validationInProgress
    ) { allRequiredFilled, sameOldAndNew, validationInProgress ->
        allRequiredFilled && !sameOldAndNew && !validationInProgress
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)

    init {
        viewModelScope.launch {
            initFormManager()
        }
        viewModelScope.launch {
            observeEmailChanges()
        }
    }

    private suspend fun observeEmailChanges() {
        getFieldItem<String>(ChangeEmailFields.KEY_NEW_EMAIL).valueState.collectLatest { newEmail ->
            mutableSameOldAndNewEmail.value = newEmail != null && newEmail == currentEmail
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
}
