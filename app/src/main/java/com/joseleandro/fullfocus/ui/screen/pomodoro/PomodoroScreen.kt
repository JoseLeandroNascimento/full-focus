package com.joseleandro.fullfocus.ui.screen.pomodoro

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.joseleandro.fullfocus.R
import com.joseleandro.fullfocus.domain.enums.SessionStatus
import com.joseleandro.fullfocus.ui.event.PomodoroEvent
import com.joseleandro.fullfocus.ui.screen.pomodoro.component.PomodoroTimer
import com.joseleandro.fullfocus.ui.screen.pomodoro_setting.PomodoroSettingBottomSheet
import com.joseleandro.fullfocus.ui.state.PomodoroUiState
import com.joseleandro.fullfocus.ui.theme.FullFocusTheme
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun PomodoroScreen(modifier: Modifier = Modifier) {

    val viewModel: PomodoroViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()


    PomodoroScreen(
        modifier = modifier,
        uiState = uiState,
        onEvent = viewModel::onEvent
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PomodoroScreen(
    modifier: Modifier = Modifier,
    uiState: PomodoroUiState,
    onEvent: (PomodoroEvent) -> Unit
) {

    val pomodoroSettingBottomSheet = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )


    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.pomodoro),
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {}
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.outline_menu_24),
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
                            modifier = Modifier.size(20.dp),
                            painter = painterResource(id = R.drawable.outline_settings_24),
                            contentDescription = null
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    actionIconContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier.padding(innerPadding),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                BoxWithConstraints(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {

                    val width = this.maxWidth

                    PomodoroTimer(
                        size = (width * .8f).coerceAtMost(340.dp),
                        timeProgress = uiState.time,
                        timeMax = uiState.timeSession,
                        supportText = "1/4 sessões"
                    )
                }


                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(
                        space = 8.dp,
                        alignment = Alignment.CenterHorizontally
                    )
                ) {

                    IconButton(
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.primary.copy(
                                alpha = .1f
                            )
                        ),
                        onClick = {}
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.rounded_skip_next_24),
                            contentDescription = null
                        )
                    }

                    val labelButton = when (uiState.sessionStatus) {
                        SessionStatus.START -> R.string.iniciar
                        SessionStatus.PROGRESS -> {
                            if (uiState.isPlay) {
                                R.string.pausar
                            } else {
                                R.string.retomar
                            }
                        }

                        SessionStatus.FINISHED -> R.string.recomecar
                    }

                    PomodoroButtonPrimary(
                        label = stringResource(
                            id = labelButton
                        ),
                        iconRes = if (uiState.isPlay) R.drawable.baseline_pause_24 else R.drawable.round_play_arrow_24,
                        onClick = {

                            if (uiState.isPlay) {
                                onEvent(PomodoroEvent.OnPause)
                            } else {
                                onEvent(PomodoroEvent.OnPlay)
                            }

                        }
                    )


                    IconButton(
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.primary.copy(
                                alpha = .1f
                            )
                        ),
                        onClick = {
                            onEvent(PomodoroEvent.OnRestart)
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.outline_restart_alt_24),
                            contentDescription = null
                        )
                    }
                }

            }
        }
    }


    LaunchedEffect(uiState.showPomodoroSettingBottomSheet) {

        if (uiState.showPomodoroSettingBottomSheet) {
            pomodoroSettingBottomSheet.expand()
        } else {
            pomodoroSettingBottomSheet.hide()
        }
    }

    if (uiState.showPomodoroSettingBottomSheet) {

        PomodoroSettingBottomSheet(
            sheetState = pomodoroSettingBottomSheet,
            onDismissRequest = {
                onEvent(PomodoroEvent.OnShowPomodoroSettingBottomSheet(false))
            }
        )
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
                colors = listOf(
                    MaterialTheme.colorScheme.primary,
                    MaterialTheme.colorScheme.secondary
                )
            ),
            shape = MaterialTheme.shapes.extraLarge
        ),
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent
        ),
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = null
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge
        )
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
            onEvent = {}
        )
    }

}