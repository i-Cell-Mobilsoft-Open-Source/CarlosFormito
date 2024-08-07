package com.icell.external.carlosformito.ui.field

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import com.icell.external.carlosformito.core.api.FormFieldItem
import com.icell.external.carlosformito.ui.datepicker.CarlosDatePicker
import com.icell.external.carlosformito.ui.extension.collectFieldState
import com.icell.external.carlosformito.ui.theme.LocalCarlosConfigs
import com.icell.external.carlosformito.ui.theme.LocalCarlosFormats
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun FormDatePickerField(
    modifier: Modifier = Modifier,
    fieldItem: FormFieldItem<LocalDate>,
    textStyle: TextStyle = LocalCarlosConfigs.current.textStyle,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    pickerIcon: ImageVector = Icons.Default.CalendarMonth,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    outlined: Boolean = LocalCarlosConfigs.current.outlined,
    shape: Shape = LocalCarlosConfigs.current.shape,
    colors: TextFieldColors = LocalCarlosConfigs.current.colors,
    dateFormatter: DateTimeFormatter = LocalCarlosFormats.current.dateFormatter,
    dialogTitle: String,
    dialogHeadline: String? = null,
    minDate: LocalDate? = null,
    maxDate: LocalDate? = null,
    onClick: (() -> Unit)? = null,
    enabled: Boolean = true,
    isClearable: Boolean = true,
    clearIcon: ImageVector = Icons.Default.Clear,
    contentDescription: String? = null,
    customErrorMessage: String? = null,
    supportingText: CharSequence? = null,
    testTag: String? = null,
) {
    val state by fieldItem.collectFieldState()

    var dialogVisible by remember { mutableStateOf(false) }
    if (dialogVisible) {
        CarlosDatePicker(
            dialogTitle = dialogTitle,
            dialogHeadline = dialogHeadline,
            formatter = dateFormatter,
            selectedDate = state.value,
            minDate = minDate,
            maxDate = maxDate,
            onSelectDate = { selectedDate ->
                fieldItem.onFieldValueChanged(selectedDate)
                dialogVisible = false
            },
            hideDialog = {
                dialogVisible = false
            }
        )
    }

    FormPickerField(
        modifier = modifier,
        fieldItem = fieldItem,
        textStyle = textStyle,
        label = label,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        pickerIcon = pickerIcon,
        prefix = prefix,
        suffix = suffix,
        outlined = outlined,
        shape = shape,
        colors = colors,
        onClick = onClick ?: { dialogVisible = true },
        enabled = enabled,
        isClearable = isClearable,
        clearIcon = clearIcon,
        displayedValue = { value ->
            value?.let {
                dateFormatter.format(value)
            } ?: ""
        },
        contentDescription = contentDescription,
        customErrorMessage = customErrorMessage,
        supportingText = supportingText,
        testTag = testTag
    )
}
