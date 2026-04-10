package com.joseleandro.fullfocus.ui.screen.manage_tag

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.joseleandro.fullfocus.R
import com.joseleandro.fullfocus.ui.component.FullFocusFloatingActionButton
import com.joseleandro.fullfocus.ui.event.ManageTagEvent
import com.joseleandro.fullfocus.ui.screen.NavigationViewModel
import com.joseleandro.fullfocus.ui.screen.create_tag.CreateTagBottomSheet
import com.joseleandro.fullfocus.ui.screen.manage_tag.component.TagCard
import com.joseleandro.fullfocus.ui.state.ManageTagUiState
import com.joseleandro.fullfocus.ui.theme.FullFocusTheme
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ManageTagScreen() {

    val navigationViewModel: NavigationViewModel = koinViewModel()

    val viewModel: ManageTagViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.onEvent(ManageTagEvent.OnLoad)
    }

    ManageTagScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onNavigateBack = navigationViewModel::onBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageTagScreen(
    uiState: ManageTagUiState,
    onEvent: (ManageTagEvent) -> Unit,
    onNavigateBack: () -> Unit
) {

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val createTagBottomSheetState = rememberModalBottomSheetState()

    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_arrow_back_24),
                            contentDescription = null
                        )
                    }
                },
                title = {
                    Text(
                        text = stringResource(R.string.minhas_tags),
                        style = MaterialTheme.typography.titleMedium
                    )
                },
            )
        },
        floatingActionButton = {

            FullFocusFloatingActionButton(
                onClick = {
                    onEvent(ManageTagEvent.OnChangeVisibilityCreateTagBottomSheetShow(true))
                },
                iconRes = R.drawable.outline_add_24
            )
        },
        containerColor = MaterialTheme.colorScheme.surface
    ) { innerPadding ->

        when {

            uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            else -> {

                LazyColumn(
                    modifier = Modifier
                        .padding(innerPadding)
                        .nestedScroll(scrollBehavior.nestedScrollConnection),
                    contentPadding = PaddingValues(
                        bottom = 100.dp
                    )
                ) {

                    items(
                        items = uiState.tags,
                        key = { it.id }
                    ) { tag ->
                        TagCard(
                            tag = tag,
                            onEdit = {
                                onEvent(
                                    ManageTagEvent.OnChangeVisibilityCreateTagBottomSheetShow(
                                        true
                                    )
                                )
                            },
                            onDelete = { tag ->

                                onEvent(ManageTagEvent.OnDelete(tag))

                            }
                        )
                    }
                }
            }

        }

    }

    if (uiState.createTagBottomSheetShow) {
        CreateTagBottomSheet(
            sheetState = createTagBottomSheetState,
            onDismissRequest = {
                scope.launch {
                    createTagBottomSheetState.hide()
                }.invokeOnCompletion {
                    if (!createTagBottomSheetState.isVisible) {
                        onEvent(ManageTagEvent.OnChangeVisibilityCreateTagBottomSheetShow(false))
                    }
                }
            }
        )
    }

}


@Preview
@Composable
private fun ManageTagScreenLightPreview() {

    FullFocusTheme(
        dynamicColor = false,
        darkTheme = false
    ) {
        ManageTagScreen(
            uiState = ManageTagUiState(
                isLoading = false
            ),
            onEvent = {},
            onNavigateBack = {}
        )
    }

}

@Preview
@Composable
private fun ManageTagScreenDarkPreview() {

    FullFocusTheme(
        dynamicColor = false,
        darkTheme = true
    ) {
        ManageTagScreen(
            uiState = ManageTagUiState(),
            onEvent = {},
            onNavigateBack = {}
        )
    }

}