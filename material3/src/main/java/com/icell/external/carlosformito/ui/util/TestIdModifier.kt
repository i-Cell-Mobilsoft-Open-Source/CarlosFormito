package com.icell.external.carlosformito.ui.util

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.semantics.testTagsAsResourceId

/**
 * Extension function for [Modifier] that adds a test tag to the semantics properties for testing purposes.
 *
 * This function sets the `testTag` and `testTagsAsResourceId` properties in the semantics configuration
 * to enable identifying the composable in tests.
 *
 * @param value The test tag to be applied to the composable.
 * @return The modified [Modifier] with the added test tag.
 */
@OptIn(ExperimentalComposeUiApi::class)
fun Modifier.testId(
    value: String
): Modifier = this
    .semantics {
        testTag = value
        testTagsAsResourceId = true
    }
