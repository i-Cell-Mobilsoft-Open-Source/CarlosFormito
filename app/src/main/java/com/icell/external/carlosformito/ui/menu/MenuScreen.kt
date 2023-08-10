package com.icell.external.carlosformito.ui.menu

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MenuScreen(
    onNavigateToFieldSamples: () -> Unit,
    onNavigateToCustomFormFieldsSample: () -> Unit,
    onNavigateToLongRunningValidationSample: () -> Unit,
) {
    Scaffold {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp)
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
                    color = contentColorFor(MaterialTheme.colors.surface).copy(alpha = ContentAlpha.medium),
                    lineHeight = 22.sp
                )
            )

            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(vertical = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                MenuListItem(title = "Built-in field samples") {
                    onNavigateToFieldSamples.invoke()
                }
                MenuListItem(title = "Custom field samples") {
                    onNavigateToCustomFormFieldsSample.invoke()
                }
                MenuListItem(title = "Long running validation sample") {
                    onNavigateToLongRunningValidationSample.invoke()
                }
            }
        }
    }
}
