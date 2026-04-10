package com.joseleandro.fullfocus.ui.screen.list_tasks

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.joseleandro.fullfocus.R
import com.joseleandro.fullfocus.domain.data.TagDomain
import com.joseleandro.fullfocus.domain.data.tasksListMock
import com.joseleandro.fullfocus.ui.component.FullFocusFloatingActionButton
import com.joseleandro.fullfocus.ui.component.FullFocusTagFilterChip
import com.joseleandro.fullfocus.ui.event.ListTasksEvent
import com.joseleandro.fullfocus.ui.screen.create_tag.CreateTagBottomSheet
import com.joseleandro.fullfocus.ui.screen.create_task.CreateTaskBottomSheet
import com.joseleandro.fullfocus.ui.screen.list_tasks.component.CreateOptionsDialog
import com.joseleandro.fullfocus.ui.screen.list_tasks.component.TaskCard
import com.joseleandro.fullfocus.ui.state.ListTasksFilter
import com.joseleandro.fullfocus.ui.state.ListTasksUiState
import com.joseleandro.fullfocus.ui.theme.FullFocusTheme
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ListTasksScreen(
    openDrawer: () -> Unit
) {

    val viewModel: ListTasksViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.onEvent(ListTasksEvent.OnLoad)
    }

    ListTasksScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        openDrawer = openDrawer
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListTasksScreen(
    uiState: ListTasksUiState,
    onEvent: (ListTasksEvent) -> Unit,
    openDrawer: () -> Unit
) {

    var showConfirmActionDialog by rememberSaveable {
        mutableStateOf(false)
    }

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    val createTaskBottomSheetState = rememberModalBottomSheetState()

    val createTagBottomSheetState = rememberModalBottomSheetState()

    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                scrollBehavior = scrollBehavior,
                title = {
                    Text(
                        text = stringResource(R.string.minhas_tarefas),
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = openDrawer
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

                        }
                    ) {
                        Icon(
                            modifier = Modifier.size(20.dp),
                            painter = painterResource(id = R.drawable.baseline_more_vert_24),
                            contentDescription = null
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    actionIconContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        floatingActionButton = {

            FullFocusFloatingActionButton(
                onClick = {
                    showConfirmActionDialog = true
                },
                iconRes = R.drawable.outline_add_24
            )
        },
        containerColor = MaterialTheme.colorScheme.surface

    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {

            when {
                uiState.isLoading -> {
                    CircularProgressIndicator()
                }

                else -> {

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(bottom = 100.dp)
                    ) {

                        item {

                            Column() {

                                Text(
                                    modifier = Modifier.padding(horizontal = 16.dp),
                                    text = stringResource(R.string.filtrar_por),
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = .7f)
                                    ),
                                )

                                TagsFilterRow(
                                    modifier = Modifier.padding(vertical = 8.dp),
                                    tags = uiState.tags,
                                    currentTag = uiState.filter.tag,
                                    onChangeValue = {
                                        onEvent(
                                            ListTasksEvent.OnFilter(
                                                ListTasksFilter(tag = it?.id)
                                            )
                                        )
                                    }
                                )
                            }
                        }

                        items(items = uiState.tasks, key = { it.id }) { task ->
                            TaskCard(
                                modifier = Modifier
                                    .padding(horizontal = 16.dp)
                                    .animateItem(
                                        fadeInSpec = tween(300),
                                        fadeOutSpec = tween(300)
                                    ),
                                task = task
                            )
                        }
                    }
                }
            }
        }
    }

    if (showConfirmActionDialog) {
        CreateOptionsDialog(
            onCreateTask = {
                showConfirmActionDialog = false
                onEvent(ListTasksEvent.OnChangeVisibilityCreateTaskBottomSheetShow(true))
            },
            onCreateTag = {
                showConfirmActionDialog = false
                onEvent(ListTasksEvent.OnChangeVisibilityCreateTagBottomSheetShow(true))
            },
            onDismiss = {
                showConfirmActionDialog = false
            }
        )
    }


    if (uiState.createTagBottomSheetShow) {
        CreateTagBottomSheet(
            sheetState = createTagBottomSheetState,
            onDismissRequest = {
                scope.launch {
                    createTagBottomSheetState.hide()
                }.invokeOnCompletion {
                    if (!createTagBottomSheetState.isVisible) {
                        onEvent(ListTasksEvent.OnChangeVisibilityCreateTagBottomSheetShow(false))
                    }
                }
            }
        )
    }

    if (uiState.createTaskBottomSheetShow) {

        CreateTaskBottomSheet(
            sheetState = createTaskBottomSheetState,
            onDismissRequest = {
                scope.launch {
                    createTaskBottomSheetState.hide()
                }.invokeOnCompletion {
                    if (!createTaskBottomSheetState.isVisible) {
                        onEvent(ListTasksEvent.OnChangeVisibilityCreateTaskBottomSheetShow(false))
                    }
                }
            }
        )
    }
}


@Composable
fun TagsFilterRow(
    modifier: Modifier = Modifier, tags: List<TagDomain>,
    currentTag: Int? = null,
    onChangeValue: (TagDomain?) -> Unit,
) {

    LazyRow(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {


        item {

            FullFocusTagFilterChip(
                selected = currentTag == null,
                onClick = {
                    onChangeValue(null)
                },
                label = stringResource(R.string.todas)
            )
        }

        items(
            items = tags,
            key = { it.id }
        ) { tag ->
            FullFocusTagFilterChip(
                selected = currentTag == tag.id,
                onClick = {
                    onChangeValue(tag)
                },
                label = tag.name
            )
        }

    }
}

@Preview
@Composable
private fun ListTasksScreenLightPreview() {
    FullFocusTheme(
        dynamicColor = false,
        darkTheme = false
    ) {
        ListTasksScreen(
            uiState = ListTasksUiState(
                isLoading = false,
                tasks = tasksListMock
            ),
            openDrawer = {},
            onEvent = {}
        )
    }
}

@Preview
@Composable
private fun ListTasksScreenDarkPreview() {
    FullFocusTheme(
        dynamicColor = false,
        darkTheme = true
    ) {
        ListTasksScreen(
            uiState = ListTasksUiState(),
            openDrawer = {},
            onEvent = {}
        )
    }
}