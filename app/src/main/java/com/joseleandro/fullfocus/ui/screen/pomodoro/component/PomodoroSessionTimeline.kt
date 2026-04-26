package com.joseleandro.fullfocus.ui.screen.pomodoro.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joseleandro.fullfocus.R
import com.joseleandro.fullfocus.data.local.preferences.data.enums.StatusSession
import com.joseleandro.fullfocus.ui.theme.FullFocusTheme

@Composable
fun PomodoroSessionTimeline(
    modifier: Modifier = Modifier,
    totalSessions: Int,
    currentSession: Int,
    statusSession: StatusSession
) {
    if (totalSessions <= 0) return

    val listState = rememberLazyListState()

    LaunchedEffect(currentSession) {
        val targetIndex = (currentSession - 1).coerceIn(0, totalSessions - 1)
        listState.animateScrollToItem(targetIndex)
    }

    val isFocus = statusSession == StatusSession.FOCUS
    val doneColor = MaterialTheme.colorScheme.primary
    val breakColor = Color(0xFF4CAF50)
    val pendingColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)

    // Container com largura definida e efeito de desfoque nas bordas
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp) // Define a "janela" da timeline
            .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
            .drawWithContent {
                drawContent()
                // Efeito de fade nas bordas para indicar que há mais itens
                drawRect(
                    brush = Brush.horizontalGradient(
                        0f to Color.Transparent,
                        0.1f to Color.Black,
                        0.9f to Color.Black,
                        1f to Color.Transparent
                    ),
                    blendMode = BlendMode.DstIn
                )
            },
        contentAlignment = Alignment.Center
    ) {
        LazyRow(
            state = listState,
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(totalSessions) { i ->
                val isPast = i < currentSession - 1
                val isCurrent = i == currentSession - 1

                val iconPainter = when {
                    isPast -> painterResource(id = R.drawable.baseline_check_24)
                    isCurrent -> if (isFocus) painterResource(id = R.drawable.tdesign_focus) else painterResource(
                        id = R.drawable.uiw_coffee
                    )

                    else -> painterResource(id = R.drawable.material_symbols_timer_outline_rounded)
                }

                val markerColor by animateColorAsState(
                    targetValue = when {
                        isPast -> doneColor
                        isCurrent -> if (isFocus) doneColor else breakColor
                        else -> pendingColor
                    },
                    label = "color"
                )

                val size by animateDpAsState(
                    targetValue = if (isCurrent) 28.dp else 20.dp,
                    label = "size"
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(size)
                            .clip(CircleShape)
                            .background(markerColor.copy(alpha = if (isCurrent) 0.2f else 0.1f))
                            .then(
                                if (isCurrent) Modifier.border(
                                    BorderStroke(2.dp, markerColor.copy(alpha = 0.5f)),
                                    CircleShape
                                ) else Modifier
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = iconPainter,
                            contentDescription = null,
                            modifier = Modifier.size(if (isCurrent) 18.dp else 16.dp),
                            tint = if (isPast || isCurrent) markerColor else markerColor.copy(alpha = 0.5f)
                        )
                    }

                    if (i < totalSessions - 1) {
                        val lineColor =
                            if (i < currentSession - 1) doneColor else MaterialTheme.colorScheme.outlineVariant
                        Spacer(
                            modifier = Modifier
                                .width(10.dp)
                                .height(3.dp)
                                .background(lineColor, CircleShape)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TimelineFewSessionsPreview() {
    FullFocusTheme(
        dynamicColor = false
    ) {
        PomodoroSessionTimeline(
            totalSessions = 4,
            currentSession = 2,
            statusSession = StatusSession.FOCUS
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TimelineManySessionsPreview() {
    FullFocusTheme(
        dynamicColor = false
    ) {
        PomodoroSessionTimeline(
            totalSessions = 20,
            currentSession = 5,
            statusSession = StatusSession.FOCUS
        )
    }
}
