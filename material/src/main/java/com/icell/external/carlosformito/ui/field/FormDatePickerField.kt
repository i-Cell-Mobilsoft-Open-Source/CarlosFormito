package com.icell.external.carlosformito.ui.field

import androidx.compose.material.Text
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.Typography
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import com.icell.external.carlosformito.core.api.FormFieldItem
import com.icell.external.carlosformito.core.ui.extensions.collectFieldState
import com.icell.external.carlosformito.ui.extension.requireActivity
import com.icell.external.carlosformito.ui.theme.LocalCarlosConfigs
import com.icell.external.carlosformito.ui.theme.LocalCarlosFormats
import com.icell.external.carlosformito.ui.util.DatePickerBuilder
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * A composable function for a date picker field with various customization options.
 *
 * @param modifier a [Modifier] for this picker field
 * @param fieldItem the [FormFieldItem] that holds the state of the form field and dispatches ui related events
 * e.g. field value change, visibility change and focus clear to the form manager
 * @param textStyle the style to be applied to the input text. The default [textStyle] uses the
 * [LocalCarlosConfigs]'s textStyle defined by the theme.
 * @param label the optional label to be displayed inside the picker field container. The default
 * text style for internal [Text] is [Typography.caption] when the picker field is in focus and
 * [Typography.subtitle1] when the picker field is not in focus
 * @param placeholder the optional placeholder to be displayed when the picker field is in focus and
 * the input text is empty. The default text style for internal [Text] is [Typography.subtitle1]
 * @param leadingIcon the optional leading icon to be displayed at the beginning of the picker field
 * container
 * @param pickerIcon the [ImageVector] icon displayed at the end of the picker field
 * when the current value is null or [isClearable] is `false`
 * @param outlined boolean indicating if the picker field should be outlined.
 * The default [outlined] uses the [LocalCarlosConfigs]'s outlined param defined by the theme.
 * @param shape the shape of the picker field's border
 * The default [shape] uses the [LocalCarlosConfigs]'s shape defined by the theme, which defaults to
 * [TextFieldDefaults.OutlinedTextFieldShape] if the field is outlined or [TextFieldDefaults.TextFieldShape] otherwise.
 * @param colors [TextFieldColors] that will be used to resolve color of the text and content
 * (including label, placeholder, leading and trailing icons, border) for this picker field in
 * different states. The default [colors] uses the [LocalCarlosConfigs]'s colors defined by the theme, which defaults to
 * [TextFieldDefaults.outlinedTextFieldColors] if the field is outlined or [TextFieldDefaults.textFieldColors] otherwise
 * @param dateFormatter the [DateTimeFormatter] used to format the displayed date.
 * The default [dateFormatter] uses the [LocalCarlosFormats]'s dateFormatter defined by the theme
 * which defaults to [DateTimeFormatter.ISO_LOCAL_DATE]
 * @param dialogTitle the title to be displayed on the date picker dialog
 * @param minDate The minimum date that can be selected
 * @param maxDate The maximum date that can be selected
 * @param onClick the callback that is triggered when the picker field is clicked
 * @param enabled controls the enabled state of the picker field. When `false`, the picker field will
 * be neither editable nor focusable, the input of the picker field will not be selectable,
 * visually picker field will appear in the disabled UI state
 * @param isClearable indicates if the picked value of the picker field can be cleared
 * @param clearIcon the [ImageVector] icon displayed at the end of the picker field
 * when the current value is not null and [isClearable] is `true`
 * @param contentDescription optional content description for accessibility
 * @param customErrorMessage a custom error message to be displayed when there is an error.
 * If null, the default error message from the field's state is used
 * @param supportingText the optional supporting text to be displayed below the picker field
 * @param testTag optional test tag for testing purposes
 */
@Composable
fun FormDatePickerField(
    modifier: Modifier = Modifier,
    fieldItem: FormFieldItem<LocalDate>,
    textStyle: TextStyle = LocalCarlosConfigs.current.textStyle,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    pickerIcon: ImageVector = Icons.Default.CalendarMonth,
    outlined: Boolean = LocalCarlosConfigs.current.outlined,
    shape: Shape = LocalCarlosConfigs.current.shape,
    colors: TextFieldColors = LocalCarlosConfigs.current.colors,
    dateFormatter: DateTimeFormatter = LocalCarlosFormats.current.dateFormatter,
    dialogTitle: String,
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
            DatePickerBuilder.build(
                dialogTitle = dialogTitle,
                minDate = minDate,
                maxDate = maxDate,
                selectedDate = state.value,
                onDateSelected = { selectedDate ->
                    fieldItem.onFieldValueChanged(selectedDate)
                }
            ).show(context.requireActivity().supportFragmentManager, "FormDatePickerField")
        },
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
