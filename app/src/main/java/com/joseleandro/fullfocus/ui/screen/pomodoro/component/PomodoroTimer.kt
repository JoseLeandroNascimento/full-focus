package com.joseleandro.fullfocus.ui.screen.pomodoro.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joseleandro.fullfocus.data.local.preferences.data.enums.StatusSession
import com.joseleandro.fullfocus.ui.theme.FullFocusTheme
import java.util.Locale


@Composable
fun PomodoroTimer(
    modifier: Modifier = Modifier,
    time: Int,
    timeSession: Int,
    statusSession: StatusSession = StatusSession.FOCUS,
    supportText: String,
    size: Dp = 340.dp
) {

    val progressColor = MaterialTheme.colorScheme.primary
    val progressColor2 = MaterialTheme.colorScheme.secondary
    val progressBar = MaterialTheme.colorScheme.surfaceVariant
    val backGround = MaterialTheme.colorScheme.background

    val progress =
        if (timeSession == 0) 0f else (360f * (time.toFloat() / timeSession)).coerceAtMost(
            360f
        )

    val progressAnimated by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(300, easing = LinearEasing),
        label = ""
    )

    val timeAnimated by animateIntAsState(
        targetValue = time,
        animationSpec = tween(durationMillis = 300, easing = LinearEasing)
    )

    Box(
        modifier = modifier.size(size)
    ) {

        Canvas(
            modifier = Modifier.size(size)
        ) {

            drawProgressStroke(
                color = progressBar
            )

            drawProgressIndicator(
                colors = listOf(progressColor, progressColor2),
                progress = progressAnimated
            )
        }

        Surface(
            modifier = Modifier
                .size(size)
                .padding(32.dp),
            shape = CircleShape,
            color = backGround,
            tonalElevation = 6.dp,
            shadowElevation = 6.dp
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = stringResource(id = statusSession.description),
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = MaterialTheme.colorScheme.primary
                        )
                    )

                    Text(
                        text = timeAnimated.formattedTime(),
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 64.sp,
                            letterSpacing = (-1).sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )

                    Text(
                        text = supportText,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        ),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                }
            }
        }
    }
}

fun Int.formattedTime(): String {
    val minutes = this / 60
    val seconds = this % 60

    return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
}

private fun DrawScope.drawProgressStroke(
    color: Color
) {
    drawArc(
        color = color,
        startAngle = -90f,
        sweepAngle = 360f,
        useCenter = false,
        style = Stroke(
            width = 12.dp.toPx(),
            cap = StrokeCap.Round
        )
    )
}

private fun DrawScope.drawProgressIndicator(
    progress: Float,
    colors: List<Color>
) {

    val strokeWidth = 12.dp.toPx()

    drawArc(
        brush = Brush.linearGradient(colors),
        startAngle = -90f,
        sweepAngle = progress,
        useCenter = false,
        style = Stroke(
            width = strokeWidth * 1.8f,
            cap = StrokeCap.Round
        ),
        alpha = 0.15f
    )

    drawArc(
        brush = Brush.linearGradient(colors),
        startAngle = -90f,
        sweepAngle = progress,
        useCenter = false,
        style = Stroke(
            width = strokeWidth * 1.4f,
            cap = StrokeCap.Round
        ),
        alpha = 0.15f
    )

    drawArc(
        brush = Brush.linearGradient(colors),
        startAngle = -90f,
        sweepAngle = progress,
        useCenter = false,
        style = Stroke(
            width = strokeWidth,
            cap = StrokeCap.Round
        )
    )
}


@Preview
@Composable
private fun PomodoroTimerLightPreview() {
    FullFocusTheme(
        dynamicColor = false,
        darkTheme = false
    ) {
        PomodoroTimer(
            size = 340.dp,
            time = 60 * 4,
            timeSession = 60 * 25,
            supportText = "1/4 sessões"
        )
    }
}

@Preview
@Composable
private fun PomodoroTimerDarkPreview() {
    FullFocusTheme(
        dynamicColor = false,
        darkTheme = true
    ) {
        PomodoroTimer(
            size = 340.dp,
            time = 60 * 4,
            timeSession = 60 * 25,
            supportText = "1/4 sessões"
        )
    }
}
