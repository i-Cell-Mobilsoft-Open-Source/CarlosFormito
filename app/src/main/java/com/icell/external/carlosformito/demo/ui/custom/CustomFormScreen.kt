package com.icell.external.carlosformito.demo.ui.custom

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.icell.external.carlosformito.commondemo.clearFocusOnTap
import com.icell.external.carlosformito.core.api.FormFieldItem
import com.icell.external.carlosformito.core.ui.collectFieldState
import com.icell.external.carlosformito.demo.ui.common.CarlosTopAppBar
import com.icell.external.carlosformito.demo.ui.common.SimpleSelectionBottomSheet
import com.icell.external.carlosformito.demo.ui.custom.CustomFormFields.KEY_DEBIT_CARD_NUMBER
import com.icell.external.carlosformito.demo.ui.custom.CustomFormFields.KEY_PAYMENT_METHOD_TYPE
import com.icell.external.carlosformito.demo.ui.custom.CustomFormFields.KEY_QUANTITY
import com.icell.external.carlosformito.demo.ui.custom.CustomFormFields.KEY_SAVE_PAYMENT_METHOD_CHECKED
import com.icell.external.carlosformito.demo.ui.custom.CustomFormFields.KEY_VALIDITY_START
import com.icell.external.carlosformito.demo.ui.custom.fields.FormQuantityField
import com.icell.external.carlosformito.demo.ui.custom.fields.FormSelectionField
import com.icell.external.carlosformito.demo.ui.custom.fields.FormSwitchField
import com.icell.external.carlosformito.demo.ui.custom.fields.FormValidityStartField
import com.icell.external.carlosformito.demo.ui.custom.fields.model.PaymentMethod
import com.icell.external.carlosformito.ui.field.FormTextField
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CustomFormScreen(
    title: String,
    viewModel: CustomFormViewModel,
    onBackPressed: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val modalSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmValueChange = { it != ModalBottomSheetValue.HalfExpanded },
        skipHalfExpanded = false
    )

    val focusManager = LocalFocusManager.current
    val paymentMethodItem = viewModel.getFieldItem<PaymentMethod>(KEY_PAYMENT_METHOD_TYPE)
    ModalBottomSheetLayout(
        sheetState = modalSheetState,
        sheetShape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
        sheetContent = {
            SimpleSelectionBottomSheet(
                items = PaymentMethod.entries,
                itemText = { _, item -> item.name },
                onItemSelected = { _, item ->
                    paymentMethodItem.onFieldValueChanged(item)
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
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Quantity*",
                    style = MaterialTheme.typography.subtitle2
                )
                Spacer(modifier = Modifier.height(8.dp))
                FormQuantityField(
                    fieldItem = viewModel.getFieldItem(KEY_QUANTITY),
                    minQuantity = 1,
                    maxQuantity = 5,
                    supportingText = "Maximum 5 tickets at once."
                )

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Validity start*",
                    style = MaterialTheme.typography.subtitle2
                )
                Spacer(modifier = Modifier.height(8.dp))
                FormValidityStartField(
                    fieldItem = viewModel.getFieldItem(KEY_VALIDITY_START),
                    supportingText = "Valid for travel at earliest 2 minutes after purchase."
                )

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Payment method*",
                    style = MaterialTheme.typography.subtitle2
                )

                Spacer(modifier = Modifier.height(8.dp))
                FormSelectionField(
                    fieldItem = paymentMethodItem,
                    onSelectValue = {
                        coroutineScope.launch {
                            modalSheetState.show()
                        }
                    },
                    displayedValue = { method ->
                        when (method) {
                            PaymentMethod.Balance -> "Balance"
                            PaymentMethod.DebitCard -> "Debit card"
                            PaymentMethod.SavedDebitCard -> "Saved debit card"
                            null -> "Please select payment method"
                        }
                    }
                )

                val selectedPaymentMethod by paymentMethodItem.collectFieldState()
                AnimatedVisibility(visible = selectedPaymentMethod.value == PaymentMethod.DebitCard) {
                    DebitCardForm(
                        viewModel.getFieldItem(KEY_DEBIT_CARD_NUMBER),
                        viewModel.getFieldItem(KEY_SAVE_PAYMENT_METHOD_CHECKED)
                    )
                }

                val allRequiredFieldFilled by viewModel.allRequiredFieldFilled.collectAsState()
                Button(
                    enabled = allRequiredFieldFilled,
                    onClick = viewModel::submit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp)
                ) {
                    Text("Submit")
                }
            }
        }
    }
}

@Composable
fun DebitCardForm(
    debitCardNumberField: FormFieldItem<String>,
    savePaymentMethodCheckedField: FormFieldItem<Boolean>
) {
    Column {
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.1f)
        )
        FormTextField(
            fieldItem = debitCardNumberField,
            label = {
                Text("Debit card number*")
            },
            maxLength = 32,
            supportingText = """
                This is a required field which is visible only when debit card option is selected.
            """.trimIndent()
        )
        FormSwitchField(
            text = "Save debit card",
            fieldItem = savePaymentMethodCheckedField,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
        )
    }
}
