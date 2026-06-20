package com.joseleandro.fullfocus.ui.screen.config_sound

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.joseleandro.fullfocus.R
import com.joseleandro.fullfocus.core.model.SettingSound
import com.joseleandro.fullfocus.data.local.preferences.model.SoundBackground
import com.joseleandro.fullfocus.ui.event.ConfigSoundEvent
import com.joseleandro.fullfocus.ui.screen.config_sound.component.ConfigSoundSegmentedButtonRow
import com.joseleandro.fullfocus.ui.screen.config_sound.component.ConfigSoundTopBar
import com.joseleandro.fullfocus.ui.screen.config_sound.component.SoundBackgroundControls
import com.joseleandro.fullfocus.ui.screen.config_sound.component.SoundItem
import com.joseleandro.fullfocus.ui.state.ConfigSoundUiState
import com.joseleandro.fullfocus.ui.theme.FullFocusTheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ConfigSoundScreen(
    typeSettingSound: SettingSound,
    viewModel: ConfigSoundViewModel = koinViewModel<ConfigSoundViewModel>(),
    onNavigateBack: () -> Unit,
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DisposableEffect(Unit) {
        onDispose {
            viewModel.onEvent(ConfigSoundEvent.StopPreview)
        }
    }

    LaunchedEffect(Unit) {

        val tabSelected = when (typeSettingSound) {
            SettingSound.SETTING_SOUND_BREAK -> TabConfigSound.BREAK_OPTIONS
            SettingSound.SETTING_SOUND_FOCUS -> TabConfigSound.FOCUS_OPTIONS
        }

        viewModel.onEvent(
            event = ConfigSoundEvent.OnSelectTab(
                tab = tabSelected
            )
        )
    }

    ConfigSoundScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onNavigateBack = onNavigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfigSoundScreen(
    uiState: ConfigSoundUiState,
    onEvent: (ConfigSoundEvent) -> Unit,
    onNavigateBack: () -> Unit,
) {

    Scaffold(
        topBar = {
            ConfigSoundTopBar(onNavigateBack = onNavigateBack)
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            ConfigSoundSegmentedButtonRow(
                uiState = uiState,
                onEvent = onEvent
            )

            SoundBackgroundControls(
                volume = uiState.currentVolume.toFloat(),
                onChangeVolume = {
                    onEvent(ConfigSoundEvent.ChangeVolume(it.toInt()))
                },
                onResetVolume = {
                    onEvent(ConfigSoundEvent.ResetVolume)
                }
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = stringResource(R.string.escolha_o_som_de_fundo),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(
                        count = SoundBackground.entries.size,
                        key = { index -> SoundBackground.entries[index].name })
                    { index ->
                        val sound = SoundBackground.entries[index]
                        val isSelected = uiState.selectedSound == sound

                        SoundItem(
                            sound = sound,
                            isSelected = isSelected,
                            isPreviewPlaying = uiState.isPreviewPlaying,
                            onSelect = {
                                onEvent(ConfigSoundEvent.ChangeSound(sound))
                            }
                        )
                    }
                }
            }
        }
    }
}


@Preview
@Composable
private fun ConfigSoundScreenLightPreview() {
    FullFocusTheme(
        dynamicColor = false,
        darkTheme = false
    ) {
        ConfigSoundScreen(
            uiState = ConfigSoundUiState(),
            onEvent = {},
            onNavigateBack = {}
        )
    }
}

@Preview
@Composable
private fun ConfigSoundScreenDarkPreview() {
    FullFocusTheme(
        dynamicColor = false,
        darkTheme = true
    ) {
        ConfigSoundScreen(
            uiState = ConfigSoundUiState(),
            onEvent = {},
            onNavigateBack = {}
        )
    }
}
