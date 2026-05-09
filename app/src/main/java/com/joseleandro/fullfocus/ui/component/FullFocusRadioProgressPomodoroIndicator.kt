package com.joseleandro.fullfocus.ui.component

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joseleandro.fullfocus.ui.theme.FullFocusTheme

@Composable
fun FullFocusRadioProgressPomodoroIndicator(
    modifier: Modifier = Modifier,
    indexCurrent: Int = 1,
    totalPomodoros: Int = 4
) {

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(
            space = 8.dp,
            alignment = Alignment.CenterHorizontally
        )
    ) {

        (1..totalPomodoros).forEach { index ->
            RadioBoxIndicator(
                enabled = index <= indexCurrent,
                selected = index < indexCurrent
            )
        }
    }
}

private enum class RadioBoxIndicatorState {
    DISABLED,
    ENABLED,
    SELECTED
}

@Composable
private fun RadioBoxIndicator(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    selected: Boolean = false
) {

    val state = when {
        !enabled && !selected -> RadioBoxIndicatorState.DISABLED
        selected -> RadioBoxIndicatorState.SELECTED
        else -> RadioBoxIndicatorState.ENABLED
    }

    val transition = updateTransition(
        targetState = state,
        label = "radio_box_indicator_transition"
    )

    val borderColor by transition.animateColor(
        label = "border_color"
    ) {
        when (it) {
            RadioBoxIndicatorState.DISABLED -> MaterialTheme.colorScheme.surfaceVariant
            RadioBoxIndicatorState.ENABLED -> Color(0xFF2FB3A8)
            RadioBoxIndicatorState.SELECTED -> Color(0xFF2FB3A8)
        }
    }

    val backgroundColor by transition.animateColor(
        label = "background_color"
    ) {
        when (it) {
            RadioBoxIndicatorState.DISABLED -> MaterialTheme.colorScheme.surfaceVariant
            RadioBoxIndicatorState.ENABLED -> MaterialTheme.colorScheme.surfaceVariant
            RadioBoxIndicatorState.SELECTED -> Color(0xFF2FB3A8)
        }
    }

    val padding by transition.animateDp(
        label = "padding_radio"
    ) {
        when (it) {
            RadioBoxIndicatorState.DISABLED -> 4.dp
            RadioBoxIndicatorState.ENABLED -> 4.dp
            RadioBoxIndicatorState.SELECTED -> 0.dp
        }
    }

    Box(
        modifier = modifier
            .size(16.dp)
            .border(
                width = 2.dp,
                color = borderColor,
                shape = CircleShape
            )
            .padding(padding),
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    color = backgroundColor,
                    shape = CircleShape
                )
        ) {

        }
    }
}

@Preview
@Composable
private fun FullFocusRadioProgressPomodoroLightPreview() {
    FullFocusTheme(
        dynamicColor = false,
        darkTheme = false
    ) {
        FullFocusRadioProgressPomodoroIndicator(
            indexCurrent = 2
        )
    }
}

@Preview
@Composable
private fun FullFocusRadioProgressPomodoroDarkPreview() {
    FullFocusTheme(
        dynamicColor = false,
        darkTheme = true
    ) {
        FullFocusRadioProgressPomodoroIndicator()
    }
}