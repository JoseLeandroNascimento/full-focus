package com.joseleandro.fullfocus.ui.screen.pomodoro

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.joseleandro.fullfocus.R
import com.joseleandro.fullfocus.data.local.preferences.data.PomodoroStatus
import com.joseleandro.fullfocus.domain.data.tasksListMock
import com.joseleandro.fullfocus.service.ACTION_PAUSE
import com.joseleandro.fullfocus.service.ACTION_PLAY
import com.joseleandro.fullfocus.service.ACTION_RESTART
import com.joseleandro.fullfocus.service.ACTION_SKIP
import com.joseleandro.fullfocus.service.ACTION_START
import com.joseleandro.fullfocus.service.PomodoroService
import com.joseleandro.fullfocus.ui.event.PomodoroEvent
import com.joseleandro.fullfocus.ui.screen.pomodoro.component.EmptyTaskCard
import com.joseleandro.fullfocus.ui.screen.pomodoro.component.PomodoroControls
import com.joseleandro.fullfocus.ui.screen.pomodoro.component.PomodoroTimer
import com.joseleandro.fullfocus.ui.screen.pomodoro.component.SelectTaskBottomSheet
import com.joseleandro.fullfocus.ui.screen.pomodoro.component.SelectedTaskCard
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
        openDrawer = openDrawer,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PomodoroScreen(
    modifier: Modifier = Modifier,
    uiState: PomodoroUiState,
    onEvent: (PomodoroEvent) -> Unit,
    openDrawer: () -> Unit,
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

            if (uiState.taskCurrent != null) {
                SelectedTaskCard(
                    taskTitle = uiState.taskCurrent.title,
                    onResetTask = { onEvent(PomodoroEvent.OnResetTaskCurrentPomodoro) }
                )
            } else {
                EmptyTaskCard(
                    onClick = {
                        onEvent(PomodoroEvent.OnShowSelectTaskBottomSheet(true))
                    }
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .fillMaxHeight(),
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
                        timeSession = uiState.timeSession,
                        statusSession = uiState.statusSession,
                        supportText = "${uiState.currentSession}/${uiState.taskCurrent?.pomodoros ?: 0} sessões"
                    )
                }

                PomodoroControls(
                    uiState = uiState,
                    onStart = { context.startPomodoroService(ACTION_START) },
                    onPause = { context.startPomodoroService(ACTION_PAUSE) },
                    onPlay = { context.startPomodoroService(ACTION_PLAY) },
                    onReset = { context.startPomodoroService(ACTION_RESTART) },
                    onSkip = { context.startPomodoroService(ACTION_SKIP) }
                )
            }

        }
    }

    if (uiState.showSelectTaskBottomSheet) {
        SelectTaskBottomSheet(
            sheetState = bottomSheetState,
            tasks = uiState.tasks,
            onDismissRequest = {
                onEvent(PomodoroEvent.OnShowSelectTaskBottomSheet(false))
            },
            value = uiState.taskCurrent,
            onSelectTask = { taskId ->
                onEvent(PomodoroEvent.OnSelectTask(taskId))
            }
        )
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


@Preview
@Composable
private fun PomodoroScreenLightPreview() {

    FullFocusTheme(
        dynamicColor = false,
        darkTheme = false
    ) {
        PomodoroScreen(
            uiState = PomodoroUiState(
                taskCurrent = tasksListMock.first()
            ),
            openDrawer = {},
            onEvent = {},
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
            uiState = PomodoroUiState(
                taskCurrent = tasksListMock.first()
            ),
            openDrawer = {},
            onEvent = {},
        )
    }

}