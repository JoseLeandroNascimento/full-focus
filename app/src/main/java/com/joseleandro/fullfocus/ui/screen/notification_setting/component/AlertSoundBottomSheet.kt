package com.joseleandro.fullfocus.ui.screen.notification_setting.component

import androidx.annotation.RawRes
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joseleandro.fullfocus.R
import com.joseleandro.fullfocus.data.local.preferences.model.SoundAlarm
import com.joseleandro.fullfocus.ui.theme.FullFocusTheme
import java.util.UUID

private data class AlertSoundOption(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    @get:RawRes val sound: Int?
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertSoundBottomSheet(
    onDismissRequest: () -> Unit,
    soundSelected: SoundAlarm? = null,
    onSoundSelected: (SoundAlarm?) -> Unit,
    sheetState: SheetState
) {

    val isPreview = LocalInspectionMode.current

    if (isPreview) {
        AlertSoundBottomSheetContent(
            soundSelected = soundSelected,
            onSoundSelected = onSoundSelected,
            onDismissRequest = onDismissRequest
        )
    } else {
        ModalBottomSheet(
            onDismissRequest = onDismissRequest,
            sheetState = sheetState
        ) {
            AlertSoundBottomSheetContent(
                soundSelected = soundSelected,
                onSoundSelected = onSoundSelected,
                onDismissRequest = onDismissRequest
            )
        }
    }
}

@Composable
private fun AlertSoundBottomSheetContent(
    modifier: Modifier = Modifier,
    soundSelected: SoundAlarm? = null,
    onDismissRequest: () -> Unit,
    onSoundSelected: (SoundAlarm?) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(.6f)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Selecione um som de alerta",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }

            IconButton(
                modifier = Modifier.align(Alignment.CenterEnd),
                onClick = onDismissRequest
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.material_symbols_close_rounded),
                    contentDescription = null
                )
            }
        }
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {

            item {
                CardItem(
                    label = "Silencioso",
                    selected = soundSelected == null,
                    onClick = {
                        onSoundSelected(null)
                    }
                )
            }

            items(items = SoundAlarm.entries, key = { it.index }) { option ->

                CardItem(
                    label = "Som ${option.index}",
                    selected = option == soundSelected,
                    onClick = {
                        onSoundSelected(option)
                    }
                )
            }
        }
    }
}

@Composable
private fun CardItem(
    modifier: Modifier = Modifier,
    label: String,
    selected: Boolean = false,
    onClick: () -> Unit
) {

    val transition = updateTransition(
        targetState = selected,
        label = "CardItemTransition"
    )

    val iconScaleAnim by transition.animateFloat(
        label = "IconScaleAnim",
        transitionSpec = {
            spring(
                dampingRatio = Spring.DampingRatioMediumBouncy
            )
        }
    ) { value ->
        when (value) {
            true -> 1.2f
            false -> .5f
        }
    }

    val iconAlphaAnim by transition.animateFloat(
        label = "IconAlphaAnim",
        transitionSpec = {
            tween(durationMillis = 500)
        }
    ) { value ->
        when (value) {
            true -> 1f
            false -> 0.5f
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = label,
            style = MaterialTheme.typography.bodyMedium
        )
        if (selected) {
            Icon(
                modifier = Modifier.graphicsLayer {
                    scaleX = iconScaleAnim
                    scaleY = iconScaleAnim
                    alpha = iconAlphaAnim
                },
                painter = painterResource(id = R.drawable.outline_check_small_24),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun AlertSoundBottomSheetLightPreview() {
    FullFocusTheme(
        dynamicColor = false,
        darkTheme = false
    ) {
        AlertSoundBottomSheet(
            sheetState = rememberModalBottomSheetState(),
            soundSelected = null,
            onSoundSelected = {},
            onDismissRequest = {}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun AlertSoundBottomSheetDarkPreview() {
    FullFocusTheme(
        dynamicColor = false,
        darkTheme = true
    ) {
        AlertSoundBottomSheet(
            sheetState = rememberModalBottomSheetState(),
            soundSelected = null,
            onSoundSelected = {},
            onDismissRequest = {}
        )
    }
}