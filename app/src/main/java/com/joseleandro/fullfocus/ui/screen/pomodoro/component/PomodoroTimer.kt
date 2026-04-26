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
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joseleandro.fullfocus.R
import com.joseleandro.fullfocus.data.local.preferences.data.enums.StatusSession
import com.joseleandro.fullfocus.ui.theme.FullFocusTheme
import java.util.Locale


@Composable
fun PomodoroTimer(
    modifier: Modifier = Modifier,
    time: Int,
    timeSession: Int,
    statusSession: StatusSession = StatusSession.FOCUS,
    currentSession: Int,
    totalSessions: Int,
    size: Dp = 340.dp
) {

    val isFocus = statusSession == StatusSession.FOCUS

    // Cores Dinâmicas baseadas no Estado
    val mainColor = if (isFocus) MaterialTheme.colorScheme.primary else Color(0xFF4CAF50)
    val secondaryColor = if (isFocus) MaterialTheme.colorScheme.secondary else Color(0xFF81C784)
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
                colors = listOf(mainColor, secondaryColor),
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
                    verticalArrangement = Arrangement.spacedBy(2.dp, Alignment.CenterVertically),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    // Ícone de Status (Ultra-Minimalista)
                    Icon(
                        painter = painterResource(
                            id = if (isFocus) R.drawable.tdesign_focus
                            else R.drawable.uiw_coffee
                        ),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = mainColor.copy(alpha = 0.6f)
                    )

                    Text(
                        text = stringResource(id = statusSession.description).uppercase(),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            letterSpacing = 2.sp,
                            color = mainColor.copy(alpha = 0.5f)
                        )
                    )

                    Text(
                        text = timeAnimated.formattedTime(),
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Light, // Mais elegante
                            fontSize = 80.sp, // Destaque total ao tempo
                            letterSpacing = (-4).sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )

                    if (totalSessions > 0) {
                        Text(
                            text = "$currentSession / $totalSessions",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Normal,
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                            )
                        )
                    }
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
private fun PomodoroTimerLightPauseShortPreview() {
    FullFocusTheme(
        dynamicColor = false,
        darkTheme = false
    ) {
        PomodoroTimer(
            size = 340.dp,
            time = 60 * 10,
            statusSession = StatusSession.PAUSE_SHORT,
            timeSession = 60 * 25,
            currentSession = 2,
            totalSessions = 10
        )
    }
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
            currentSession = 2,
            totalSessions = 10
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
            currentSession = 2,
            totalSessions = 10
        )
    }
}
