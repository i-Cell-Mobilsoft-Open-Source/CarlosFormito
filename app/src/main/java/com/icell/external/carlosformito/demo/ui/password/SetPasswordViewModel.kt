package com.icell.external.carlosformito.demo.ui.password

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.icell.external.carlosformito.core.api.FormManager
import kotlinx.coroutines.launch

class SetPasswordViewModel(
    private val setPasswordFormManager: SetPasswordFormManager = SetPasswordFormManager()
) : ViewModel(), FormManager by setPasswordFormManager {

    fun submit() {
        viewModelScope.launch {
            if (validateForm()) {
                Log.i("SetPasswordViewModel", "Form is valid")
            }
        }
    }
}
