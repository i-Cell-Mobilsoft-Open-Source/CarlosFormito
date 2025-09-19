# USE CASE 6 - Multi connection validation

In this example, we demonstrate how to implement an apple quantity stock checker that validates input based on the selected apple type and size type.

### Requirements:
1. Validate the entered apple quantity in relation to the chosen apple type and size type.
2. Automatically revalidate the apple quantity whenever either the apple type or size type field changes.

To achieve this, we create a custom `AppleQuantityValidator` by extending the built-in `MultiConnectionValidator`.
This allows us to declare multiple connected fields (apple type and size type), so that any change in those fields will automatically trigger revalidation of the apple quantity field.

_Read more about the `MultiConnectionValidator` class [here](https://github.com/icellmobilsoft/CarlosFormito/blob/master/README.md#multi-connection-validator)._

<img src="https://github.com/icellmobilsoft/CarlosFormito/blob/master/documentation/images/carlos_connected_fields_validation_multi_sample_1.png" height="500" alt="Multi connections sample 1"/> | 
<img src="https://github.com/icellmobilsoft/CarlosFormito/blob/master/documentation/images/carlos_connected_fields_validation_multi_sample_2.png" height="500" alt="Multi connections sample 2"/>

```kotlin
enum class AppleType {
    Jonagold, Gala, Jonathan, Golden
}

enum class SizeType {
    Small, Medium, Big
}

// Define the form fields keys
const val KEY_APPLE_TYPE = "KEY_APPLE_TYPE"
const val KEY_SIZE_TYPE = "KEY_SIZE_TYPE"
const val KEY_QUANTITY = "KEY_QUANTITY"

// Define the form fields, initial states and it's validators
val fields = listOf(
    FormField<AppleType>(
        id = KEY_APPLE_TYPE,
        initialValue = AppleType.Jonagold
    ),
    FormField<SizeType>(
        id = KEY_SIZE_TYPE,
        initialValue = SizeType.Medium
    ),
    FormField<Int>(
        id = KEY_QUANTITY,
        validators = listOf(
            AppleQuantityValidator()
        )
    ),
)

// Create a custom validator that validates apple quantity in relation to the selected apple type and size type.
class AppleQuantityValidator : MultiConnectionValidator<Int>() {

    /**
     * Declare the field connections for this validator.
     *
     * Whenever either the apple type or size type field changes,
     * the field using this validator will:
     * - Automatically revalidate (in AutoOnFocusClear or AutoInline modes), or
     * - Reset its validation result (in Manual mode).
     */
    override val connectedFieldIds: List<String> = listOf(
        KEY_APPLE_TYPE,
        KEY_SIZE_TYPE
    )

    override suspend fun validate(
        value: Int?,
        context: FormValueContext
    ): FormFieldValidationResult {
        val quantity = value ?: return FormFieldValidationResult.Valid
        
        val appleType: AppleType? = context.getFieldValue(KEY_APPLE_TYPE)
        val sizeType: SizeType? = context.getFieldValue(KEY_SIZE_TYPE)

        // Dummy invalid cases just for demo purposes
        return when {
            sizeType == SizeType.Small && quantity > 1 -> {
                FormFieldValidationResult.Invalid.of(
                    R.string.apple_quantity_error,
                    listOf(appleType.name, sizeType.name, 1)
                )
            }

            appleType == AppleType.Golden && quantity > 15 -> {
                FormFieldValidationResult.Invalid.of(
                    R.string.apple_quantity_error,
                    listOf(appleType.name, sizeType.name, 15)
                )
            }

            appleType == AppleType.Jonagold && sizeType == SizeType.Big -> {
                FormFieldValidationResult.Invalid.of(
                    R.string.apple_quantity_error,
                    listOf(appleType.name, sizeType.name, 0)
                )
            }

            quantity > 20 -> {
                FormFieldValidationResult.Invalid.of(
                    R.string.apple_quantity_error,
                    listOf(appleType.name, sizeType.name, 20)
                )
            }

            else -> FormFieldValidationResult.Valid
        }
    }
}

// Initialize the form manager with the fields
val formManager = CarlosFormManager(formFields, FormFieldValidationStrategy.AutoInline())

@Composable
fun MultiConnectionSampleForm(formManager: FormManager) {
    val scope = rememberCoroutineScope()

    // Initialize the form manager
    LaunchedEffect(Unit) {
        formManager.initFormManager()
    }

    val appleTypeFieldItem = formManager.getFieldItem<AppleType>(KEY_APPLE_TYPE)
    val sizeTypeFieldItem = formManager.getFieldItem<SizeType>(KEY_SIZE_TYPE)

    Column {

        // Bind the FieldItem to the FormField composable
        FormPickerField(
            fieldItem = appleTypeFieldItem,
            onClick = {
                // Display a apple type selection UI (eg. a bottom sheet with a list of apples)
            },
            displayedValue = { value -> value?.name ?: "-" },
            isClearable = false,
            label = {
                Text("Apple type")
            }
        )

        FormPickerField(
            fieldItem = sizeTypeFieldItem,
            onClick = {
                // Display a size type selection UI (eg. a bottom sheet with a list of size types)
            },
            displayedValue = { value -> value?.name ?: "-" },
            isClearable = false,
            label = {
                Text("Size type")
            }
        )

        FormIntegerField(
            fieldItem = formManager.getFieldItem(KEY_QUANTITY),
            label = {
                Text("Quantity")
            }
        )

        Button(
            onClick = {
                // Validate the form when clicking the submit button
                scope.launch {
                    formManager.validateForm()
                }
            }
        ) {
            Text("Submit")
        }
    }
}
```

_Note: Check the demo app code for more details, such as handling type selections._
