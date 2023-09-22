package com.icell.external.carlosformito.ui.theme

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticCompositionLocalOf
import com.icell.external.carlosformito.commonui.R

@Stable
data class CarlosIcons(
    @DrawableRes val passwordVisible: Int,
    @DrawableRes val passwordInvisible: Int,
    @DrawableRes val arrowDropDown: Int,
    @DrawableRes val calendar: Int,
    @DrawableRes val schedule: Int,
    @DrawableRes val clear: Int
)

val DefaultFilledCarlosIcons = CarlosIcons(
    passwordVisible = R.drawable.ic_password_visible_filled,
    passwordInvisible = R.drawable.ic_password_invisible_filled,
    arrowDropDown = R.drawable.ic_arrow_drop_down,
    calendar = R.drawable.ic_calendar_filled,
    schedule = R.drawable.ic_schedule_filled,
    clear = R.drawable.ic_clear
)

val DefaultOutlinedCarlosIcons = CarlosIcons(
    passwordVisible = R.drawable.ic_password_visible_outlined,
    passwordInvisible = R.drawable.ic_password_invisible_outlined,
    arrowDropDown = R.drawable.ic_arrow_drop_down,
    calendar = R.drawable.ic_calendar_outlined,
    schedule = R.drawable.ic_schedule_outlined,
    clear = R.drawable.ic_clear
)

val LocalCarlosIcons = staticCompositionLocalOf { DefaultFilledCarlosIcons }
