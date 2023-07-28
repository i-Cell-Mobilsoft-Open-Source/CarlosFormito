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
import com.icell.external.carlosformito.ui.fieldsamples.SamplesFormFields.KEY_FORM_FIELD_DATE
import com.icell.external.carlosformito.ui.fieldsamples.SamplesFormFields.KEY_FORM_FIELD_NAME
import com.icell.external.carlosformito.ui.fieldsamples.SamplesFormFields.KEY_FORM_FIELD_SECRET
import com.icell.external.carlosformito.ui.fieldsamples.SamplesFormFields.KEY_FORM_FIELD_SIZE
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

            FormTextField(
                fieldItem = viewModel.getFieldItem(KEY_FORM_FIELD_NAME),
                label = "Name*",
                supportingText = """
                    Please enter your full name.
                    Provided name will be seen by other users (min. 3 characters).
                """.trimIndent()
            )

            FormPasswordTextField(
                fieldItem = viewModel.getFieldItem(KEY_FORM_FIELD_SECRET),
                label = "Secret*",
                supportingText = "Please give a strong password (min 8 characters)."
            )

            FormDateField(
                fieldItem = viewModel.getFieldItem(KEY_FORM_FIELD_DATE),
                label = "Date*",
                dateFormatter = DateTimeFormatter.ISO_DATE,
                supportingText = "Please select a date from next week."
            )

            FormIntegerField(
                fieldItem = viewModel.getFieldItem(KEY_FORM_FIELD_SIZE),
                label = "Size (optional field)",
                supportingText = "Please pick a value between 100 and 200."
            )

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
