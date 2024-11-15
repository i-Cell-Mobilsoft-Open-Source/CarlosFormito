# USE CASE 3 - Long running validations

In this example, we’ll walk through a typical use case: implementing a Change Email screen.

### Things covered:
1. Email Format Validation: First, we validate whether the entered text is a valid email address.
2. Availability Check: After the user enters a new email and clicks the Submit button, we verify remotely if the email address is available.
3. Loading Indicator: While the remote check is in progress, a loading indicator should be displayed to inform the user.

The `FormFieldValidator`'s validate function is a suspend function, allowing for long-running asynchronous validations.
We can utilize the `validationInProgress` Boolean state flow provided by the `FormManager` to monitor ongoing validation progress.

<img src="https://github.com/icellmobilsoft/CarlosFormito/blob/master/documentation/images/carlos_long_running_validation_sample.png" height="500" alt="Long running validation sample 1"/> | 
<img src="https://github.com/icellmobilsoft/CarlosFormito/blob/master/documentation/images/carlos_long_running_validation_sample_loading.png" height="500" alt="Long running validation sample 2"/> |
<img src="https://github.com/icellmobilsoft/CarlosFormito/blob/master/documentation/images/carlos_long_running_validation_sample_result.png" height="500" alt="Long running validation sample 3"/>

```kotlin
// First create a custom validator for validating email address availability
class FakeEmailAvailableValidator : FormFieldValidator<String> {

    // Mocked unavailable emails for demo purposes
    private val unavailableEmails = listOf(
        "user123@gmail.com",
        "test.user@gmail.com",
        "user.test@gmail.com"
    )

    override suspend fun validate(value: String?): FormFieldValidationResult {
        delay(2.seconds) // Fake network request delay

        return if (unavailableEmails.contains(value.orEmpty())) {
            FormFieldValidationResult.Invalid.Message(R.string.email_not_unique_error)
        } else {
            FormFieldValidationResult.Valid
        }
    }
}

// Define the form fields and it's validators
val fields = listOf(
    FormField(
        id = KEY_NEW_EMAIL,
        validators = listOf(
            ValueRequiredValidator(R.string.value_required_error),
            // Regex validation to ensure email address validity
            TextRegexValidator(Regexp.EMAIL_REGEXP, R.string.invalid_format_error),
            // Email availability validation
            FakeEmailAvailableValidator()
        )
    )
)

// Initialize the form manager with the fields
val formManager = CarlosFormManager(formFields)

@Composable
fun ChangeEmailForm(formManager: FormManager) {
    val scope = rememberCoroutineScope()

    // Initialize the form manager
    LaunchedEffect(Unit) {
        formManager.initFormManager()
    }

    // Display a loading indicator to inform the user of ongoing validation
    val validationInProgress by formManager.validationInProgress.collectAsState()
    if (validationInProgress) {
        FullScreenProgressDialog()
    }

    Column {

        // Bind the FieldItem to the FormField composable
        FormTextField(
            fieldItem = formManager.getFieldItem(ChangeEmailFields.KEY_NEW_EMAIL),
            label = {
                Text("New email*")
            },
            maxLength = 32
        )

        Button(
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

_Note: Check the demo app code for more details, such as error handling and ensuring the new email differs from the current one._
