package com.joseleandro.fullfocus.ui.screen.pomodoro_setting.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joseleandro.fullfocus.ui.theme.FullFocusTheme

@Composable
fun TimerInput(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {

    Column(
        modifier = modifier.height(IntrinsicSize.Min),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = .7f)
            )
        )

        BasicTextField(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            textStyle = TextStyle(
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            ),
            onValueChange = onValueChange
        ) { innerTextField ->
            Box(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = .1f),
                        shape = MaterialTheme.shapes.medium
                    )
                    .height(48.dp)
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                innerTextField()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TimerInputLightPreview() {

    FullFocusTheme(
        dynamicColor = false,
        darkTheme = false
    ) {
        TimerInput(
            label = "Pomodoro",
            value = "25",
            onValueChange = {}
        )
    }

}

@Preview(showBackground = true)
@Composable
private fun TimerInputDarkPreview() {

    FullFocusTheme(
        dynamicColor = false,
        darkTheme = true
    ) {
        TimerInput(
            label = "Pomodoro",
            value = "25",
            onValueChange = {}
        )
    }

}