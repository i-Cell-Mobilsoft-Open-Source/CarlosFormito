package hu.icellmobilsoft.carlosformito.ui.field

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.KeyboardAlt
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.TimePickerLayoutType
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.Typography
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import hu.icellmobilsoft.carlosformito.core.api.FormFieldItem
import hu.icellmobilsoft.carlosformito.core.ui.extensions.collectFieldState
import hu.icellmobilsoft.carlosformito.ui.extension.selectedTime
import hu.icellmobilsoft.carlosformito.ui.theme.LocalCarlosConfigs
import hu.icellmobilsoft.carlosformito.ui.theme.LocalCarlosFormats
import kotlinx.datetime.LocalTime
import kotlinx.datetime.format.DateTimeFormat

/**
 * A composable function for a time picker field with various customization options.
 *
 * @param modifier a [Modifier] for this picker field
 * @param fieldItem the [FormFieldItem] that holds the state of the form field and dispatches ui related events
 * e.g. field value change, visibility change and focus clear to the form manager
 * @param textStyle the style to be applied to the input text. The default [textStyle] uses the
 * [LocalCarlosConfigs]'s textStyle defined by the theme.
 * @param label the optional label to be displayed inside the text field container. The default
 * text style for internal [Text] is [Typography.bodySmall] when the text field is in focus and
 * [Typography.bodyLarge] when the text field is not in focus
 * @param placeholder the optional placeholder to be displayed when the text field is in focus and
 * the input text is empty. The default text style for internal [Text] is [Typography.bodyLarge]
 * @param leadingIcon the optional leading icon to be displayed at the beginning of the picker field
 * container
 * @param pickerIcon the [ImageVector] icon displayed at the end of the picker field
 * when the current value is null or [isClearable] is `false`
 * @param prefix the optional prefix to be displayed before the displayed value in the picker field
 * @param suffix the optional suffix to be displayed after the displayed value in the picker field
 * @param outlined boolean indicating if the picker field should be outlined.
 * The default [outlined] uses the [LocalCarlosConfigs]'s outlined param defined by the theme.
 * @param shape the shape of the picker field's border
 * The default [shape] uses the [LocalCarlosConfigs]'s shape defined by the theme, which defaults to
 * [OutlinedTextFieldDefaults.shape] if the field is outlined or [TextFieldDefaults.shape] otherwise.
 * @param colors [TextFieldColors] that will be used to resolve color of the text and content
 * (including label, placeholder, leading and trailing icons, border) for this picker field in
 * different states. The default [colors] uses the [LocalCarlosConfigs]'s colors defined by the theme, which defaults to
 * [OutlinedTextFieldDefaults.colors] if the field is outlined or [TextFieldDefaults.colors] otherwise
 * @param timeFormat the [DateTimeFormat] used to format the displayed time.
 * The default [timeFormat] uses the [LocalCarlosFormats]'s timeFormatter defined by the theme
 * which defaults to [LocalTime.Formats.ISO].
 * @param dialogTitle the title to be displayed on the time picker dialog
 * @param onClick the callback that is triggered when the picker field is clicked
 * @param is24HourFormat indicates if the 24-hour - or the 12 hour (AM/ PM) - format should be used for displaying time
 * on the picker dialog. The default [is24HourFormat] uses the [LocalCarlosFormats]'s defined value by the theme
 * which defaults to true
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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormTimePickerField(
    modifier: Modifier = Modifier,
    fieldItem: FormFieldItem<LocalTime>,
    textStyle: TextStyle = LocalCarlosConfigs.current.textStyle,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    pickerIcon: ImageVector = Icons.Default.AccessTime,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    outlined: Boolean = LocalCarlosConfigs.current.outlined,
    shape: Shape = LocalCarlosConfigs.current.shape,
    colors: TextFieldColors = LocalCarlosConfigs.current.colors,
    timeFormat: DateTimeFormat<LocalTime> = LocalCarlosFormats.current.timeFormat,
    dialogTitle: String,
    onClick: (() -> Unit)? = null,
    is24HourFormat: Boolean = LocalCarlosFormats.current.is24HourFormat,
    enabled: Boolean = true,
    isClearable: Boolean = true,
    clearIcon: ImageVector = Icons.Default.Clear,
    contentDescription: String? = null,
    customErrorMessage: String? = null,
    supportingText: CharSequence? = null,
    testTag: String? = null,
) {
    val state by fieldItem.collectFieldState()

    val displayMode = remember { mutableStateOf(DisplayMode.Picker) }
    var dialogVisible by remember { mutableStateOf(false) }

    if (dialogVisible) {
        val currentValue = state.value ?: LocalTime(0, 0)
        val timePickerState = rememberTimePickerState(
            initialHour = currentValue.hour,
            initialMinute = currentValue.minute,
            is24Hour = is24HourFormat
        )

        TimePickerDialog(
            onDismissRequest = { dialogVisible = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        fieldItem.onFieldValueChanged(timePickerState.selectedTime)
                        dialogVisible = false
                    }
                ) {
                    Text(text = stringResource(android.R.string.ok))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { dialogVisible = false }
                ) {
                    Text(text = stringResource(android.R.string.cancel))
                }
            },
            displayModeIcon = {
                ChangeLayoutIcon(displayMode)
            }
        ) {
            TimePicker(
                timePickerState = timePickerState,
                displayMode = displayMode,
                title = dialogTitle
            )
        }
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
                timeFormat.format(value)
            } ?: ""
        },
        contentDescription = contentDescription,
        customErrorMessage = customErrorMessage,
        supportingText = supportingText,
        testTag = testTag
    )
}

/**
 * A composable function that displays an icon button for toggling between different display modes.
 * The icon and functionality switch between [DisplayMode.Input] and [DisplayMode.Picker].
 * The icon's appearance is determined by the current display mode.
 *
 * @param displayMode A [MutableState] holding the current [DisplayMode]
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangeLayoutIcon(displayMode: MutableState<DisplayMode>) {
    IconButton(
        onClick = {
            displayMode.value = when (displayMode.value) {
                DisplayMode.Input -> DisplayMode.Picker
                else -> DisplayMode.Input
            }
        }
    ) {
        Icon(
            imageVector = when (displayMode.value) {
                DisplayMode.Input -> Icons.Outlined.AccessTime
                else -> Icons.Outlined.KeyboardAlt
            },
            contentDescription = null,
            tint = LocalContentColor.current.copy(alpha = 0.8f)
        )
    }
}

/**
 * A composable function that displays a time picker with an option to switch between different display modes.
 *
 * This function shows a [TimePicker] or [TimeInput] based on the current [displayMode]. It also displays a title
 * and an optional icon or button for changing the layout (switching between picker and input modes).
 *
 * @param timePickerState The state object to control the selected time and related properties.
 * @param modifier A [Modifier] for this time picker
 * @param displayMode A mutable state that controls whether the time picker is in picker mode or input mode.
 * @param title The title text to display above the time picker.
 * @param changeLayoutIcon An optional composable to display an icon or button for changing the time picker layout.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimePicker(
    timePickerState: TimePickerState,
    modifier: Modifier = Modifier,
    displayMode: MutableState<DisplayMode>,
    title: String,
    changeLayoutIcon: @Composable (() -> Unit)? = null
) {
    Column(modifier = modifier) {
        Text(text = title)
        Spacer(modifier = Modifier.height(24.dp))
        AnimatedContent(
            targetState = displayMode.value,
            label = "TimePicker"
        ) { targetState ->
            when (targetState) {
                DisplayMode.Picker -> {
                    TimePicker(
                        state = timePickerState,
                        colors = TimePickerDefaults.colors(),
                        layoutType = TimePickerLayoutType.Vertical
                    )
                }

                DisplayMode.Input -> {
                    TimeInput(
                        state = timePickerState,
                        colors = TimePickerDefaults.colors()
                    )
                }
            }
        }
        changeLayoutIcon?.invoke()
    }
}

/**
 * A composable function that displays dialog wrapping a time picker in the [content] composable.
 *
 * This function provides a structured dialog that contains a customizable content area for the time picker
 * and buttons for confirming or dismissing the dialog. An optional icon for changing the display mode
 * of the time picker can also be included.
 *
 * @param onDismissRequest Lambda function to be invoked when the dialog is dismissed.
 * @param confirmButton Composable function to define the confirm button.
 * @param modifier A [Modifier] for this time picker dialog
 * @param dismissButton Optional composable function to define the dismiss button.
 * @param displayModeIcon Optional composable function to define an icon or button for changing the display mode.
 * @param content Composable lambda defining the content of the dialog.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimePickerDialog(
    onDismissRequest: () -> Unit,
    confirmButton: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    dismissButton: @Composable (() -> Unit)? = null,
    displayModeIcon: @Composable (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    BasicAlertDialog(
        onDismissRequest = onDismissRequest,
        modifier = modifier.wrapContentHeight()
    ) {
        Surface(shape = MaterialTheme.shapes.large) {
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.padding(24.dp)
            ) {
                content()
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    displayModeIcon?.invoke()
                    Spacer(modifier = modifier.weight(1f))
                    dismissButton?.invoke()
                    confirmButton()
                }
            }
        }
    }
}
