package com.icell.external.carlosformito.core.api

interface FormFieldHandleCallback {

    fun onFieldFocusCleared(id: String)

    fun <T> onFieldValueChanged(id: String, value: T?)
}
