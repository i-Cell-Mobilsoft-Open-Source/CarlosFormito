package hu.icellmobilsoft.carlosformito.demo.ui.custom.fields

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import hu.icellmobilsoft.carlosformito.core.api.FormFieldItem
import hu.icellmobilsoft.carlosformito.core.ui.TrackVisibilityEffect
import hu.icellmobilsoft.carlosformito.core.ui.extensions.collectFieldState
import hu.icellmobilsoft.carlosformito.core.ui.extensions.errorMessage
import hu.icellmobilsoft.carlosformito.core.ui.testId
import hu.icellmobilsoft.carlosformito.demo.ui.custom.fields.model.ValidityStart
import hu.icellmobilsoft.carlosformito.ui.extension.requireActivity
import hu.icellmobilsoft.carlosformito.ui.field.base.TextFieldSupportingText
import hu.icellmobilsoft.carlosformito.ui.theme.LocalCarlosFormats
import hu.icellmobilsoft.carlosformito.ui.util.DatePickerBuilder
import hu.icellmobilsoft.carlosformito.ui.util.TimePickerBuilder
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.format.DateTimeFormat

@Composable
fun FormValidityStartField(
    modifier: Modifier = Modifier,
    fieldItem: FormFieldItem<ValidityStart>,
    enabled: Boolean = true,
    minDate: LocalDate? = null,
    maxDate: LocalDate? = null,
    dateFormat: DateTimeFormat<LocalDate> = LocalCarlosFormats.current.dateFormat,
    timeFormat: DateTimeFormat<LocalTime> = LocalCarlosFormats.current.timeFormat,
    isSystem24Hour: Boolean = LocalCarlosFormats.current.is24HourFormat,
    supportingText: CharSequence? = null
) {
    val state by fieldItem.collectFieldState()

    TrackVisibilityEffect { visible ->
        fieldItem.onFieldVisibilityChanged(visible)
    }

    Column(
        modifier = modifier
    ) {
        val context = LocalContext.current

        var savedValue by rememberSaveable {
            mutableStateOf(state.value ?: ValidityStart())
        }

        Row {
            val displayedDateValue = savedValue.date?.let { date ->
                dateFormat.format(date)
            } ?: "Select date"

            SelectValueCard(
                value = displayedDateValue,
                enabled = enabled,
                modifier = Modifier.weight(1f)
            ) {
                DatePickerBuilder.build(
                    dialogTitle = "Select date",
                    minDate = minDate,
                    maxDate = maxDate,
                    selectedDate = savedValue.date,
                    onDateSelected = { selectedDate ->
                        savedValue = savedValue.copy(date = selectedDate)

                        if (savedValue.isFilled()) {
                            fieldItem.onFieldValueChanged(savedValue)
                        }
                    }
                ).show(context.requireActivity().supportFragmentManager, "Validitu")
            }
            Spacer(modifier = Modifier.width(16.dp))

            val displayedTimeValue = savedValue.time?.let { time ->
                timeFormat.format(time)
            } ?: "Select time"

            SelectValueCard(
                value = displayedTimeValue,
                enabled = enabled,
                modifier = Modifier.weight(1f)
            ) {
                TimePickerBuilder.build(
                    dialogTitle = "Select time",
                    selectedTime = savedValue.time,
                    is24HourFormat = isSystem24Hour,
                    onTimeSelected = { selectedTime ->
                        savedValue = savedValue.copy(time = selectedTime)

                        if (savedValue.isFilled()) {
                            fieldItem.onFieldValueChanged(savedValue)
                        }
                    }
                ).show(context.requireActivity().supportFragmentManager, "FormTimePicker")
            }
        }

        val displayedSupportingText = if (state.isError) {
            state.errorMessage() ?: supportingText
        } else {
            supportingText
        }

        AnimatedVisibility(visible = !displayedSupportingText.isNullOrBlank()) {
            TextFieldSupportingText(
                modifier = Modifier
                    .testId("text_supported")
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 12.dp)
                    .padding(horizontal = 16.dp),
                isError = state.isError,
                supportingText = displayedSupportingText ?: ""
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun SelectValueCard(
    value: String,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        elevation = 0.dp,
        modifier = modifier,
        enabled = enabled,
        onClick = onClick,
        backgroundColor = MaterialTheme.colors.primary.copy(alpha = 0.06f)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.body1,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null)
        }
    }
}
