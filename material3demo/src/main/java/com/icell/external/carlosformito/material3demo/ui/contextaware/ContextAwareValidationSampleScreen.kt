package com.icell.external.carlosformito.material3demo.ui.contextaware

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
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
import com.icell.external.carlosformito.commondemo.clearFocusOnTap
import com.icell.external.carlosformito.material3demo.ui.common.CarlosTopAppBar
import com.icell.external.carlosformito.material3demo.ui.common.SimpleSelectionBottomSheet
import com.icell.external.carlosformito.material3demo.ui.contextaware.model.AppleType
import com.icell.external.carlosformito.material3demo.ui.contextaware.model.SizeType
import com.icell.external.carlosformito.ui.field.FormIntegerField
import com.icell.external.carlosformito.ui.field.FormPickerField
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContextAwareValidationSampleScreen(
    title: String,
    viewModel: ContextAwareValidationViewModel,
    onBackPressed: () -> Unit
) {
    val scope = rememberCoroutineScope()
    var bottomSheetType by rememberSaveable { mutableStateOf<SheetType?>(null) }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val appleTypeFieldItem = viewModel.getFieldItem<AppleType>(ContextAwareFormFields.KEY_APPLE_TYPE)
    val sizeTypeFieldItem = viewModel.getFieldItem<SizeType>(ContextAwareFormFields.KEY_SIZE_TYPE)

    Scaffold(
        topBar = {
            CarlosTopAppBar(
                title = title,
                onNavigationIconPressed = onBackPressed,
                actions = {}
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
            Text(
                text = "Note: stock may vary depending on apple type and size.",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(vertical = 24.dp)
            )

            FormPickerField(
                fieldItem = appleTypeFieldItem,
                onClick = {
                    bottomSheetType = SheetType.SelectAppleType
                },
                displayedValue = { value -> value?.name ?: "-" },
                isClearable = false,
                label = {
                    Text("Apple type")
                }
            )

            FormPickerField(
                fieldItem = sizeTypeFieldItem,
                onClick = {
                    bottomSheetType = SheetType.SelectSizeType
                },
                displayedValue = { value -> value?.name ?: "-" },
                isClearable = false,
                label = {
                    Text("Size type")
                }
            )

            FormIntegerField(
                fieldItem = viewModel.getFieldItem(ContextAwareFormFields.KEY_QUANTITY),
                label = {
                    Text("Quantity")
                }
            )

            Button(
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

    fun hideSheet() {
        scope.launch { bottomSheetState.hide() }.invokeOnCompletion {
            if (!bottomSheetState.isVisible) {
                bottomSheetType = null
            }
        }
    }

    if (bottomSheetType != null) {
        ModalBottomSheet(
            onDismissRequest = { bottomSheetType = null },
            sheetState = bottomSheetState
        ) {
            when (bottomSheetType) {
                SheetType.SelectAppleType -> {
                    SimpleSelectionBottomSheet(
                        items = AppleType.entries,
                        itemText = { _, item -> item.name },
                        onItemSelected = { _, item ->
                            appleTypeFieldItem.onFieldValueChanged(item)
                            hideSheet()
                        }
                    )
                }

                SheetType.SelectSizeType -> {
                    SimpleSelectionBottomSheet(
                        items = SizeType.entries,
                        itemText = { _, item -> item.name },
                        onItemSelected = { _, item ->
                            sizeTypeFieldItem.onFieldValueChanged(item)
                            hideSheet()
                        }
                    )
                }

                null -> Unit
            }
        }
    }
}

enum class SheetType {
    SelectAppleType,
    SelectSizeType
}
