package com.joseleandro.fullfocus.ui.screen.pomodoro

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
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
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.joseleandro.fullfocus.R
import com.joseleandro.fullfocus.domain.data.tasksListMock
import com.joseleandro.fullfocus.ui.event.PomodoroActionControlsEvent
import com.joseleandro.fullfocus.ui.event.PomodoroEvent
import com.joseleandro.fullfocus.ui.screen.pomodoro.component.ConfirmCancelSessionDialog
import com.joseleandro.fullfocus.ui.screen.pomodoro.component.EmptyTaskCard
import com.joseleandro.fullfocus.ui.screen.pomodoro.component.PomodoroControls
import com.joseleandro.fullfocus.ui.screen.pomodoro.component.PomodoroSessionTimeline
import com.joseleandro.fullfocus.ui.screen.pomodoro.component.PomodoroTimer
import com.joseleandro.fullfocus.ui.screen.pomodoro.component.SelectTaskBottomSheet
import com.joseleandro.fullfocus.ui.screen.pomodoro.component.SelectedTaskCard
import com.joseleandro.fullfocus.ui.screen.pomodoro_setting.PomodoroSettingBottomSheet
import com.joseleandro.fullfocus.ui.state.PomodoroModalTypeUiState
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
                            onEvent(PomodoroEvent.OnShowModal(modal = PomodoroModalTypeUiState.Settings))
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

        BoxWithConstraints(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            val minHeight = maxHeight
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ConstraintLayout(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = minHeight)
                ) {

                    val (selectTaskContainerRef, progressPomodoroContainerRef) = createRefs()

                    AnimatedContent(
                        modifier = Modifier.constrainAs(
                            selectTaskContainerRef
                        ) {
                            top.linkTo(parent.top, margin = 32.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        },
                        targetState = uiState.taskCurrent,
                        label = "task_card_transition"
                    ) { task ->
                        if (task != null) {
                            SelectedTaskCard(
                                taskTitle = task.title,
                                onResetTask = { onEvent(PomodoroEvent.OnResetTaskCurrentPomodoro) }
                            )
                        } else {
                            EmptyTaskCard(
                                onClick = {
                                    onEvent(PomodoroEvent.OnShowModal(modal = PomodoroModalTypeUiState.SelectTask))
                                }
                            )
                        }
                    }

                    Column(
                        modifier = Modifier
                            .constrainAs(progressPomodoroContainerRef) {
                                top.linkTo(selectTaskContainerRef.bottom, margin = 24.dp)
                                bottom.linkTo(parent.bottom, margin = 24.dp)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
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
                                currentSession = uiState.currentSession,
                                totalSessions = uiState.taskCurrent?.pomodoros ?: 4
                            )
                        }
                        Spacer(
                            modifier = Modifier.height(40.dp)
                        )
                        PomodoroSessionTimeline(
                            modifier = Modifier.height(28.dp),
                            totalSessions = uiState.taskCurrent?.pomodoros ?: 4,
                            currentSession = uiState.currentSession + 1,
                            statusSession = uiState.statusSession
                        )
                        Spacer(
                            modifier = Modifier.height(24.dp)
                        )
                        PomodoroControls(
                            uiState = uiState,
                            onActionControlPomodoro = { event ->
                                onEvent(
                                    PomodoroEvent.OnActionPomodoro(
                                        context = context,
                                        actionEvent = event
                                    )
                                )
                            },
                            onEvent = onEvent
                        )
                    }
                }
            }
        }
    }

    PomodoroModals(
        uiState = uiState,
        onEvent = onEvent,
        onActionControlPomodoro = { eventControl ->
            onEvent(PomodoroEvent.OnActionPomodoro(context = context, actionEvent = eventControl))
        }
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PomodoroModals(
    uiState: PomodoroUiState,
    onEvent: (PomodoroEvent) -> Unit,
    onActionControlPomodoro: (PomodoroActionControlsEvent) -> Unit
) {
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    when (uiState.activeModal) {
        PomodoroModalTypeUiState.ConfirmCancel -> {
            ConfirmCancelSessionDialog(
                onDismissRequest = { onEvent(PomodoroEvent.CloseModal) },
                onConfirm = { isFinished ->
                    onEvent(PomodoroEvent.CloseModal)
                    if (isFinished) {
                        onActionControlPomodoro(PomodoroActionControlsEvent.OnCancelCompleted)
                    } else {
                        onActionControlPomodoro(PomodoroActionControlsEvent.OnCancelDiscard)
                    }
                }
            )
        }

        PomodoroModalTypeUiState.SelectTask -> {
            SelectTaskBottomSheet(
                sheetState = bottomSheetState,
                tasks = uiState.tasks,
                onDismissRequest = {
                    onEvent(PomodoroEvent.CloseModal)
                },
                value = uiState.taskCurrent,
                onSelectTask = { taskId ->
                    onEvent(PomodoroEvent.OnSelectTask(taskId))
                }
            )
        }

        PomodoroModalTypeUiState.Settings -> {
            PomodoroSettingBottomSheet(
                sheetState = bottomSheetState,
                onDismissRequest = {
                    onEvent(PomodoroEvent.CloseModal)
                    scope.launch { bottomSheetState.hide() }
                }
            )
        }

        PomodoroModalTypeUiState.None -> { /* Nada a exibir */
        }
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
                taskCurrent = tasksListMock[2]
            ),
            openDrawer = {},
            onEvent = {},
        )
    }

}