package com.joseleandro.fullfocus.ui.screen.config_sound.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.joseleandro.fullfocus.ui.event.ConfigSoundEvent
import com.joseleandro.fullfocus.ui.screen.config_sound.TabConfigSound
import com.joseleandro.fullfocus.ui.state.ConfigSoundUiState
import com.joseleandro.fullfocus.ui.theme.FullFocusTheme

@Composable
fun ConfigSoundSegmentedButtonRow(
    modifier: Modifier = Modifier,
    uiState: ConfigSoundUiState,
    onEvent: (ConfigSoundEvent) -> Unit
) {

    SingleChoiceSegmentedButtonRow(
        modifier = modifier.fillMaxWidth()
    ) {
        TabConfigSound.entries.forEachIndexed { index, tab ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(
                    index = index,
                    count = TabConfigSound.entries.size,
                    baseShape = MaterialTheme.shapes.extraLarge
                ),
                onClick = {
                    onEvent(ConfigSoundEvent.OnSelectTab(tab = tab))
                },
                selected = tab == uiState.selectedTab,
            ) {
                Text(
                    text = stringResource(id = tab.labelRes)
                )
            }
        }
    }
}

@Preview
@Composable
private fun ConfigSoundSegmentedButtonRowLightPreview() {
    FullFocusTheme(
        dynamicColor = false,
        darkTheme = false
    ) {
        ConfigSoundSegmentedButtonRow(
            uiState = ConfigSoundUiState(),
            onEvent = {}
        )
    }
}

@Preview
@Composable
private fun ConfigSoundSegmentedButtonRowDarkPreview() {
    FullFocusTheme(
        dynamicColor = false,
        darkTheme = true
    ) {
        ConfigSoundSegmentedButtonRow(
            uiState = ConfigSoundUiState(),
            onEvent = {}
        )
    }
}