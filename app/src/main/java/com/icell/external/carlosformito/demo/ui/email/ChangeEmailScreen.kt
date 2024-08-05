package com.icell.external.carlosformito.demo.ui.email

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.icell.external.carlosformito.commondemo.clearFocusOnTap
import com.icell.external.carlosformito.demo.ui.common.CarlosTopAppBar
import com.icell.external.carlosformito.demo.ui.common.FullScreenProgressDialog
import com.icell.external.carlosformito.ui.field.FormTextField
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ChangeEmailScreen(
    title: String,
    viewModel: ChangeEmailViewModel,
    onBackPressed: () -> Unit
) {
    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(Unit) {
        viewModel.isError.collectLatest { throwable ->
            scaffoldState.snackbarHostState.showSnackbar(
                throwable.message ?: "Something went wrong"
            )
        }
    }

    val showLoadingIndicator by viewModel.showLoadingIndicator.collectAsState()
    if (showLoadingIndicator) {
        FullScreenProgressDialog()
    }

    Scaffold(
        topBar = {
            CarlosTopAppBar(title, onBackPressed)
        },
        scaffoldState = scaffoldState
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
                text = "Change your email",
                style = MaterialTheme.typography.subtitle1
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Your current email address:",
                style = MaterialTheme.typography.caption
            )
            Spacer(modifier = Modifier.height(4.dp))

            val currentEmail by viewModel.currentEmail.collectAsState()
            val dataIsLoading by viewModel.dataIsLoading.collectAsState()
            Text(
                text = currentEmail ?: "Loading...",
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.onSurface.copy(
                    alpha = if (dataIsLoading) .3f else 1f
                )
            )

            Text(
                """
                    We will send you a verification email to the provided address.
                """.trimIndent(),
                style = MaterialTheme.typography.caption,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )

            val sameOldAndNewEmail by viewModel.sameOldAndNewEmail.collectAsState()
            FormTextField(
                fieldItem = viewModel.getFieldItem(ChangeEmailFields.KEY_NEW_EMAIL),
                customErrorMessage = if (sameOldAndNewEmail) {
                    "Same old and new email!"
                } else {
                    null
                },
                label = "New email*",
                maxLength = 32
            )

            val submitButtonEnabled by viewModel.submitButtonEnabled.collectAsState()
            Button(
                enabled = submitButtonEnabled,
                onClick = viewModel::submit,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Submit")
            }
        }
    }
}
