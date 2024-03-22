package com.icell.external.carlosformito.material3demo.ui.menu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.icell.external.carlosformito.core.api.model.FormFieldValidationStrategy
import com.icell.external.carlosformito.material3demo.ui.common.SimpleSelectionBottomSheet
import com.icell.external.carlosformito.ui.extension.collectFieldState
import com.icell.external.carlosformito.ui.field.FormPickerField
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    viewModel: MenuViewModel,
    onNavigateToFieldsSample: (validationStrategy: FormFieldValidationStrategy) -> Unit,
) {
    val validationStrategyField =
        viewModel.getFieldItem<FormFieldValidationStrategy>(KEY_VALIDATION_STRATEGY_FIELD)
    val validationStrategyState by validationStrategyField.collectFieldState()

    val scope = rememberCoroutineScope()
    var openBottomSheet by rememberSaveable { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Welcome to Carlos Formito!",
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = "Explore our form state management solution for Jetpack Compose in action. " +
                    "Here are some use cases and examples:",
                style = MaterialTheme.typography.bodySmall.copy(lineHeight = 22.sp)
            )

            FormPickerField(
                modifier = Modifier.padding(top = 24.dp),
                fieldItem = validationStrategyField,
                label = "Field validation strategy",
                onClick = { openBottomSheet = true },
                displayedValue = { validationStrategy ->
                    validationStrategy?.displayedValue() ?: ""
                },
                isClearable = false,
                supportingText = validationStrategyState.value?.description()
            )

            Column(
                modifier = Modifier.padding(vertical = 16.dp)
            ) {
                MenuListItem(title = "Built-in field samples") {
                    onNavigateToFieldsSample.invoke(
                        validationStrategyState.value ?: FormFieldValidationStrategy.MANUAL
                    )
                }
            }
        }
    }

    if (openBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { openBottomSheet = false },
            sheetState = bottomSheetState
        ) {
            SimpleSelectionBottomSheet(
                items = FormFieldValidationStrategy.entries,
                itemText = { _, item -> item.displayedValue() },
                onItemSelected = { _, item ->
                    validationStrategyField.onFieldValueChanged(item)

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
