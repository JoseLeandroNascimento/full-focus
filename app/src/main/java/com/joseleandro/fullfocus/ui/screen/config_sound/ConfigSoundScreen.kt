package com.joseleandro.fullfocus.ui.screen.config_sound

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.joseleandro.fullfocus.R
import com.joseleandro.fullfocus.core.model.SettingSound
import com.joseleandro.fullfocus.data.local.preferences.model.SoundBackground
import com.joseleandro.fullfocus.ui.event.ConfigSoundEvent
import com.joseleandro.fullfocus.ui.state.ConfigSoundUiState
import com.joseleandro.fullfocus.ui.theme.FullFocusTheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ConfigSoundScreen(
    typeSettingSound: SettingSound,
    onNavigateBack: () -> Unit,
) {

    val viewModel = koinViewModel<ConfigSoundViewModel>()
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

    var sliderValue by remember(uiState.currentVolume) {
        mutableFloatStateOf(uiState.currentVolume.toFloat())
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Sons do Cronômetro",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            painter = painterResource(id = R.drawable.outline_arrow_back_ios_new_24),
                            contentDescription = "Voltar"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Focus/Pause Selector
            SingleChoiceSegmentedButtonRow(
                modifier = Modifier.fillMaxWidth()
            ) {
                TabConfigSound.entries.forEachIndexed { index, tab ->
                    SegmentedButton(
                        shape = SegmentedButtonDefaults.itemShape(
                            index = index,
                            count = TabConfigSound.entries.size
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

            Card(
                modifier = Modifier.fillMaxWidth(),
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
                            text = "Volume do Som",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "${sliderValue.toInt()}%",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Slider(
                        value = sliderValue,
                        onValueChange = {
                            sliderValue = it
                        },
                        onValueChangeFinished = {
                            onEvent(ConfigSoundEvent.ChangeVolume(sliderValue.toInt()))
                        },
                        valueRange = 0f..100f,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        IconButton(onClick = {
                            sliderValue = 0f
                            onEvent(ConfigSoundEvent.ChangeVolume(0))
                        }) {
                            Icon(painterResource(R.drawable.mingcute_volume_mute_line), "Mudo")
                        }
                        IconButton(onClick = {
                            val newVal = (sliderValue - 5).coerceAtLeast(0f)
                            sliderValue = newVal
                            onEvent(ConfigSoundEvent.ChangeVolume(newVal.toInt()))
                        }) {
                            Icon(painterResource(R.drawable.outline_remove_24), "Diminuir")
                        }
                        IconButton(onClick = {
                            val newVal = (sliderValue + 5).coerceAtMost(100f)
                            sliderValue = newVal
                            onEvent(ConfigSoundEvent.ChangeVolume(newVal.toInt()))
                        }) {
                            Icon(painterResource(R.drawable.baseline_add_24), "Aumentar")
                        }
                        IconButton(onClick = {
                            onEvent(ConfigSoundEvent.ResetVolume)
                        }) {
                            Icon(painterResource(R.drawable.fa7_solid_rotate_back), "Resetar")
                        }
                    }
                }
            }

            // Sound Selection List
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Escolha o som de fundo",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(SoundBackground.entries.size) { index ->
                        val sound = SoundBackground.entries[index]
                        val isSelected = uiState.selectedSound == sound

                        SoundItem(
                            sound = sound,
                            isSelected = isSelected,
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

@Composable
fun SoundItem(
    sound: SoundBackground,
    isSelected: Boolean,
    onSelect: () -> Unit,
) {
    OutlinedCard(
        onClick = onSelect,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.outlinedCardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
            contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
        ),
        border = CardDefaults.outlinedCardBorder(enabled = isSelected)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = sound.icon),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = sound.title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                )
                Text(
                    text = sound.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f) else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (isSelected) {
                Icon(
                    painter = painterResource(id = R.drawable.line_md_play_filled),
                    contentDescription = "Tocando",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
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
