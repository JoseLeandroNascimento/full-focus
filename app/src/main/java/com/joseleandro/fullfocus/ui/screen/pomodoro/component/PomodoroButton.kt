package com.joseleandro.fullfocus.ui.screen.pomodoro.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joseleandro.fullfocus.R
import com.joseleandro.fullfocus.ui.theme.FullFocusTheme

@Composable
fun PomodoroButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true,
    icon: @Composable () -> Unit
) {

    Box(
        modifier = modifier
            .clip(shape = CircleShape)
            .clickable(
                enabled = enabled,
                onClick = onClick
            )
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        icon()
    }

}

@Preview(showBackground = true)
@Composable
private fun PomodoroButtonLightPreview() {

    FullFocusTheme(
        dynamicColor = false,
        darkTheme = false
    ) {
        PomodoroButton(
            onClick = {}
        ) {
            Icon(
                modifier = Modifier.size(40.dp),
                painter = painterResource(id = R.drawable.line_md_play_filled),
                contentDescription = "play"
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
private fun PomodoroButtonDarkPreview() {

    FullFocusTheme(
        dynamicColor = false,
        darkTheme = true
    ) {
        PomodoroButton(
            onClick = {}
        ) {
            Icon(
                modifier = Modifier.size(40.dp),
                painter = painterResource(id = R.drawable.line_md_play_filled),
                contentDescription = "play"
            )
        }
    }

}