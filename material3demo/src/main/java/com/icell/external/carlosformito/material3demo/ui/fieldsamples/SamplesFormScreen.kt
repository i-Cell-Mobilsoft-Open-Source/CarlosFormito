package com.icell.external.carlosformito.material3demo.ui.fieldsamples

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.icell.external.carlosformito.commondemo.clearFocusOnTap
import com.icell.external.carlosformito.material3demo.ui.common.CarlosTopAppBar
import com.icell.external.carlosformito.material3demo.ui.common.SimpleSelectionBottomSheet
import com.icell.external.carlosformito.material3demo.ui.fieldsamples.SamplesFormFields.KEY_FORM_FIELD_DATE
import com.icell.external.carlosformito.material3demo.ui.fieldsamples.SamplesFormFields.KEY_FORM_FIELD_NAME
import com.icell.external.carlosformito.material3demo.ui.fieldsamples.SamplesFormFields.KEY_FORM_FIELD_PACKAGE
import com.icell.external.carlosformito.material3demo.ui.fieldsamples.SamplesFormFields.KEY_FORM_FIELD_SECRET
import com.icell.external.carlosformito.material3demo.ui.fieldsamples.SamplesFormFields.KEY_FORM_FIELD_SIZE
import com.icell.external.carlosformito.material3demo.ui.fieldsamples.SamplesFormFields.KEY_FORM_FIELD_TIME
import com.icell.external.carlosformito.material3demo.ui.fieldsamples.model.PackageType
import com.icell.external.carlosformito.ui.field.FormDatePickerField
import com.icell.external.carlosformito.ui.field.FormIntegerField
import com.icell.external.carlosformito.ui.field.FormPasswordTextField
import com.icell.external.carlosformito.ui.field.FormPickerField
import com.icell.external.carlosformito.ui.field.FormTextField
import com.icell.external.carlosformito.ui.field.FormTimePickerField
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SampleFormScreen(
    title: String,
    viewModel: SamplesFormViewModel,
    onBackPressed: () -> Unit
) {
    val scope = rememberCoroutineScope()
    var openBottomSheet by rememberSaveable { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

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

    val packageFieldItem = viewModel.getFieldItem<PackageType>(KEY_FORM_FIELD_PACKAGE)

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
                .padding(horizontal = 16.dp)
                .padding(innerPadding)
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
                onClick = { openBottomSheet = true },
                displayedValue = { packageType -> packageType?.displayedValue() ?: "" }
            )

            val allRequiredFieldFilled by viewModel.allRequiredFieldFilled.collectAsState()
            Button(
                enabled = allRequiredFieldFilled,
                onClick = viewModel::submit,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text("Submit")
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    if (openBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { openBottomSheet = false },
            sheetState = bottomSheetState,
            windowInsets = BottomSheetDefaults.windowInsets
        ) {
            SimpleSelectionBottomSheet(
                items = PackageType.entries,
                itemText = { _, item -> item.displayedValue() },
                onItemSelected = { _, item ->
                    packageFieldItem.onFieldValueChanged(item)

                    scope.launch { bottomSheetState.hide() }.invokeOnCompletion {
                        if (!bottomSheetState.isVisible) {
                            openBottomSheet = false
                        }
                    }
                }
            )
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
