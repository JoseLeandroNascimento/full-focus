package com.joseleandro.fullfocus.ui.screen.create_tag

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.joseleandro.fullfocus.R
import com.joseleandro.fullfocus.ui.component.FullFocusBottomSheet
import com.joseleandro.fullfocus.ui.component.FullFocusBottomSheetHeader
import com.joseleandro.fullfocus.ui.component.FullFocusTextField
import com.joseleandro.fullfocus.ui.event.CreateTagEvent
import com.joseleandro.fullfocus.ui.state.CreateTagUiState
import com.joseleandro.fullfocus.ui.theme.FullFocusTheme
import kotlinx.coroutines.flow.filter
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTagBottomSheet(
    modifier: Modifier = Modifier,
    idTag: Int? = null,
    sheetState: SheetState,
    onDismissRequest: () -> Unit
) {

    val viewModel: CreateTagViewModel = koinViewModel()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.onEvent(CreateTagEvent.OnLoad(id = idTag))
    }

    CreateTagBottomSheet(
        modifier = modifier,
        sheetState = sheetState,
        uiState = uiState.value,
        onEvent = viewModel::onEvent,
        onDismissRequest = {
            viewModel.onEvent(CreateTagEvent.OnReset)
            onDismissRequest()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTagBottomSheet(
    modifier: Modifier = Modifier,
    uiState: CreateTagUiState,
    onEvent: (CreateTagEvent) -> Unit,
    sheetState: SheetState,
    onDismissRequest: () -> Unit
) {

    val nameFocus = remember { FocusRequester() }

    LaunchedEffect(Unit) {

        snapshotFlow { sheetState.isVisible }
            .filter { it }
            .collect {
                nameFocus.requestFocus()
            }
    }

    FullFocusBottomSheet(
        modifier = modifier,
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
        header = {
            FullFocusBottomSheetHeader(
                title = {
                    Text(
                        text = stringResource(R.string.nova_tag),
                    )
                },
                leadingIcon = {
                    IconButton(
                        onClick = onDismissRequest
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_close_24),
                            contentDescription = null
                        )
                    }
                },
                trailingIcon = {
                    TextButton(
                        onClick = {
                            onEvent(
                                CreateTagEvent.OnSave(onSuccess = {
                                    onDismissRequest()
                                })
                            )
                        }
                    ) {
                        Text(
                            text = stringResource(R.string.salvar)
                        )
                    }
                },
            )
        }
    ) {

        Box(
            contentAlignment = Alignment.Center
        ) {


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 32.dp)
            ) {

                FullFocusTextField(
                    modifier = Modifier.focusRequester(nameFocus),
                    label = stringResource(R.string.nome_da_tag),
                    value = uiState.form.name.value,
                    onValueChange = { value ->
                        onEvent(CreateTagEvent.OnNameChange(value))
                    },
                    error = uiState.form.name.messageError?.getMessage(),
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.round_bookmarks_24),
                            contentDescription = null
                        )
                    }
                )
            }

            if (uiState.isLoading)
                CircularProgressIndicator()

        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun CreateTagBottomSheetLightPreview() {
    FullFocusTheme(
        dynamicColor = false,
        darkTheme = false
    ) {
        CreateTagBottomSheet(
            sheetState = rememberModalBottomSheetState(),
            uiState = CreateTagUiState(),
            onEvent = {},
            onDismissRequest = {}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun CreateTagBottomSheetDarkPreview() {
    FullFocusTheme(
        dynamicColor = false,
        darkTheme = true
    ) {
        CreateTagBottomSheet(
            sheetState = rememberModalBottomSheetState(),
            uiState = CreateTagUiState(),
            onEvent = {},
            onDismissRequest = {}
        )
    }
}