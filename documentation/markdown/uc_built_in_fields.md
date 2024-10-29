# USE CASE 1 - Using the built-in fields

<img src="https://github.com/icellmobilsoft/CarlosFormito/blob/master/documentation/images/carlos_built_in_field_samples.png" height="500" alt="Built-in fields sample 1"/> | 
<img src="https://github.com/icellmobilsoft/CarlosFormito/blob/master/documentation/images/carlos_built_in_field_samples_filled.png" height="500" alt="Built-in fields sample 2"/>

Using Carlos's built-in fields is as straightforward as it sounds.

Simply choose the appropriate composable for each `FormFieldItem` and bind the field's state to it.\
_Note: For a full list of built-in form fields, along with their purposes and key features, click [here](https://github.com/icellmobilsoft/CarlosFormito/blob/master/README.md#built-in-form-fields)._

```kotlin
// Define the form fields and it's validators
val formFields = listOf(
    FormField(
        id = KEY_FORM_FIELD_NAME,
        validators = listOf(
            ValueRequiredValidator(R.string.value_required_error),
            TextMinLengthValidator(minLength = 3, R.string.min_length_error)
        )
    ),
    FormField(
        id = KEY_FORM_FIELD_SECRET,
        validators = listOf(
            ValueRequiredValidator(R.string.value_required_error),
            TextMinLengthValidator(minLength = 8)
        )
    ),
    FormField(
        id = KEY_FORM_FIELD_DATE,
        validators = listOf(
            ValueRequiredValidator(R.string.value_required_error),
            DateMinMaxValidator(
                minValue = LocalDate.now(),
                maxValue = LocalDate.now().plusWeeks(1),
                R.string.date_min_max_error
            )
        )
    ),
    FormField(
        id = KEY_FORM_FIELD_TIME,
        validators = listOf(
            ValueRequiredValidator(R.string.value_required_error),
            TimeMinMaxValidator(
                minValue = LocalTime.NOON,
                maxValue = LocalTime.of(18, 0)
            )
        )
    ),
    FormField(
        id = KEY_FORM_FIELD_SIZE,
        validators = listOf(
            IntegerMinMaxValidator(
                minValue = 100,
                maxValue = 200
            )
        )
    ),
    FormField<PackageType>(
        id = KEY_FORM_FIELD_PACKAGE,
        validators = listOf(
            ValueRequiredValidator()
        )
    )
)

// Initialize the form manager with the fields and a validation strategy
val formManager = CarlosFormManager(formFields, FormFieldValidationStrategy.AUTO_ON_FOCUS_CLEAR)

// Implement a form using the formManager
@Composable
fun SampleForm(formManager: FormManager) {
    val scope = rememberCoroutineScope()

    // Initialize the form manager
    LaunchedEffect(Unit) {
        formManager.initFormManager()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .clearFocusOnTap() // You can use this Modifier extension to clear focus when clicking outside the fields
            .padding(horizontal = 16.dp)
    ) {

        // Bind the FieldItem to the FormField composable
        FormTextField(
            fieldItem = formManager.getFieldItem(KEY_FORM_FIELD_NAME),
            label = {
                Text("Name*")
            },
            supportingText = "Please enter your full name."
        )

        FormPasswordTextField(
            fieldItem = formManager.getFieldItem(KEY_FORM_FIELD_SECRET),
            label = {
                Text("Secret*")
            },
            supportingText = "Please give a strong password (min 8 characters)."
        )

        FormDatePickerField(
            fieldItem = formManager.getFieldItem(KEY_FORM_FIELD_DATE),
            label = {
                Text("Date*")
            },
            dialogTitle = "Select date",
            supportingText = "Please select a date from next week."
        )

        FormTimePickerField(
            fieldItem = formManager.getFieldItem(KEY_FORM_FIELD_TIME),
            label = {
                Text("Time*")
            },
            dialogTitle = "Select time",
            supportingText = "Please choose a time between noon and 6 p.m."
        )

        FormIntegerField(
            fieldItem = formManager.getFieldItem(KEY_FORM_FIELD_SIZE),
            label = {
                Text("Size (optional field)")
            },
            supportingText = "Please enter a value between 100 and 200."
        )

        FormPickerField(
            fieldItem = formManager.getFieldItem(KEY_FORM_FIELD_PACKAGE),
            label = {
                Text("Package type*")
            },
            supportingText = "Please pick a Package type.",
            onClick = {
                // Display a package type selection UI (eg. a bottom sheet with a list of package types)
            },
            displayedValue = { packageType ->
                packageType?.displayedValue() ?: ""
            }
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