package com.icell.external.carlosformito.ui.field.base

sealed interface TextFieldInputMode {
    data object Default : TextFieldInputMode

    class Picker(
        val onClick: () -> Unit,
        val isClearable: Boolean,
        val onClear: () -> Unit
    ) : TextFieldInputMode
}
