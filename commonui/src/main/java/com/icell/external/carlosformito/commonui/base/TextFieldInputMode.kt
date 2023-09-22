package com.icell.external.carlosformito.commonui.base

sealed interface TextFieldInputMode {
    data object Default : TextFieldInputMode

    class Picker(
        val onClick: () -> Unit,
        val isClearable: Boolean,
        val onClear: () -> Unit
    ) : TextFieldInputMode
}
