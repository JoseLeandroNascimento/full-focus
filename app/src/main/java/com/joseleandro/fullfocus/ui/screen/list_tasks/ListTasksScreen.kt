package com.joseleandro.fullfocus.ui.screen.list_tasks

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joseleandro.fullfocus.R
import com.joseleandro.fullfocus.domain.data.TagDomain
import com.joseleandro.fullfocus.domain.data.tagsListMock
import com.joseleandro.fullfocus.domain.data.tasksListMock
import com.joseleandro.fullfocus.ui.component.FullFocusFloatingActionButton
import com.joseleandro.fullfocus.ui.screen.create_task.CreateTaskBottomSheet
import com.joseleandro.fullfocus.ui.component.FullFocusTagFilterChip
import com.joseleandro.fullfocus.ui.screen.list_tasks.component.TaskCard
import com.joseleandro.fullfocus.ui.theme.FullFocusTheme
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListTasksScreen() {

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val createTaskBottomSheetState = rememberModalBottomSheetState()
    var createTaskBottomSheetShow by remember { mutableStateOf(false) }

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
                    createTaskBottomSheetShow = true
                },
                iconRes = R.drawable.outline_add_24
            )
        },
        containerColor = MaterialTheme.colorScheme.surface,

        ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {

            item {

                Column() {
                    Text(
                        text = stringResource(R.string.filtrar_por),
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = .7f)
                        ),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    TagsFilterRow(
                        modifier = Modifier.padding(vertical = 8.dp),
                        tags = tagsListMock
                    )
                }
            }

            items(items = tasksListMock, key = { it.id }) { task ->
                TaskCard(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    task = task
                )
            }

        }

    }

    if (createTaskBottomSheetShow) {

        CreateTaskBottomSheet(
            sheetState = createTaskBottomSheetState,
            onDismissRequest = {
                createTaskBottomSheetShow = false
                scope.launch {
                    createTaskBottomSheetState.hide()
                }
            }
        )
    }
}


@Composable
fun TagsFilterRow(modifier: Modifier = Modifier, tags: List<TagDomain>) {

    LazyRow(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {


        item {

            FullFocusTagFilterChip(
                selected = true,
                onClick = {},
                label = stringResource(R.string.todas)
            )
        }

        items(
            items = tags,
            key = { it.id }
        ) { tag ->
            FullFocusTagFilterChip(
                selected = false,
                onClick = {},
                label = tag.name
            )
        }

        item {
            FilterChip(
                shape = MaterialTheme.shapes.extraLarge,
                selected = false,
                label = {
                    Icon(
                        painter = painterResource(id = R.drawable.outline_add_24),
                        contentDescription = null
                    )
                },
                onClick = {}
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
        ListTasksScreen()
    }
}

@Preview
@Composable
private fun ListTasksScreenDarkPreview() {
    FullFocusTheme(
        dynamicColor = false,
        darkTheme = true
    ) {
        ListTasksScreen()
    }
}