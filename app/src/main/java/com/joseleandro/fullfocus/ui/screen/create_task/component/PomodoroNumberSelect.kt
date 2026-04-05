package com.joseleandro.fullfocus.ui.screen.create_task.component

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joseleandro.fullfocus.ui.theme.FullFocusTheme

@Composable
fun PomodoroNumberSelect(
    modifier: Modifier = Modifier,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {

    val transition = updateTransition(
        targetState = selected,
        label = "Transition"
    )

    val alpha by transition.animateFloat(
        transitionSpec = {
            tween(
                durationMillis = 300
            )
        },
        label = "Alpha"
    ) {
        if (it) 1f else .1f
    }

    val labelColor by transition.animateColor(
        transitionSpec = {
            tween(
                durationMillis = 300
            )
        },
        label = "Color Text"
    ) {
        if (it) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
    }

    val scale by transition.animateFloat(
        transitionSpec = {
            tween(
                durationMillis = 300
            )
        },
        label = "Scale"
    ) {
        if (it) 1f else .9f
    }

    Surface(
        modifier = modifier
            .size(38.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            },
        onClick = onClick,
        shape = CircleShape,
        color = MaterialTheme.colorScheme.primary.copy(alpha = alpha)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = labelColor
                )
            )
        }
    }
}

@Preview
@Composable
private fun PomodoroNumberSelectSelectedPreview() {

    FullFocusTheme(
        dynamicColor = false,
        darkTheme = false
    ) {
        PomodoroNumberSelect(
            label = "8",
            selected = true,
            onClick = {}
        )
    }
}

@Preview
@Composable
private fun PomodoroNumberSelectLightPreview() {

    FullFocusTheme(
        dynamicColor = false,
        darkTheme = false
    ) {
        PomodoroNumberSelect(
            label = "8",
            selected = false,
            onClick = {}
        )
    }
}

@Preview
@Composable
private fun PomodoroNumberSelectDarkPreview() {

    FullFocusTheme(
        dynamicColor = false,
        darkTheme = false
    ) {
        PomodoroNumberSelect(
            label = "8",
            selected = false,
            onClick = {}
        )
    }
}