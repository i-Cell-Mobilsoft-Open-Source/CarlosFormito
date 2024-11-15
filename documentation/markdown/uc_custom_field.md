# USE CASE 2 - Creating a custom field

You can choose whether to use the UI modules or not. It’s possible to use only the core module as a project dependency and create your own UI components that adapt to the states provided by Carlos.

### Important considerations:
When implementing custom form input components, remember that as a developer, you are responsible for calling a set of predefined callbacks to achieve the desired behavior.

The `FormFieldItem` plays a crucial role in this setup, as it serves as the communication channel through which the `FormManager` receives updates about field state changes, such as value changes, focus loss, and visibility changes. Failure to call any of these callbacks may result in incorrect or inconsistent form validation states.

```kotlin
interface FormFieldItem<T> {
    
    // Notifies the form field item that its value has changed.
    fun <T> onFieldValueChanged(value: T?)

    // Notifies the form field item that focus has been cleared from the field.
    fun onFieldFocusCleared()

    // Notifies the form field item about changes in field visibility.
    fun onFieldVisibilityChanged(visible: Boolean)
}
```

### Checklist for implementing custom components:
1. **Value Change** : When the custom field value changes, call `onFieldValueChanged(<new value>)` on the corresponding `FormFieldItem`.
2. **Visibility Tracking**: Use the built-in `TrackVisibilityEffect` and pass the visibility value to the field item's `onFieldVisibilityChanged(<visible>)` function. This is essential because Carlos skips validation for invisible fields.
3. **Focus Clear Tracking** (optional): To track focus-clear events, use the `Modifier.onFocusCleared(callback: () -> Unit)` modifier and call the field item's `onFieldFocusCleared()` within the callback. This step is recommended for components receiving key events and is relevant only for the `AUTO_ON_FOCUS_CLEAR` validation strategy.

<img src="https://github.com/icellmobilsoft/CarlosFormito/blob/master/documentation/images/carlos_custom_fields_samples.png" height="500" alt="Custom fields sample 1"/> | 
<img src="https://github.com/icellmobilsoft/CarlosFormito/blob/master/documentation/images/carlos_custom_fields_samples_filled.png" height="500" alt="Custom fields sample 2"/>

Here is an example of a simple custom field, that displays the selected value's text placed on a Card:

```kotlin
// First, create the custom field
fun FormQuantityField(
    modifier: Modifier = Modifier,
    fieldItem: FormFieldItem<Int>,
    enabled: Boolean = true,
    minQuantity: Int = Int.MIN_VALUE,
    maxQuantity: Int = Int.MAX_VALUE,
    supportingText: CharSequence? = null
) {
    // Retrieve the field's state, which includes both the value and validation result
    val state by fieldItem.collectFieldState()

    // Visibility Tracking
    TrackVisibilityEffect { visible ->
        fieldItem.onFieldVisibilityChanged(visible)
    }

    Column(
        modifier = modifier
    ) {
        Card(
            elevation = 0.dp,
            enabled = false,
            onClick = {},
            backgroundColor = MaterialTheme.colors.primary.copy(alpha = 0.06f)
        ) {
            val currentValue = state.value ?: 0

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    enabled = enabled && currentValue > minQuantity,
                    onClick = {
                        // Handling value changes
                        fieldItem.onFieldValueChanged(currentValue - 1)
                    }
                ) {
                    Icon(
                        Icons.Default.RemoveCircleOutline,
                        contentDescription = "Minus",
                        modifier = Modifier.size(32.dp)
                    )
                }
                Text(
                    text = currentValue.toString(),
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
                IconButton(
                    enabled = enabled && currentValue < maxQuantity,
                    onClick = {
                        // Handling value changes
                        fieldItem.onFieldValueChanged(currentValue + 1)
                    }
                ) {
                    Icon(
                        Icons.Default.AddCircleOutline,
                        contentDescription = "Plus",
                        modifier = Modifier.size(32.dp)
                    )
                }
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

// Define the form field, initial state and the validators
val formFields = listOf( 
    FormField(
        id = KEY_QUANTITY,
        initialState = FormFieldState(value = 0),
        validators = listOf(
            IntegerMinValidator(minValue = 1)
        )
    )
)

// Initialize the form manager with the fields
val formManager = CarlosFormManager(formFields)

@Composable
fun CustomFieldDemoForm(formManager: FormManager) {
    val scope = rememberCoroutineScope()

    // Initialize the form manager
    LaunchedEffect(Unit) {
        formManager.initFormManager()
    }
    
    Column {

        // Bind the FieldItem to the custom field component
        FormQuantityField(
            fieldItem = formManager.getFieldItem(KEY_QUANTITY),
            minQuantity = 0,
            maxQuantity = 5,
            supportingText = "Maximum 5 tickets at once."
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

_Note: Check the demo app code for more examples of custom fields._
