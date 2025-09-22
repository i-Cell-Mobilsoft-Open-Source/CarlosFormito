package hu.icellmobilsoft.carlosformito.demo.ui.custom.fields

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import hu.icellmobilsoft.carlosformito.core.api.FormFieldItem
import hu.icellmobilsoft.carlosformito.core.ui.TrackVisibilityEffect
import hu.icellmobilsoft.carlosformito.core.ui.extensions.collectFieldState
import hu.icellmobilsoft.carlosformito.core.ui.extensions.errorMessage
import hu.icellmobilsoft.carlosformito.core.ui.testId
import hu.icellmobilsoft.carlosformito.ui.field.base.TextFieldSupportingText

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun <T> FormSelectionField(
    modifier: Modifier = Modifier,
    fieldItem: FormFieldItem<T>,
    enabled: Boolean = true,
    onSelectValue: () -> Unit,
    displayedValue: (T?) -> String,
    supportingText: AnnotatedString? = null
) {
    val state by fieldItem.collectFieldState()

    TrackVisibilityEffect { visible ->
        fieldItem.onFieldVisibilityChanged(visible)
    }

    Column(
        modifier = modifier
    ) {
        Card(
            elevation = 0.dp,
            enabled = enabled,
            onClick = onSelectValue,
            backgroundColor = MaterialTheme.colors.primary.copy(alpha = 0.06f)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = displayedValue(state.value),
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null)
            }
        }

        val displayedSupportingText = if (state.isError) {
            state.errorMessage() ?: supportingText
        } else {
            supportingText
        }

        AnimatedVisibility(visible = !displayedSupportingText.isNullOrBlank()) {
            TextFieldSupportingText(
                modifier = Modifier
                    .testId("text_supported")
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 12.dp)
                    .padding(horizontal = 16.dp),
                isError = state.isError,
                supportingText = displayedSupportingText ?: ""
            )
        }
    }
}
