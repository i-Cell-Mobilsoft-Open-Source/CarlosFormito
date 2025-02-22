package com.icell.external.carlosformito.demo.ui.custom.fields

import android.text.format.DateFormat
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
import com.google.android.material.timepicker.TimeFormat
import com.icell.external.carlosformito.core.api.FormFieldItem
import com.icell.external.carlosformito.core.ui.TrackVisibilityEffect
import com.icell.external.carlosformito.core.ui.collectFieldState
import com.icell.external.carlosformito.core.ui.errorMessage
import com.icell.external.carlosformito.core.ui.testId
import com.icell.external.carlosformito.demo.ui.custom.fields.model.ValidityStart
import com.icell.external.carlosformito.ui.extension.requireActivity
import com.icell.external.carlosformito.ui.field.base.TextFieldSupportingText
import com.icell.external.carlosformito.ui.util.DatePickerBuilder
import com.icell.external.carlosformito.ui.util.TimePickerBuilder
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun FormValidityStartField(
    modifier: Modifier = Modifier,
    fieldItem: FormFieldItem<ValidityStart>,
    enabled: Boolean = true,
    minDate: LocalDate? = null,
    maxDate: LocalDate? = null,
    dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd."),
    timeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm"),
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
                dateFormatter.format(date)
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
                timeFormatter.format(time)
            } ?: "Select time"

            SelectValueCard(
                value = displayedTimeValue,
                enabled = enabled,
                modifier = Modifier.weight(1f)
            ) {
                val isSystem24Hour = DateFormat.is24HourFormat(context)
                val clockFormat = if (isSystem24Hour) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H

                TimePickerBuilder.build(
                    dialogTitle = "Select time",
                    selectedTime = savedValue.time,
                    timeFormat = clockFormat,
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
