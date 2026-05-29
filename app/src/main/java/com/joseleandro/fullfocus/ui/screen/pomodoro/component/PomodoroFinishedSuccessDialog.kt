package com.joseleandro.fullfocus.ui.screen.pomodoro.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieClipSpec
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.joseleandro.fullfocus.R
import com.joseleandro.fullfocus.data.local.database.model.PomodoroState
import com.joseleandro.fullfocus.ui.theme.FullFocusTheme
import kotlinx.coroutines.delay

private const val COUNT_TIME = 10L

@Composable
fun PomodoroFinishedSuccessDialog(
    type: PomodoroState = PomodoroState.FOCUS,
    onDismissRequest: () -> Unit
) {

    val isPreview = LocalInspectionMode.current

    if (isPreview) {
        PomodoroFinishedSuccessDialogContent(
            onDismissRequest = onDismissRequest
        )
    } else {
        Dialog(
            onDismissRequest = onDismissRequest
        ) {
            PomodoroFinishedSuccessDialogContent(
                onDismissRequest = onDismissRequest
            )
        }
    }
}

@Composable
private fun PomodoroFinishedSuccessDialogContent(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit
) {

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.success))
    var time by remember { mutableLongStateOf(COUNT_TIME) }
    var isPressed by remember { mutableStateOf(false) }

    val timeFinished by remember {
        derivedStateOf {
            time == 0L
        }
    }

    LaunchedEffect(Unit) {
        while (time > 0) {
            delay(1_000)
            if (!isPressed)
                time -= 1
        }
    }

    LaunchedEffect(timeFinished) {
        if (timeFinished) {
            onDismissRequest()
        }
    }

    Surface(
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        tryAwaitRelease()
                        isPressed = false
                    }
                )
            }
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.extraLarge
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {


                LottieAnimation(
                    modifier = Modifier
                        .size(100.dp)
                        .padding(bottom = 16.dp),
                    composition = composition,
                    iterations = LottieConstants.IterateForever,
                    clipSpec = LottieClipSpec.Progress(0.5f, 0.75f),
                )
                Text(
                    text = "Tempo de foco terminado!",
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    modifier = Modifier.padding(vertical = 8.dp),
                    text = "Bom trabalho! Agora você pode fazer uma pausa.",
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center
                )

                TextButton(
                    modifier = Modifier.padding(top = 24.dp),
                    onClick = onDismissRequest,
                ) {
                    Text(
                        text = "Fechar"
                    )
                }
            }

            Stopwatch(
                modifier = Modifier.align(alignment = Alignment.TopEnd),
                time = time
            )
        }
    }
}

@Composable
private fun Stopwatch(
    modifier: Modifier = Modifier,
    time: Long
) {
    Box(
        modifier = modifier
            .padding(16.dp)
            .size(32.dp)
            .border(
                width = 2.dp, color = MaterialTheme.colorScheme.outlineVariant.copy(.6f),
                shape = CircleShape
            )
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = .6f),
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = time.toString(),
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Preview
@Composable
private fun PomodoroFinishedSuccessDialogLightPreview() {
    FullFocusTheme(
        dynamicColor = false,
        darkTheme = false
    ) {
        PomodoroFinishedSuccessDialog(
            type = PomodoroState.FOCUS,
            onDismissRequest = {}
        )
    }
}

@Preview
@Composable
private fun PomodoroFinishedSuccessDialogDarkPreview() {
    FullFocusTheme(
        dynamicColor = false,
        darkTheme = true
    ) {
        PomodoroFinishedSuccessDialog(
            type = PomodoroState.FOCUS,
            onDismissRequest = {}
        )
    }
}