package com.joseleandro.fullfocus.ui.component

import androidx.annotation.FloatRange
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.joseleandro.fullfocus.R
import com.joseleandro.fullfocus.domain.model.PomodoroState
import com.joseleandro.fullfocus.ui.theme.FullFocusTheme

private const val POMODORO_TIME_WIDTH_STROKE = 36f
private const val POMODORO_TIME_PROGRESS_BAR_BACKGROUND_COLOR = 0XFFAAAAAA
private const val POMODORO_TIME_PROGRESS_BAR_COLOR = 0XFFFF3838

@Composable
fun FullFocusPomodoroTime(
    modifier: Modifier = Modifier,
    size: Dp = 320.dp,
    @FloatRange(from = 0.0, to = 1.0)
    progress: Float = 0f,
    timeTotal: Long,
    state: PomodoroState
) {

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(300)
    )

    val currentTime = (timeTotal * animatedProgress).toLong()

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {

        FullFocusPomodoroTimeBackgroundBar()

        FullFocusPomodoroTimeProgressBar(progress = animatedProgress)

        Surface(
            modifier = Modifier
                .size(size = size * .8f)
                .dropShadow(
                    shape = CircleShape,
                    shadow = Shadow(
                        radius = 8.dp,
                        color = Color.Black.copy(alpha = 0.1f),
                        spread = 8.dp,
                        offset = DpOffset(0.dp, 0.dp)
                    )
                ),
            shape = CircleShape,
        ) {

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(
                    space = 16.dp,
                    alignment = Alignment.CenterVertically
                ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = stringResource(id = state.labelRes).uppercase(),
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Medium
                    )
                )

                Text(
                    text = currentTime.formattedTimer(),
                    style = MaterialTheme.typography.displayLarge.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Medium
                    )
                )

                Text(
                    text = stringResource(R.string.index_sessao, 1),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

private fun Long.formattedTimer(): String {
    val totalSeconds = this / 1000

    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60

    return "%02d:%02d".format(minutes, seconds)
}

@Composable
private fun FullFocusPomodoroTimeBackgroundBar(modifier: Modifier = Modifier) {
    Canvas(
        modifier = modifier.fillMaxSize()
    ) {

        drawArc(
            startAngle = 90f,
            color = Color(POMODORO_TIME_PROGRESS_BAR_BACKGROUND_COLOR),
            sweepAngle = 360f,
            useCenter = false,
            style = Stroke(
                width = POMODORO_TIME_WIDTH_STROKE
            )
        )
    }
}

@Composable
private fun FullFocusPomodoroTimeProgressBar(
    modifier: Modifier = Modifier,
    @FloatRange(from = 0.0, to = 1.0)
    progress: Float
) {


    Canvas(
        modifier = modifier.fillMaxSize()
    ) {

        drawArc(
            startAngle = -90f,
            color = Color(POMODORO_TIME_PROGRESS_BAR_COLOR).copy(alpha = .2f),
            sweepAngle = progress * 360,
            useCenter = false,
            style = Stroke(
                width = POMODORO_TIME_WIDTH_STROKE + 18f,
                cap = StrokeCap.Round
            )
        )

        drawArc(
            startAngle = -90f,
            color = Color(POMODORO_TIME_PROGRESS_BAR_COLOR).copy(alpha = .4f),
            sweepAngle = progress * 360,
            useCenter = false,
            style = Stroke(
                width = POMODORO_TIME_WIDTH_STROKE + 10f,
                cap = StrokeCap.Round
            )
        )

        drawArc(
            startAngle = -90f,
            color = Color(POMODORO_TIME_PROGRESS_BAR_COLOR),
            sweepAngle = progress * 360,
            useCenter = false,
            style = Stroke(
                width = POMODORO_TIME_WIDTH_STROKE,
                cap = StrokeCap.Round
            )
        )
    }
}

@Preview
@Composable
private fun FullFocusPomodoroTimeLightPreview() {
    FullFocusTheme(
        dynamicColor = false,
        darkTheme = false
    ) {
        FullFocusPomodoroTime(
            progress = .5f,
            state = PomodoroState.FOCUS,
            timeTotal = 25 * 60 * 1000
        )
    }
}

@Preview
@Composable
private fun FullFocusPomodoroTimeDarkPreview() {
    FullFocusTheme(
        dynamicColor = false,
        darkTheme = true
    ) {
        FullFocusPomodoroTime(
            progress = .5f,
            state = PomodoroState.FOCUS,
            timeTotal = 25 * 60 * 1000
        )
    }
}