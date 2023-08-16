package com.icell.external.carlosformito

import com.icell.external.carlosformito.api.FormFieldItem
import com.icell.external.carlosformito.api.FormFieldItemListener
import com.icell.external.carlosformito.api.model.FormFieldState
import kotlinx.coroutines.flow.StateFlow

class FormFieldItemImpl<T>(
    private val fieldId: String,
    override val fieldState: StateFlow<FormFieldState<T>>
) : FormFieldItem<T> {

    private var listener: FormFieldItemListener? = null

    override fun onFieldFocusCleared() {
        listener?.onFieldFocusCleared(fieldId)
    }

    override fun <T> onFieldValueChanged(value: T?) {
        listener?.onFieldValueChanged(fieldId, value)
    }

    fun setListener(listener: FormFieldItemListener?) {
        this.listener = listener
    }
}
