package com.icell.external.carlosformito.demo.ui.menu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ContentAlpha
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.contentColorFor
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.icell.external.carlosformito.core.api.model.FormFieldValidationStrategy
import com.icell.external.carlosformito.demo.ui.common.SimpleSelectionBottomSheet
import com.icell.external.carlosformito.ui.extension.collectFieldState
import com.icell.external.carlosformito.ui.field.FormPickerField
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MenuScreen(
    viewModel: MenuViewModel,
    onNavigateToFieldSamples: (validationStrategy: FormFieldValidationStrategy) -> Unit,
    onNavigateToCustomFormFieldsSample: (validationStrategy: FormFieldValidationStrategy) -> Unit,
    onNavigateToLongRunningValidationSample: (validationStrategy: FormFieldValidationStrategy) -> Unit,
    onNavigateToMatchValidationSample: (validationStrategy: FormFieldValidationStrategy) -> Unit,
    onNavigateToConnectedFieldValidationSample: (validationStrategy: FormFieldValidationStrategy) -> Unit
) {
    val validationStrategyField =
        viewModel.getFieldItem<FormFieldValidationStrategy>(KEY_VALIDATION_STRATEGY_FIELD)
    val validationStrategyState by validationStrategyField.collectFieldState()

    val coroutineScope = rememberCoroutineScope()
    val modalSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmValueChange = { it != ModalBottomSheetValue.HalfExpanded },
        skipHalfExpanded = false
    )
    ModalBottomSheetLayout(
        sheetState = modalSheetState,
        sheetShape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
        sheetContent = {
            SimpleSelectionBottomSheet(
                items = FormFieldValidationStrategy.entries,
                itemText = { _, item -> item.displayedValue() },
                onItemSelected = { _, item ->
                    validationStrategyField.onFieldValueChanged(item)
                    coroutineScope.launch {
                        modalSheetState.hide()
                    }
                }
            )
        }
    ) {
        Scaffold { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(innerPadding)
                    .padding(horizontal = 24.dp)
            ) {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Welcome to Carlos Formito!",
                    style = MaterialTheme.typography.h5.copy(
                        fontWeight = FontWeight.Black
                    )
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Explore our form state management solution for Jetpack Compose in action. " +
                        "Here are some use cases and examples:",
                    style = MaterialTheme.typography.body2.copy(
                        color = contentColorFor(MaterialTheme.colors.surface)
                            .copy(alpha = ContentAlpha.medium),
                        lineHeight = 22.sp
                    )
                )

                FormPickerField(
                    modifier = Modifier.padding(top = 24.dp),
                    fieldItem = validationStrategyField,
                    label = "Field validation strategy",
                    onClick = {
                        coroutineScope.launch {
                            modalSheetState.show()
                        }
                    },
                    displayedValue = { validationStrategy ->
                        validationStrategy?.displayedValue() ?: ""
                    },
                    isClearable = false,
                    supportingText = validationStrategyState.value?.description()
                )

                Column(
                    modifier = Modifier.padding(vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    MenuListItem(title = "Built-in field samples") {
                        onNavigateToFieldSamples.invoke(
                            validationStrategyState.value ?: FormFieldValidationStrategy.MANUAL
                        )
                    }
                    MenuListItem(title = "Custom field samples") {
                        onNavigateToCustomFormFieldsSample.invoke(
                            validationStrategyState.value ?: FormFieldValidationStrategy.MANUAL
                        )
                    }
                    MenuListItem(title = "Long running validation sample") {
                        onNavigateToLongRunningValidationSample.invoke(
                            validationStrategyState.value ?: FormFieldValidationStrategy.MANUAL
                        )
                    }
                    MenuListItem(title = "EqualsTo validation sample") {
                        onNavigateToMatchValidationSample.invoke(
                            validationStrategyState.value ?: FormFieldValidationStrategy.MANUAL
                        )
                    }
                    MenuListItem(title = "Connected fields validation sample") {
                        onNavigateToConnectedFieldValidationSample.invoke(
                            validationStrategyState.value ?: FormFieldValidationStrategy.MANUAL
                        )
                    }
                }
            }
        }
    }
}

private fun FormFieldValidationStrategy.displayedValue(): String {
    return when (this) {
        FormFieldValidationStrategy.MANUAL -> "Manual"
        FormFieldValidationStrategy.AUTO_ON_FOCUS_CLEAR -> "Automatic on focus clear"
        FormFieldValidationStrategy.AUTO_INLINE -> "Automatic inline"
    }
}

private fun FormFieldValidationStrategy.description(): String {
    return when (this) {
        FormFieldValidationStrategy.MANUAL -> """
            The validation of the form is executed manually and performed in order from top to bottom consecutively.
        """.trimIndent()

        FormFieldValidationStrategy.AUTO_ON_FOCUS_CLEAR -> """
            The validation of each individual form field is executed automatically by focus clear event of the particular field.
        """.trimIndent()

        FormFieldValidationStrategy.AUTO_INLINE -> """
            The validation of each individual form field is executed automatically by any value change event of the particular field.
        """.trimIndent()
    }
}
