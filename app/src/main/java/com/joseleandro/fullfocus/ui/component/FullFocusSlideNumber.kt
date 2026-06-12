package com.joseleandro.fullfocus.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.joseleandro.fullfocus.ui.theme.FullFocusTheme
import kotlin.math.roundToInt

@Composable
fun FullFocusSlideNumber(
    modifier: Modifier = Modifier,
    value: Int = 0,
    min: Int = 0,
    max: Int = 100,
    onValueChange: (Int) -> Unit,
) {
    val density = LocalDensity.current
    val thumbSize = 30.dp
    val thumbSizePx = with(density) { thumbSize.toPx() }

    BoxWithConstraints(
        modifier = modifier
            .widthIn(min = 200.dp)
            .fillMaxWidth()
            .height(thumbSize),
        contentAlignment = Alignment.CenterStart
    ) {
        val totalWidthPx = constraints.maxWidth.toFloat()
        val maxOffsetPx = if (totalWidthPx > thumbSizePx) totalWidthPx - thumbSizePx else 0f

        var internalOffsetX by remember { mutableFloatStateOf(0f) }

        LaunchedEffect(value, maxOffsetPx) {
            val range = max - min
            if (range > 0 && maxOffsetPx > 0) {
                internalOffsetX = ((value - min).toFloat() / range) * maxOffsetPx
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .background(
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f),
                    shape = MaterialTheme.shapes.medium
                )
        )

        val activeTrackWidth = with(density) { (internalOffsetX + (thumbSizePx / 2)).toDp() }
        Box(
            modifier = Modifier
                .width(activeTrackWidth)
                .height(8.dp)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = MaterialTheme.shapes.medium
                )
        )

        Box(
            modifier = Modifier
                .offset { IntOffset(internalOffsetX.roundToInt(), 0) }
                .draggable(
                    orientation = Orientation.Horizontal,
                    state = rememberDraggableState { delta ->
                        if (maxOffsetPx > 0) {
                            internalOffsetX = (internalOffsetX + delta).coerceIn(0f, maxOffsetPx)
                            val range = max - min
                            if (range > 0) {
                                val newValue = min + ((internalOffsetX / maxOffsetPx) * range).roundToInt()
                                if (newValue != value) {
                                    onValueChange(newValue)
                                }
                            }
                        }
                    }
                )
                .size(thumbSize)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = CircleShape
                )
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = CircleShape
                )
        )
    }
}

@Preview
@Composable
private fun FullFocusSlideNumberLightPreview() {

    var value by remember { mutableIntStateOf(0) }

    FullFocusTheme(
        dynamicColor = false,
        darkTheme = false
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                text = "Valor $value"
            )

            FullFocusSlideNumber(
                value = value,
                onValueChange = {
                    value = it
                }
            )
        }
    }
}

@Preview
@Composable
private fun FullFocusSlideNumberDarkPreview() {

    FullFocusTheme(
        dynamicColor = false,
        darkTheme = true
    ) {
        FullFocusSlideNumber(
            onValueChange = {}
        )
    }
}