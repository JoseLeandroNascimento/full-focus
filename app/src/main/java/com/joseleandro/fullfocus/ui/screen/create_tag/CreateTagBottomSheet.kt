package com.joseleandro.fullfocus.ui.screen.create_tag

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joseleandro.fullfocus.R
import com.joseleandro.fullfocus.ui.component.FullFocusBottomSheet
import com.joseleandro.fullfocus.ui.component.FullFocusBottomSheetHeader
import com.joseleandro.fullfocus.ui.component.FullFocusTextField
import com.joseleandro.fullfocus.ui.theme.FullFocusTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTagBottomSheet(
    modifier: Modifier = Modifier,
    sheetState: SheetState,
    onDismissRequest: () -> Unit
) {

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
                        onClick = {}
                    ) {
                        Text(
                            text = stringResource(R.string.salvar)
                        )
                    }
                },
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 32.dp)
        ) {

            FullFocusTextField(
                label = stringResource(R.string.nome_da_tag),
                value = "",
                onValueChange = {},
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.round_bookmarks_24),
                        contentDescription = null
                    )
                },
                trailingIcon = {
                    IconButton(
                        onClick = {}
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.outline_palette_24),
                            contentDescription = null
                        )
                    }
                }
            )
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
            onDismissRequest = {}
        )
    }
}