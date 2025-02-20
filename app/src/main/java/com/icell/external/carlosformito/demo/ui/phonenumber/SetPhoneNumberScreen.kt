package com.icell.external.carlosformito.demo.ui.phonenumber

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.icell.external.carlosformito.commondemo.clearFocusOnTap
import com.icell.external.carlosformito.demo.ui.common.CarlosTopAppBar
import com.icell.external.carlosformito.demo.ui.common.SimpleSelectionBottomSheet
import com.icell.external.carlosformito.demo.ui.phonenumber.SetPhoneNumberFields.KEY_COUNTRY_CODE
import com.icell.external.carlosformito.demo.ui.phonenumber.SetPhoneNumberFields.KEY_LOCAL_NUMBER
import com.icell.external.carlosformito.demo.ui.phonenumber.model.Country
import com.icell.external.carlosformito.ui.field.FormPickerField
import com.icell.external.carlosformito.ui.field.FormTextField
import kotlinx.coroutines.launch

@Composable
fun SetPhoneNumberScreen(
    title: String,
    viewModel: SetPhoneNumberViewModel,
    onBackPressed: () -> Unit
) {
    val context = LocalContext.current

    val coroutineScope = rememberCoroutineScope()
    val modalSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmValueChange = { it != ModalBottomSheetValue.HalfExpanded },
        skipHalfExpanded = false
    )

    val focusManager = LocalFocusManager.current
    val countryItem = viewModel.getFieldItem<Country>(KEY_COUNTRY_CODE)
    ModalBottomSheetLayout(
        sheetState = modalSheetState,
        sheetShape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
        sheetContent = {
            SimpleSelectionBottomSheet(
                items = Country.entries,
                itemText = { _, item -> context.getString(item.selectionTextRes) },
                onItemSelected = { _, item ->
                    countryItem.onFieldValueChanged(item)
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
                CarlosTopAppBar(title, onBackPressed)
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
                Text(
                    text = "Set your phone number",
                    style = MaterialTheme.typography.subtitle1
                )

                FormPickerField(
                    fieldItem = countryItem,
                    label = {
                        Text("Country*")
                    },
                    onClick = {
                        coroutineScope.launch {
                            modalSheetState.show()
                        }
                    },
                    isClearable = false,
                    displayedValue = { country ->
                        country?.let {
                            context.getString(country.selectionTextRes)
                        } ?: "-"
                    },
                    modifier = Modifier.padding(top = 16.dp)
                )

                FormTextField(
                    fieldItem = viewModel.getFieldItem(KEY_LOCAL_NUMBER),
                    label = {
                        Text("Phone number*")
                    },
                    maxLength = SetPhoneNumberFields.PHONE_NUMBER_MAX_LENGTH,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                )

                val allRequiredFieldFilled by viewModel.allRequiredFieldFilled.collectAsState()
                Button(
                    enabled = allRequiredFieldFilled,
                    onClick = viewModel::submit,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Submit")
                }
            }
        }
    }
}
