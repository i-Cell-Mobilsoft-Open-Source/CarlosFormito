package com.icell.external.carlosformito.ui.util

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.semantics.testTagsAsResourceId

@OptIn(ExperimentalComposeUiApi::class)
internal fun Modifier.testId(
    value: String
): Modifier = this
    .semantics {
        testTag = value
        testTagsAsResourceId = true
    }
