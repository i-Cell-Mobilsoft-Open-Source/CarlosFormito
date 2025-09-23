# CarlosFormito

<picture>
  <source media="(prefers-color-scheme: dark)" srcset="./documentation/images/carlos_logo_dark.png" height="200">
  <img alt="Carlos Logo" src="./documentation/images/carlos_logo_light.png" height="200">
</picture>

Easy form state management for Jetpack Compose.

## Introduction

Working with forms in your application? Carlos can be your ultimate wingman.\
While building forms might seem straightforward, a closer look reveals the need to manage:

1. Validation strategies
2. Field state changes
3. Cross-field dependencies, and more...
<br></br>
> "I don't want to reinvent the wheel.\
Many common user flows and use cases appear across mobile applications."

\
There’s an effective solution for this, right? - The answer is yes!\
Carlos handles the heavy lifting so you can focus on creating forms effectively and quickly.

## Adding Carlos to Your project

```groovy
repositories {
    mavenCentral()
}

dependencies {
    // Carlos core functionality
    implementation("hu.icellmobilsoft.carlosformito:carlosformito-core:$carlos_version")

    // Carlos built-in FormFields for material3 (optional usage)
    implementation("hu.icellmobilsoft.carlosformito:carlosformito-material3:$carlos_version")

    // Carlos built-in FormFields for material (optional usage)
    implementation("hu.icellmobilsoft.carlosformito:carlosformito-material:$carlos_version")
}
```

**Note:** Don't forget to enable core library desugaring in your project if `minSdk < 26`.

## Using Carlos

