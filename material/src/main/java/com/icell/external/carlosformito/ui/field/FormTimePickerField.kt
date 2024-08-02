package com.icell.external.carlosformito.ui.field

import androidx.compose.material.TextFieldColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import com.icell.external.carlosformito.core.api.FormFieldItem
import com.icell.external.carlosformito.ui.extension.collectFieldState
import com.icell.external.carlosformito.ui.extension.requireActivity
import com.icell.external.carlosformito.ui.theme.LocalCarlosConfigs
import com.icell.external.carlosformito.ui.theme.LocalCarlosFormats
import com.icell.external.carlosformito.ui.util.TimePickerBuilder
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun FormTimePickerField(
    modifier: Modifier = Modifier,
    fieldItem: FormFieldItem<LocalTime>,
    textStyle: TextStyle = LocalCarlosConfigs.current.textStyle,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    pickerIcon: ImageVector = Icons.Default.AccessTime,
    outlined: Boolean = LocalCarlosConfigs.current.outlined,
    shape: Shape = LocalCarlosConfigs.current.shape,
    colors: TextFieldColors = LocalCarlosConfigs.current.colors,
    timeFormatter: DateTimeFormatter = LocalCarlosFormats.current.timeFormatter,
    dialogTitle: String,
    onClick: (() -> Unit)? = null,
    timeFormat: Int = LocalCarlosFormats.current.timeFormat,
    enabled: Boolean = true,
    isClearable: Boolean = true,
    clearIcon: ImageVector = Icons.Default.Clear,
    contentDescription: String? = null,
    customErrorMessage: String? = null,
    supportingText: CharSequence? = null,
    testTag: String? = null,
) {
    val context = LocalContext.current
    val state by fieldItem.collectFieldState()

    FormPickerField(
        modifier = modifier,
        fieldItem = fieldItem,
        textStyle = textStyle,
        label = label,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        pickerIcon = pickerIcon,
        outlined = outlined,
        shape = shape,
        colors = colors,
        onClick = onClick ?: {
            TimePickerBuilder.build(
                dialogTitle = dialogTitle,
                selectedTime = state.value,
                timeFormat = timeFormat,
                onTimeSelected = { selectedDate ->
                    fieldItem.onFieldValueChanged(selectedDate)
                }
            ).show(context.requireActivity().supportFragmentManager, "FormTimePickerField")
        },
        enabled = enabled,
        isClearable = isClearable,
        clearIcon = clearIcon,
        displayedValue = { value ->
            value?.let {
                timeFormatter.format(value)
            } ?: ""
        },
        contentDescription = contentDescription,
        customErrorMessage = customErrorMessage,
        supportingText = supportingText,
        testTag = testTag
    )
}
