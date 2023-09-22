package com.icell.external.carlosformito.commonui.base

import androidx.annotation.DrawableRes

sealed class TextFieldAffixContentType {

    data class Text(
        val value: String
    ) : TextFieldAffixContentType()

    data class Icon(
        @DrawableRes val value: Int,
        val onClick: () -> Unit = {}
    ) : TextFieldAffixContentType()

    data object None : TextFieldAffixContentType()
}
