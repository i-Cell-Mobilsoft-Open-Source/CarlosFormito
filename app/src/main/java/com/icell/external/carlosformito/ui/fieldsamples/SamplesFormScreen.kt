package com.icell.external.carlosformito.ui.fieldsamples

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.icell.external.carlosformito.ui.common.CarlosTopAppBar
import com.icell.external.carlosformito.ui.common.SimpleSelectionBottomSheet
import com.icell.external.carlosformito.ui.field.FormDatePickerField
import com.icell.external.carlosformito.ui.field.FormIntegerField
import com.icell.external.carlosformito.ui.field.FormPasswordTextField
import com.icell.external.carlosformito.ui.field.FormPickerField
import com.icell.external.carlosformito.ui.field.FormTextField
import com.icell.external.carlosformito.ui.field.FormTimePickerField
import com.icell.external.carlosformito.ui.fieldsamples.SamplesFormFields.KEY_FORM_FIELD_DATE
import com.icell.external.carlosformito.ui.fieldsamples.SamplesFormFields.KEY_FORM_FIELD_NAME
import com.icell.external.carlosformito.ui.fieldsamples.SamplesFormFields.KEY_FORM_FIELD_PACKAGE
import com.icell.external.carlosformito.ui.fieldsamples.SamplesFormFields.KEY_FORM_FIELD_SECRET
import com.icell.external.carlosformito.ui.fieldsamples.SamplesFormFields.KEY_FORM_FIELD_SIZE
import com.icell.external.carlosformito.ui.fieldsamples.SamplesFormFields.KEY_FORM_FIELD_TIME
import com.icell.external.carlosformito.ui.fieldsamples.model.PackageType
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SampleFormScreen(
    title: String,
    viewModel: SamplesFormViewModel,
    onBackPressed: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val modalSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmValueChange = { it != ModalBottomSheetValue.HalfExpanded },
        skipHalfExpanded = false
    )

    val packageFieldItem = viewModel.getFieldItem<PackageType>(KEY_FORM_FIELD_PACKAGE)
    ModalBottomSheetLayout(
        sheetState = modalSheetState,
        sheetShape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
        sheetContent = {
            SimpleSelectionBottomSheet(
                items = PackageType.entries,
                itemText = { _, item -> item.displayedValue() },
                onItemSelected = { _, item ->
                    packageFieldItem.onFieldValueChanged(item)
                    coroutineScope.launch {
                        modalSheetState.hide()
                    }
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                CarlosTopAppBar(title, onBackPressed)
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
                    supportingText = "Please enter your full name."
                )

                FormPasswordTextField(
                    fieldItem = viewModel.getFieldItem(KEY_FORM_FIELD_SECRET),
                    label = "Secret*",
                    supportingText = "Please give a strong password (min 8 characters)."
                )

                FormDatePickerField(
                    fieldItem = viewModel.getFieldItem(KEY_FORM_FIELD_DATE),
                    label = "Date*",
                    supportingText = "Please select a date from next week."
                )

                FormTimePickerField(
                    fieldItem = viewModel.getFieldItem(KEY_FORM_FIELD_TIME),
                    label = "Time*",
                    supportingText = "Please choose a time between noon and 6 p.m."
                )

                FormIntegerField(
                    fieldItem = viewModel.getFieldItem(KEY_FORM_FIELD_SIZE),
                    label = "Size (optional field)",
                    supportingText = "Please enter a value between 100 and 200."
                )

                FormPickerField(
                    fieldItem = packageFieldItem,
                    label = "Package type*",
                    supportingText = "Please pick a Package type.",
                    onClick = {
                        coroutineScope.launch {
                            modalSheetState.show()
                        }
                    },
                    displayedValue = { packageType -> packageType?.displayedValue() ?: "" }
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
}

private fun PackageType.displayedValue(): String {
    return when (this) {
        PackageType.XS -> "Extra small (XS)"
        PackageType.S -> "Small (S)"
        PackageType.M -> "Medium (M)"
        PackageType.L -> "Large (L)"
        PackageType.XL -> "Extra large (XL)"
    }
}
