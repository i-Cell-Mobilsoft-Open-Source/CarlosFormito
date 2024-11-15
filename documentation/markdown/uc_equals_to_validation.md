# USE CASE 4 - EqualsTo validation

In this example, we’ll walk through a typical use case: implementing a Change Password screen.
For security, we’ll ask the user to provide their current password and a new password.
To avoid mistakes, we'll also include a Confirm Password field to ensure the new password is correctly entered.

### Requirements:
* The old password and the new password must be different.
* The new password should meet certain criteria (e.g., minimum length, containing a number, a special character, and both uppercase and lowercase characters).
* The Confirm Password field should match the new password.

To achieve this validation behavior, we’ll use the built-in connection validators: `EqualsToValidator` and `NotEqualsToValidator` (read more [here](https://github.com/icellmobilsoft/CarlosFormito/blob/master/README.md#built-in-connection-validators)).

<img src="https://github.com/icellmobilsoft/CarlosFormito/blob/master/documentation/images/carlos_equals_to_validation_sample.png" height="500" alt="Cross field validation sample 1"/> | 
<img src="https://github.com/icellmobilsoft/CarlosFormito/blob/master/documentation/images/carlos_equals_to_validation_sample_old_new.png" height="500" alt="Cross field validation sample 2"/> | 
<img src="https://github.com/icellmobilsoft/CarlosFormito/blob/master/documentation/images/carlos_equals_to_validation_sample_confirm.png" height="500" alt="Cross field validation sample 3"/>

```kotlin
// Define the form fields and it's validators
val fields = listOf(
    FormField(
        id = KEY_OLD_PASSWORD,
        validators = listOf(
            ValueRequiredValidator(R.string.value_required_error)
        )
    ),
    FormField(
        id = KEY_NEW_PASSWORD,
        validators = listOf(
            ValueRequiredValidator(R.string.value_required_error),
            // Validators for password criteria
            TextMinLengthValidator(minLength = KEY_PASSWORD_MIN_LENGTH, R.string.min_length_error),
            ContainsNumberValidator(R.string.password_not_contains_number_error),
            ContainsSpecialCharacterValidator(R.string.password_not_contains_special_char_error),
            ContainsUpperAndLowercaseValidator(R.string.password_not_contains_uppercase_or_lowercase_error),
            // Defining a validation connection to the old password field by passing the KEY_OLD_PASSWORD field id
            NotEqualsToValidator(KEY_OLD_PASSWORD, R.string.new_password_equals_old_password_error)
        )
    ),
    FormField(
        id = KEY_CONFIRM_PASSWORD,
        validators = listOf(
            ValueRequiredValidator(R.string.value_required_error),
            // Defining a validation connection to the new password field by passing the KEY_NEW_PASSWORD field id
            EqualsToValidator(KEY_NEW_PASSWORD, R.string.confirm_password_not_match_error)
        )
    )
)

// Initialize the form manager with the fields
val formManager = CarlosFormManager(formFields)

@Composable
fun ChangePasswordScreen(formManager: FormManager) {
    val scope = rememberCoroutineScope()

    // Initialize the form manager
    LaunchedEffect(Unit) {
        formManager.initFormManager()
    }
    
    Column {

        // Bind the FieldItem to the FormField composable
        FormPasswordTextField(
            fieldItem = viewModel.getFieldItem(KEY_OLD_PASSWORD),
            label = {
                Text("Old password*")
            },
            maxLength = KEY_PASSWORD_MAX_LENGTH
        )

        FormPasswordTextField(
            fieldItem = viewModel.getFieldItem(KEY_NEW_PASSWORD),
            label = {
                Text("New password*")
            },
            maxLength = KEY_PASSWORD_MAX_LENGTH
        )

        FormPasswordTextField(
            fieldItem = viewModel.getFieldItem(KEY_CONFIRM_PASSWORD),
            label = {
                Text("Confirm password*")
            },
            maxLength = KEY_PASSWORD_MAX_LENGTH
        )

        val allRequiredFieldFilled by formManager.allRequiredFieldFilled.collectAsState()
        Button(
            enabled = allRequiredFieldFilled,
            onClick = {
                // Validate the form when clicking the submit button
                scope.launch {
                    formManager.validateForm()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Submit")
        }
    }
}

```
