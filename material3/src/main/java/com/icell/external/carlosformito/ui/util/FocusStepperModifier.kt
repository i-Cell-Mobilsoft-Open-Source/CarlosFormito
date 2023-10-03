package com.icell.external.carlosformito.ui.util

import android.view.KeyEvent.KEYCODE_ENTER
import android.view.KeyEvent.KEYCODE_TAB
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalFocusManager

private enum class StepperState {
    Idle, Initiated
}

fun Modifier.focusStepper(
    triggerKeyCodes: List<Int> = listOf(KEYCODE_TAB, KEYCODE_ENTER),
    nextFocusDirection: FocusDirection = FocusDirection.Down
): Modifier = composed {
    val focusManager = LocalFocusManager.current
    var stepperState = remember { StepperState.Idle }

    onPreviewKeyEvent { event ->
        if (event.nativeKeyEvent.keyCode in triggerKeyCodes) {
            when {
                event.type == KeyEventType.KeyDown -> {
                    stepperState = StepperState.Initiated
                }
                event.type == KeyEventType.KeyUp && stepperState == StepperState.Initiated -> {
                    stepperState = StepperState.Idle
                    focusManager.moveFocus(nextFocusDirection)
                }
            }
            true
        } else {
            false
        }
    }
}
