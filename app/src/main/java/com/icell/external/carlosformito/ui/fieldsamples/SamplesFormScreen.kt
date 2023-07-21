package com.icell.external.carlosformito.ui.fieldsamples

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.icell.external.carlosformito.ui.common.CarlosTopAppBar
import com.icell.external.carlosformito.ui.field.FormDateField
import com.icell.external.carlosformito.ui.field.FormIntegerField
import com.icell.external.carlosformito.ui.field.FormPasswordTextField
import com.icell.external.carlosformito.ui.field.FormTextField
import com.icell.external.carlosformito.ui.fieldsamples.SamplesFormItems.KEY_FORM_FIELD_DATE
import com.icell.external.carlosformito.ui.fieldsamples.SamplesFormItems.KEY_FORM_FIELD_NAME
import com.icell.external.carlosformito.ui.fieldsamples.SamplesFormItems.KEY_FORM_FIELD_SECRET
import com.icell.external.carlosformito.ui.fieldsamples.SamplesFormItems.KEY_FORM_FIELD_SIZE
import com.icell.external.carlosformito.ui.util.extension.collectFieldState
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SampleFormScreen(
    viewModel: SamplesFormViewModel,
    onBackPressed: () -> Unit
) {
    Scaffold(
        topBar = {
            CarlosTopAppBar(
                title = "Form field samples",
                onNavigationIconPressed = onBackPressed
            )
        }
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            val nameFieldHandle = viewModel.getFieldHandle<String>(KEY_FORM_FIELD_NAME)
            val nameFieldState by viewModel.collectFieldState<String>(id = KEY_FORM_FIELD_NAME)

            FormTextField(
                state = nameFieldState,
                handle = nameFieldHandle,
                label = "Name*",
                supportingText = """
                    Please enter your full name.
                    Provided name will be seen by other users (min. 3 characters).
                """.trimIndent()
            )

            /*
            FormTextField(
                state = nameFieldState,
                label = "Name*",
                onValueChange = { value ->
                    nameFieldHandle.onFieldValueChanged(value)
                },
                onFocusCleared = {
                    nameFieldHandle.onFieldFocusCleared()
                }
            )
            FormTextField(
                value = nameFieldState.value,
                isError = nameFieldState.isError,
                errorMessage = nameFieldState.errorMessage(),
                label = "Name*",
                onValueChange = { value ->
                    nameFieldHandle.onFieldValueChanged(value)
                },
                onFocusCleared = {
                    nameFieldHandle.onFieldFocusCleared()
                }
            )*/

            val secretFieldHandle = viewModel.getFieldHandle<String>(KEY_FORM_FIELD_SECRET)
            val secretFieldState by viewModel.collectFieldState<String>(KEY_FORM_FIELD_SECRET)

            FormPasswordTextField(
                state = secretFieldState,
                handle = secretFieldHandle,
                label = "Secret*",
                supportingText = "Please give a strong password (min 8 characters)."
            )

            /*
            FormPasswordTextField(
                state = secretFieldState,
                label = "Secret*",
                onValueChange = { value ->
                    secretFieldHandle.onFieldValueChanged(value)
                },
                onFocusCleared = {
                    secretFieldHandle.onFieldFocusCleared()
                }
            )
            FormPasswordTextField(
                value = secretFieldState.value,
                isError = secretFieldState.isError,
                errorMessage = secretFieldState.errorMessage(),
                label = "Secret*",
                onValueChange = { value ->
                    secretFieldHandle.onFieldValueChanged(value)
                },
                onFocusCleared = {
                    secretFieldHandle.onFieldFocusCleared()
                }
            )*/

            val dateFieldHandle = viewModel.getFieldHandle<LocalDate>(KEY_FORM_FIELD_DATE)
            val dateFieldState by viewModel.collectFieldState<LocalDate>(id = KEY_FORM_FIELD_DATE)

            FormDateField(
                state = dateFieldState,
                handle = dateFieldHandle,
                label = "Date*",
                dateFormatter = DateTimeFormatter.ISO_DATE,
                supportingText = "Please select a date from next week."
            )

            /*
            FormDateField(
                state = dateFieldState,
                label = "Date*",
                dateFormatter = DateTimeFormatter.ISO_DATE,
                onValueChange = { value ->
                    dateFieldHandle.onFieldValueChanged(value)
                }
            )
            FormDateField(
                value = dateFieldState.value,
                isError = dateFieldState.isError,
                errorMessage = dateFieldState.errorMessage(),
                label = "Date*",
                dateFormatter = DateTimeFormatter.ISO_DATE,
                onValueChange = { value ->
                    dateFieldHandle.onFieldValueChanged(value)
                }
            )*/

            val sizeFieldHandle = viewModel.getFieldHandle<Int>(KEY_FORM_FIELD_SIZE)
            val sizeFieldState by viewModel.collectFieldState<Int>(id = KEY_FORM_FIELD_SIZE)

            FormIntegerField(
                state = sizeFieldState,
                handle = sizeFieldHandle,
                label = "Size (optional field)",
                supportingText = "Please pick a value between 100 and 200."
            )

            /*
            FormIntegerField(
                state = sizeFieldState,
                label = "Size*",
                onValueChange = { value ->
                    sizeFieldHandle.onFieldValueChanged(value)
                },
                onFocusCleared = {
                    sizeFieldHandle.onFieldFocusCleared()
                }
            )
            FormIntegerField(
                value = sizeFieldState.value,
                isError = sizeFieldState.isError,
                errorMessage = sizeFieldState.errorMessage(),
                label = "Size*",
                onValueChange = { value ->
                    sizeFieldHandle.onFieldValueChanged(value)
                },
                onFocusCleared = {
                    sizeFieldHandle.onFieldFocusCleared()
                }
            )*/

            val allRequiredFieldFilled by viewModel.allRequiredFieldFilled.collectAsState()
            Button(
                enabled = allRequiredFieldFilled,
                onClick = viewModel::submit,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Submit")
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
