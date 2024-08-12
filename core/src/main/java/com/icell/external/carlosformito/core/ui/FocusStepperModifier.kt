package com.icell.external.carlosformito.core.ui

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

/**
 * Represents the states of the stepper.
 */
private enum class StepperState {
    Idle, Initiated
}

/**
 * Extension function that adds focus-stepping behavior to a Modifier.
 * This behavior listens for specified key events and moves the focus
 * in a given direction when these events occur.
 *
 * @param triggerKeyCodes A list of key codes that will trigger the focus-stepping.
 * The default keys are [KEYCODE_TAB] and [KEYCODE_ENTER].
 * @param nextFocusDirection The direction in which the focus should move when a
 * trigger key is pressed. The default direction is [FocusDirection.Down].
 * @return A [Modifier] with the added focus-stepping behavior.
 */
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
