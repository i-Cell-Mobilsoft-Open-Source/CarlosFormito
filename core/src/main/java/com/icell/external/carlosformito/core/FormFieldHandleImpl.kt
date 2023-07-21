package com.icell.external.carlosformito.core

import com.icell.external.carlosformito.core.api.FormFieldHandle
import com.icell.external.carlosformito.core.api.FormFieldHandleCallback

class FormFieldHandleImpl<T>(private val id: String) :
    FormFieldHandle<T> {

    private var callback: FormFieldHandleCallback? = null

    override fun onFieldFocusCleared() {
        callback?.onFieldFocusCleared(id)
    }

    override fun <T> onFieldValueChanged(value: T?) {
        callback?.onFieldValueChanged(id, value)
    }

    override fun setFieldHandleCallback(callback: FormFieldHandleCallback) {
        this.callback = callback
    }
}
