package com.joseleandro.fullfocus.ui.screen.config_sound.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joseleandro.fullfocus.R
import com.joseleandro.fullfocus.ui.theme.FullFocusTheme

@Composable
fun SoundBackgroundControls(
    modifier: Modifier = Modifier,
    volume: Float,
    onChangeVolume: (Float) -> Unit,
    onResetVolume: () -> Unit
) {

    var volumeValue by remember(volume) {
        mutableFloatStateOf(volume)
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.volume_do_som),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "${volumeValue.toInt()}%",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Slider(
                value = volumeValue,
                onValueChange = {
                    volumeValue = it
                },
                onValueChangeFinished = {
                    onChangeVolume(volumeValue)
                },
                valueRange = 0f..100f,
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                IconButton(onClick = {
                    volumeValue = 0f
                    onChangeVolume(0f)
                }) {
                    Icon(painterResource(R.drawable.mingcute_volume_mute_line), "Mudo")
                }
                IconButton(onClick = {
                    val newVal = (volumeValue - 5).coerceAtLeast(0f)
                    volumeValue = newVal
                    onChangeVolume(newVal)
                }) {
                    Icon(painterResource(R.drawable.outline_remove_24), "Diminuir")
                }
                IconButton(onClick = {
                    val newVal = (volumeValue + 5).coerceAtMost(100f)
                    volumeValue = newVal
                    onChangeVolume(newVal)
                }) {
                    Icon(painterResource(R.drawable.baseline_add_24), "Aumentar")
                }
                IconButton(onClick = onResetVolume) {
                    Icon(painterResource(R.drawable.fa7_solid_rotate_back), "Resetar")
                }
            }
        }
    }
}

@Preview
@Composable
private fun SoundBackgroundControlsLightPreview() {
    FullFocusTheme(
        dynamicColor = false,
        darkTheme = false
    ) {

        SoundBackgroundControls(
            volume = 10f,
            onResetVolume = {},
            onChangeVolume = {}
        )
    }
}

@Preview
@Composable
private fun SoundBackgroundControlsDarkPreview() {
    FullFocusTheme(
        dynamicColor = false,
        darkTheme = true
    ) {
        SoundBackgroundControls(
            volume = 10f,
            onResetVolume = {},
            onChangeVolume = {}
        )
    }
}

