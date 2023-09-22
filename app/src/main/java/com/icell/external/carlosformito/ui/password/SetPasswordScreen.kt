package com.icell.external.carlosformito.ui.password

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.icell.external.carlosformito.R
import com.icell.external.carlosformito.commonui.extension.collectFieldState
import com.icell.external.carlosformito.commonui.extension.errorMessage
import com.icell.external.carlosformito.ui.common.CarlosTopAppBar
import com.icell.external.carlosformito.ui.field.FormPasswordTextField
import com.icell.external.carlosformito.ui.password.SetPasswordFields.KEY_CONFIRM_PASSWORD
import com.icell.external.carlosformito.ui.password.SetPasswordFields.KEY_PASSWORD
import com.icell.external.carlosformito.ui.password.SetPasswordFields.KEY_PASSWORD_MAX_LENGTH

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SetPasswordScreen(
    title: String,
    viewModel: SetPasswordViewModel,
    onBackPressed: () -> Unit
) {
    Scaffold(
        topBar = {
            CarlosTopAppBar(title, onBackPressed)
        }
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 24.dp)
        ) {
            Text(
                text = "Set your password",
                style = MaterialTheme.typography.subtitle1
            )

            Spacer(modifier = Modifier.height(16.dp))

            FormPasswordTextField(
                fieldItem = viewModel.getFieldItem(KEY_PASSWORD),
                label = "Password*",
                maxLength = KEY_PASSWORD_MAX_LENGTH,
                supportingText = """
                    •  Min 8, max 32 characters
                    •  Contains number
                    •  Contains special characters
                    •  Contains both uppercase and lowercase characters
                """.trimIndent()
            )

            val confirmPasswordItem = viewModel.getFieldItem<String>(KEY_CONFIRM_PASSWORD)
            val confirmPasswordState by confirmPasswordItem.collectFieldState()
            val passwordMatchError by viewModel.passwordMatchError.collectAsState()

            FormPasswordTextField(
                value = confirmPasswordState.value,
                isError = confirmPasswordState.isError || passwordMatchError,
                errorMessage = if (passwordMatchError) {
                    stringResource(id = R.string.confirm_password_not_match_error)
                } else {
                    confirmPasswordState.errorMessage()
                },
                label = "Confirm password*",
                maxLength = KEY_PASSWORD_MAX_LENGTH,
                onValueChange = { value ->
                    confirmPasswordItem.onFieldValueChanged(value)
                },
                onFocusCleared = {
                    confirmPasswordItem.onFieldFocusCleared()
                }
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