```kotlin
// Define the form fields, initial states and it's validators
val formFields = listOf(
    FormField(
        id = KEY_USERNAME,
        validators = listOf(
            ValueRequiredValidator(R.string.value_required_error),
            TextMinLengthValidator(minLength = 3, R.string.min_length_error)
        )
    ),
    FormField(
        id = KEY_PASSWORD,
        validators = listOf(
            ValueRequiredValidator(R.string.value_required_error),
            TextMinLengthValidator(minLength = 8)
        )
    )
)

// Initialize the form manager with the fields and a validation strategy
val formManager = CarlosFormManager(formFields, FormFieldValidationStrategy.Manual)

// Implement a form using the formManager
@Composable
fun LoginForm(formManager: FormManager) {
    val scope = rememberCoroutineScope()

    // Initialize the form manager
    LaunchedEffect(Unit) {
        formManager.initFormManager()
    }

    Column {

        // Bind the FieldItem to the FormField composable
        FormTextField(
            fieldItem = formManager.getFieldItem(KEY_USERNAME),
            label = {
                Text("Name*")
            },
            supportingText = "Please enter your username."
        )

        FormPasswordTextField(
            fieldItem = formManager.getFieldItem(KEY_PASSWORD),
            label = {
                Text("Password*")
            },
            supportingText = "Please enter your password."
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

### Steps to build a Carlos form

1. Define `FormField` elements, specifying field keys, initial states, and validators.
2. Create a `CarlosFormManager`, configure it with the fields, and select a validation strategy.
3. Build a Composable form UI using Carlos's built-in fields or your custom fields.
4. Bind `FieldItem`s to the respective UI elements.

### Validation strategies

| Validation strategy      | Description                                                     |
|:-------------------------|:----------------------------------------------------------------|
| Manual (default)         | Validates the form manually in a sequential top-down order.     |
| Automatic on focus clear | Validates each field automatically when its focus is cleared.   |
| Automatic inline         | Validates each field automatically upon any value change event. |

Try each strategy using the provided demo to determine the best fit for your project.

### Built-in validators

| Validator                          | Description                                                                   |
|:-----------------------------------|:------------------------------------------------------------------------------|
| ValueRequiredValidator             | Ensures that a value is not null or blank (for `String` types).               |
| TextMinLengthValidator             | Checks if a `String` value meets the minimum length requirement.              |
| TextRegexValidator                 | Verifies that a `String` value matches a specified regex pattern.             |
| IntegerMinValidator                | Ensures that an integer value is no less than a specified minimum.            |
| IntegerMaxValidator                | Ensures that an integer value does not exceed a specified maximum value.      |
| IntegerMinMaxValidator             | Confirms that an integer value is within a specified range.                   |
| DateMinValidator                   | Checks that a `LocalDate` value is not earlier than a specified minimum date. |
| DateMaxValidator                   | Ensures that a `LocalDate` value does not exceed a specified maximum date.    |
| DateMinMaxValidator                | Confirms that a `LocalDate` value falls within a specific date range.         |
| TimeMinValidator                   | Checks that a `LocalTime` value is not earlier than a minimum time.           |
| TimeMaxValidator                   | Ensures that a `LocalTime` value does not exceed a maximum time.              |
| TimeMinMaxValidator                | Confirms that a `LocalTime` falls within a specified range.                   |
| ContainsNumberValidator            | Ensures that a `String` contains at least one numeric digit.                  |
| ContainsSpecialCharacterValidator  | Ensures that a `String` contains at least one special character.              |
| ContainsUpperAndLowercaseValidator | Confirms that a `String` includes both uppercase and lowercase characters.    |

_Note: To create custom validators, inherit from `FormFieldValidator` - learn more [here](https://github.com/icellmobilsoft/CarlosFormito/blob/master/documentation/markdown/uc_long_running_validation.md)._

### Cross-field validation

Carlos leverages cross-field validation. In the validation callback, alongside the field input, a `FormContext` parameter provides access to other field states, enabling comprehensive validation.
<br></br>
By specifying a `connectedFieldId`, Carlos supports automatic validation between connected fields through the `ConnectionValidator`, tracking field changes and interactions seamlessly.

#### Connection validator

The `ConnectionValidator` facilitates validation in various scenarios:
* Ensuring a field’s value matches another field’s value (e.g., password confirmation).
* Validating a field whose value depends on another field (e.g., country code + phone number).

Carlos manages field connections during user interactions:
1. **Manual strategy**: Clears the validation results for both the field and connected field when the field’s value changes.
2. **Auto on focus clear strategy**: If the connected field has a value, triggers auto-validation on the connected field when the primary field loses focus.
3. **Auto inline strategy**: If the connected field has a value, runs auto-validation on the connected field when the primary field’s value changes.

##### Built-in connection validators

| Validator            | Description                                                       |
|:---------------------|:------------------------------------------------------------------|
| EqualsToValidator    | Ensures that a field’s value matches another field’s value.       |
| NotEqualsToValidator | Confirms that a field’s value differs from another field’s value. |
| CompareToValidator   | Compares a field’s value against another field’s value.           |

_Note: create a custom connection validator, inherit from `ConnectionValidator` - learn more [here](https://github.com/icellmobilsoft/CarlosFormito/blob/master/documentation/markdown/uc_connected_fields.md)._

#### Multi-connection validator

`MultiConnectionValidator` extends the idea of `ConnectionValidator` by supporting validation across multiple field connections simultaneously.

This is especially useful for context-aware validation, where a field’s validity depends on related field values.
For example: verifying that a price is correct based on the selected unit type, VAT rate, or applied discounts.

_Note: to implement your own connection logic, create a custom validator by inheriting from `MultiConnectionValidator`. Learn more in the [documentation]((https://github.com/icellmobilsoft/CarlosFormito/blob/master/documentation/markdown/uc_connected_fields_multi.md))._

### Built-in form fields

| Form field name       | Purpose & key features                                                                                          |
|:----------------------|:----------------------------------------------------------------------------------------------------------------|
| FormTextField         | A customizable text field ideal for general input, with placeholder text, character limits, and input patterns. |
| FormPasswordTextField | A password field with a toggle to show/hide text, ensuring secure input.                                        |
| FormIntegerField      | A text field that accepts only integer values. Ideal for numerical data like age or quantity.                   |
| FormPickerField       | A picker for selecting from predefined options. Suitable for lists like countries or categories.                |
| FormDatePickerField   | A date picker field with support for min/max dates and custom formats, perfect for selecting dates.             |
| FormTimePickerField   | A time picker field supporting both AM/PM and 24-hour formats, ideal for time selection.                        |

## Use cases
1. [**USE CASE 1** - Using the built-in fields](https://github.com/icellmobilsoft/CarlosFormito/blob/master/documentation/markdown/uc_built_in_fields.md)
2. [**USE CASE 2** - Creating a custom field](https://github.com/icellmobilsoft/CarlosFormito/blob/master/documentation/markdown/uc_custom_field.md)
3. [**USE CASE 3** - Long running validations](https://github.com/icellmobilsoft/CarlosFormito/blob/master/documentation/markdown/uc_long_running_validation.md)
4. [**USE CASE 4** - EqualsTo validation](https://github.com/icellmobilsoft/CarlosFormito/blob/master/documentation/markdown/uc_equals_to_validation.md)
5. [**USE CASE 5** - Connected fields validation](https://github.com/icellmobilsoft/CarlosFormito/blob/master/documentation/markdown/uc_connected_fields.md)
6. [**USE CASE 6** - Multi connection validation](https://github.com/icellmobilsoft/CarlosFormito/blob/master/documentation/markdown/uc_connected_fields_multi.md)

## Contribution

Want to contribute to Carlos? Have suggestions or issues with a specific feature or use case?

1. Open an issue to discuss your solution or idea.
2. After validating your approach, feel free to open a PR.
