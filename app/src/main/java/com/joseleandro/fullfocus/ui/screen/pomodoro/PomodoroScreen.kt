package com.joseleandro.fullfocus.ui.screen.pomodoro

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.joseleandro.fullfocus.R
import com.joseleandro.fullfocus.data.local.preferences.data.PomodoroStatus
import com.joseleandro.fullfocus.service.ACTION_PAUSE
import com.joseleandro.fullfocus.service.ACTION_PLAY
import com.joseleandro.fullfocus.service.ACTION_RESET
import com.joseleandro.fullfocus.service.ACTION_SKIP
import com.joseleandro.fullfocus.service.ACTION_START
import com.joseleandro.fullfocus.service.PomodoroService
import com.joseleandro.fullfocus.ui.event.PomodoroEvent
import com.joseleandro.fullfocus.ui.screen.pomodoro.component.PomodoroTimer
import com.joseleandro.fullfocus.ui.screen.pomodoro_setting.PomodoroSettingBottomSheet
import com.joseleandro.fullfocus.ui.state.PomodoroUiState
import com.joseleandro.fullfocus.ui.theme.FullFocusTheme
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun PomodoroScreen(
    modifier: Modifier = Modifier,
    openDrawer: () -> Unit
) {
    val viewModel: PomodoroViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    PomodoroScreen(
        modifier = modifier,
        uiState = uiState,
        onEvent = viewModel::onEvent,
        openDrawer = openDrawer
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PomodoroScreen(
    modifier: Modifier = Modifier,
    uiState: PomodoroUiState,
    onEvent: (PomodoroEvent) -> Unit,
    openDrawer: () -> Unit
) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        stringResource(R.string.pomodoro),
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = openDrawer) {
                        Icon(
                            painterResource(R.drawable.outline_menu_24),
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            onEvent(PomodoroEvent.OnShowPomodoroSettingBottomSheet(true))
                        }
                    ) {
                        Icon(
                            painterResource(R.drawable.outline_settings_24),
                            contentDescription = null
                        )
                    }
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.surface
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            BoxWithConstraints(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                val size = (maxWidth * .8f).coerceAtMost(340.dp)

                PomodoroTimer(
                    size = size,
                    time = uiState.time,
                    statusSession = uiState.statusSession,
                    timeSession = uiState.timeSession,
                    supportText = "${uiState.currentSession}/4 sessões"
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconButton(
                    onClick = {
                        context.startPomodoroService(ACTION_SKIP)
                    }
                ) {
                    Icon(
                        painterResource(R.drawable.rounded_skip_next_24),
                        contentDescription = "Pular"
                    )
                }

                PomodoroButtonPrimary(
                    label = stringResource(id = getButtonLabel(uiState)),
                    iconRes = getButtonIcon(uiState),
                    onClick = {
                        when (uiState.pomodoroStatus) {

                            PomodoroStatus.START -> {
                                context.startPomodoroService(ACTION_START)
                            }

                            PomodoroStatus.PROGRESS -> {
                                if (uiState.isPlay) {
                                    context.startPomodoroService(ACTION_PAUSE)
                                } else {
                                    context.startPomodoroService(ACTION_PLAY)
                                }
                            }

                            PomodoroStatus.FINISHED -> {
                                context.startPomodoroService(ACTION_RESET)
                            }
                        }
                    }
                )

                IconButton(
                    onClick = {
                        context.startPomodoroService(ACTION_RESET)
                    },
                    enabled = uiState.pomodoroStatus != PomodoroStatus.START
                ) {
                    Icon(
                        painterResource(R.drawable.outline_restart_alt_24),
                        contentDescription = "Reiniciar"
                    )
                }
            }
        }
    }

    if (uiState.showPomodoroSettingBottomSheet) {
        PomodoroSettingBottomSheet(
            sheetState = bottomSheetState,
            onDismissRequest = {
                onEvent(PomodoroEvent.OnShowPomodoroSettingBottomSheet(false))
                scope.launch { bottomSheetState.hide() }
            }
        )
    }
}


@StringRes
fun getButtonLabel(uiState: PomodoroUiState): Int {
    return when (uiState.pomodoroStatus) {
        PomodoroStatus.START -> R.string.iniciar
        PomodoroStatus.PROGRESS -> if (uiState.isPlay) R.string.pausar else R.string.retomar
        PomodoroStatus.FINISHED -> R.string.recomecar
    }
}

@DrawableRes
fun getButtonIcon(uiState: PomodoroUiState): Int {
    return when (uiState.pomodoroStatus) {
        PomodoroStatus.START -> R.drawable.round_play_arrow_24
        PomodoroStatus.PROGRESS -> {
            if (uiState.isPlay) R.drawable.baseline_pause_24
            else R.drawable.round_play_arrow_24
        }

        PomodoroStatus.FINISHED -> R.drawable.outline_restart_alt_24
    }
}


fun Context.startPomodoroService(action: String) {
    val intent = Intent(this, PomodoroService::class.java).apply {
        this.action = action
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        startForegroundService(intent)
    } else {
        startService(intent)
    }
}


@Composable
fun PomodoroButtonPrimary(
    modifier: Modifier = Modifier,
    label: String,
    @DrawableRes iconRes: Int,
    onClick: () -> Unit,
) {
    Button(
        modifier = modifier.background(
            brush = Brush.linearGradient(
                listOf(
                    MaterialTheme.colorScheme.primary,
                    MaterialTheme.colorScheme.secondary
                )
            ),
            shape = MaterialTheme.shapes.extraLarge
        ),
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Icon(painterResource(iconRes), contentDescription = null)
        Spacer(modifier = Modifier.width(8.dp))
        Text(label)
    }
}

@Preview
@Composable
private fun PomodoroScreenLightPreview() {

    FullFocusTheme(
        dynamicColor = false,
        darkTheme = false
    ) {
        PomodoroScreen(
            uiState = PomodoroUiState(),
            openDrawer = {},
            onEvent = {}
        )
    }

}

@Preview
@Composable
private fun PomodoroScreenDarkPreview() {

    FullFocusTheme(
        dynamicColor = false,
        darkTheme = true
    ) {
        PomodoroScreen(
            uiState = PomodoroUiState(),
            openDrawer = {},
            onEvent = {}
        )
    }

}