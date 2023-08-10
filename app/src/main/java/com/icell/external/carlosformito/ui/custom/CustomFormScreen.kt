package com.icell.external.carlosformito.ui.custom

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Card
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
import androidx.compose.ui.unit.dp
import com.icell.external.carlosformito.ui.common.CarlosTopAppBar
import com.icell.external.carlosformito.ui.common.SimpleSelectionBottomSheet
import com.icell.external.carlosformito.ui.custom.CustomFormFields.KEY_PAYMENT_METHOD_TYPE
import com.icell.external.carlosformito.ui.custom.CustomFormFields.KEY_QUANTITY
import com.icell.external.carlosformito.ui.custom.CustomFormFields.KEY_SAVE_PAYMENT_METHOD_CHECKED
import com.icell.external.carlosformito.ui.custom.CustomFormFields.KEY_VALIDITY_START
import com.icell.external.carlosformito.ui.custom.fields.FormQuantityField
import com.icell.external.carlosformito.ui.custom.fields.FormSelectionField
import com.icell.external.carlosformito.ui.custom.fields.FormSwitchField
import com.icell.external.carlosformito.ui.custom.fields.FormValidityStartField
import com.icell.external.carlosformito.ui.custom.fields.model.PaymentMethod
import com.icell.external.carlosformito.ui.util.extension.collectFieldState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
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
                Card(
                    elevation = 0.dp,
                    enabled = false,
                    onClick = {}
                ) {
                    Column {
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
                            Divider(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                color = MaterialTheme.colors.background
                            )
                            FormSwitchField(
                                text = "Save debit card",
                                fieldItem = viewModel.getFieldItem(KEY_SAVE_PAYMENT_METHOD_CHECKED),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }
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
