package com.icell.external.carlosformito.demo.ui.fieldsamples

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.icell.external.carlosformito.commondemo.clearFocusOnTap
import com.icell.external.carlosformito.demo.ui.common.CarlosTopAppBar
import com.icell.external.carlosformito.demo.ui.common.SimpleSelectionBottomSheet
import com.icell.external.carlosformito.demo.ui.fieldsamples.SamplesFormFields.KEY_FORM_FIELD_DATE
import com.icell.external.carlosformito.demo.ui.fieldsamples.SamplesFormFields.KEY_FORM_FIELD_NAME
import com.icell.external.carlosformito.demo.ui.fieldsamples.SamplesFormFields.KEY_FORM_FIELD_PACKAGE
import com.icell.external.carlosformito.demo.ui.fieldsamples.SamplesFormFields.KEY_FORM_FIELD_SECRET
import com.icell.external.carlosformito.demo.ui.fieldsamples.SamplesFormFields.KEY_FORM_FIELD_SIZE
import com.icell.external.carlosformito.demo.ui.fieldsamples.SamplesFormFields.KEY_FORM_FIELD_TIME
import com.icell.external.carlosformito.demo.ui.fieldsamples.model.PackageType
import com.icell.external.carlosformito.ui.field.FormDatePickerField
import com.icell.external.carlosformito.ui.field.FormIntegerField
import com.icell.external.carlosformito.ui.field.FormPasswordTextField
import com.icell.external.carlosformito.ui.field.FormPickerField
import com.icell.external.carlosformito.ui.field.FormTextField
import com.icell.external.carlosformito.ui.field.FormTimePickerField
import kotlinx.coroutines.launch

@Suppress("LongMethod")
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

    var clearFormConfirmDialogVisible by remember { mutableStateOf(false) }
    if (clearFormConfirmDialogVisible) {
        AlertDialog(
            onDismissRequest = { clearFormConfirmDialogVisible = false },
            text = {
                Text("Are you sure you want to clear the form?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.clearForm()
                        clearFormConfirmDialogVisible = false
                    }
                ) {
                    Text("Yes")
                }
            }
        )
    }

    val focusManager = LocalFocusManager.current
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
                        focusManager.clearFocus()
                    }
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                CarlosTopAppBar(
                    title = title,
                    onNavigationIconPressed = onBackPressed,
                    actions = {
                        IconButton(
                            onClick = {
                                clearFormConfirmDialogVisible = true
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Clear form"
                            )
                        }
                    }
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .clearFocusOnTap()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                FormTextField(
                    fieldItem = viewModel.getFieldItem(KEY_FORM_FIELD_NAME),
                    label = {
                        Text("Name*")
                    },
                    supportingText = "Please enter your full name."
                )

                FormPasswordTextField(
                    fieldItem = viewModel.getFieldItem(KEY_FORM_FIELD_SECRET),
                    label = {
                        Text("Secret*")
                    },
                    supportingText = "Please give a strong password (min 8 characters)."
                )

                FormDatePickerField(
                    fieldItem = viewModel.getFieldItem(KEY_FORM_FIELD_DATE),
                    label = {
                        Text("Date*")
                    },
                    dialogTitle = "Select date",
                    supportingText = "Please select a date from next week."
                )

                FormTimePickerField(
                    fieldItem = viewModel.getFieldItem(KEY_FORM_FIELD_TIME),
                    label = {
                        Text("Time*")
                    },
                    dialogTitle = "Select time",
                    supportingText = "Please choose a time between noon and 6 p.m."
                )

                FormIntegerField(
                    fieldItem = viewModel.getFieldItem(KEY_FORM_FIELD_SIZE),
                    label = {
                        Text("Size (optional field)")
                    },
                    supportingText = "Please enter a value between 100 and 200."
                )

                FormPickerField(
                    fieldItem = packageFieldItem,
                    label = {
                        Text("Package type*")
                    },
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
