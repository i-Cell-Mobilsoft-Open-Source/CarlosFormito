# USE CASE 5 - Connected fields validation

In this example, we demonstrate how to implement a form for an international phone number. The form includes a country code picker field and a local phone number text input.

### Requirements:
1. Ignore validation for the test country (country code "TST").
2. For all other countries, ensure the local phone number matches the format expected for the selected country. (For simplicity, this example uses a dummy format validation. In real-world cases, consider integrating a third-party solution like [Google's libphonenumber](https://github.com/google/libphonenumber)).
3. If the selected country changes, the local phone number field should automatically update its validation state.

To achieve the desired validation behavior, we create a custom `InternationalPhoneNumberValidator` that extends the built-in `ConnectionValidator`, providing automatic validation for field connections.

_Read more about the `ConnectionValidator` class [here](https://github.com/icellmobilsoft/CarlosFormito/blob/master/README.md#connection-validator)._

<img src="https://github.com/icellmobilsoft/CarlosFormito/blob/master/documentation/images/carlos_connected_fields_validation_sample.png" height="500" alt="Connected fields sample 1"/> | 
<img src="https://github.com/icellmobilsoft/CarlosFormito/blob/master/documentation/images/carlos_connected_fields_validation_sample_filled.png" height="500" alt="Connected fields sample 2"/>

```kotlin
// First step: let's create a custom validator for validating international phone numbers
class InternationalPhoneNumberValidator(
    override val connectedFieldId: String,
    @StringRes private val errorMessageId: Int? = null
) : ConnectionValidator<String>() {

    override suspend fun validate(value: String?, context: FormValueContext): FormFieldValidationResult {
        val nonNullValue = value ?: ""

        val country = context.requireFieldValue<Country>(connectedFieldId)
        val phoneNumber = country.callCode + nonNullValue

        // Ignoring validation for test phone numbers
        if (country == Country.TST) {
            return FormFieldValidationResult.Valid
        }

        // Dummy phone number validation just for demo purposes
        if (phoneNumber.length != REQUIRED_INTERNATIONAL_PHONE_NUMBER_LENGTH) {
            return FormFieldValidationResult.Invalid.of(errorMessageId)
        }

        return FormFieldValidationResult.Valid
    }

    companion object {
        const val REQUIRED_INTERNATIONAL_PHONE_NUMBER_LENGTH = 12
    }
}

// Define the form fields, initial states and it's validators
val fields = listOf(
    FormField(
        id = KEY_COUNTRY_CODE,
        initialState = FormFieldState(Country.TST),
        validators = listOf(
            ValueRequiredValidator(R.string.value_required_error)
        )
    ),
    FormField<String>(
        id = KEY_LOCAL_NUMBER,
        validators = listOf(
            ValueRequiredValidator(R.string.value_required_error),
            // Defining a validation connection to the country code field by passing the KEY_COUNTRY_CODE field id
            InternationalPhoneNumberValidator(KEY_COUNTRY_CODE, R.string.invalid_phone_number_error)
        )
    )
)

// Initialize the form manager with the fields
val formManager = CarlosFormManager(formFields)

@Composable
fun InternationalPhoneNumberForm(formManager: FormManager) {
    val scope = rememberCoroutineScope()

    // Initialize the form manager
    LaunchedEffect(Unit) {
        formManager.initFormManager()
    }

    Column {

        // Bind the FieldItem to the FormField composable
        FormPickerField(
            fieldItem = formManager.getFieldItem<Country>(KEY_COUNTRY_CODE),
            label = {
                Text("Country*")
            },
            onClick = {
                // Display a country selection UI (eg. a bottom sheet with a list of countries)
            },
            isClearable = false,
            displayedValue = { country ->
                country?.let {
                    context.getString(country.selectionTextRes)
                } ?: "-"
            }
        )

        FormTextField(
            fieldItem = formManager.getFieldItem(KEY_LOCAL_NUMBER),
            label = {
                Text("Phone number*")
            },
            maxLength = SetPhoneNumberFields.PHONE_NUMBER_MAX_LENGTH,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
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

_Note: Check the demo app code for more details, such as handling country selection._

