package hu.icellmobilsoft.carlosformito.demo.ui.password

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import hu.icellmobilsoft.carlosformito.commondemo.clearFocusOnTap
import hu.icellmobilsoft.carlosformito.demo.ui.common.CarlosTopAppBar
import hu.icellmobilsoft.carlosformito.ui.field.FormPasswordTextField

@Composable
fun UpdatePasswordScreen(
    title: String,
    viewModel: UpdatePasswordViewModel,
    onBackPressed: () -> Unit
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
                text = "Update your password",
                style = MaterialTheme.typography.subtitle1
            )

            Spacer(modifier = Modifier.height(16.dp))

            FormPasswordTextField(
                fieldItem = viewModel.getFieldItem(UpdatePasswordFields.KEY_OLD_PASSWORD),
                label = {
                    Text("Old password*")
                },
                maxLength = UpdatePasswordFields.KEY_PASSWORD_MAX_LENGTH
            )

            FormPasswordTextField(
                fieldItem = viewModel.getFieldItem(UpdatePasswordFields.KEY_NEW_PASSWORD),
                label = {
                    Text("New password*")
                },
                maxLength = UpdatePasswordFields.KEY_PASSWORD_MAX_LENGTH
            )

            Text(
                text = """
                    •  Min 8, max 32 characters
                    •  Contains number
                    •  Contains special characters
                    •  Contains both uppercase and lowercase characters
                """.trimIndent(),
                style = MaterialTheme.typography.caption,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            )

            FormPasswordTextField(
                fieldItem = viewModel.getFieldItem(UpdatePasswordFields.KEY_CONFIRM_PASSWORD),
                label = {
                    Text("Confirm password*")
                },
                maxLength = UpdatePasswordFields.KEY_PASSWORD_MAX_LENGTH
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
