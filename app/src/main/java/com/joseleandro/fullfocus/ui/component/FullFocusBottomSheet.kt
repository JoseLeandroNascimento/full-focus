package com.joseleandro.fullfocus.ui.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joseleandro.fullfocus.R
import com.joseleandro.fullfocus.ui.theme.FullFocusTheme

const val WIDTH_DRAG_HANDLE = 50f
const val HEIGHT_DRAG_HANDLE = 6f
const val RADIUS_DRAG_HANDLE = 8f
const val PADDING_VERTICAL_DRAG_HANDLE = 16f
const val WIDTH_AREA_ICON_HEADER = 30f

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FullFocusBottomSheet(
    modifier: Modifier = Modifier,
    sheetState: SheetState,
    onDismissRequest: () -> Unit,
    header: (@Composable () -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {

    val dragColor = MaterialTheme.colorScheme.onSurface.copy(alpha = .3f)

    ModalBottomSheet(
        modifier = modifier,
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
        containerColor = MaterialTheme.colorScheme.surface,
        dragHandle = {
            Canvas(
                modifier = Modifier
                    .width(WIDTH_DRAG_HANDLE.dp)
                    .padding(vertical = PADDING_VERTICAL_DRAG_HANDLE.dp)
            ) {
                drawRoundRect(
                    color = dragColor,
                    size = Size(width = size.width, height = HEIGHT_DRAG_HANDLE.dp.toPx()),
                    cornerRadius = CornerRadius(x = RADIUS_DRAG_HANDLE.dp.toPx())
                )
            }

        },
        tonalElevation = 1.dp
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {

            header?.invoke()

            content()

        }
    }
}

@Composable
fun FullFocusBottomSheetHeader(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null
) {

    val density = LocalDensity.current

    var maxSizePx by remember { mutableIntStateOf(0) }

    val defaultSize = WIDTH_AREA_ICON_HEADER.dp

    val sizeAreaIconHeader =
        if (maxSizePx == 0) defaultSize
        else with(density) { maxSizePx.toDp() }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .widthIn(sizeAreaIconHeader)
                .onSizeChanged {
                    if (it.width > maxSizePx) {
                        maxSizePx = it.width
                    }
                },
            contentAlignment = Alignment.CenterStart
        ) {
            leadingIcon?.let { leadingIcon ->

                leadingIcon()

            }
        }

        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center
        ) {
            ProvideTextStyle(
                value = MaterialTheme.typography.labelMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = .8f),
                    fontWeight = FontWeight.SemiBold
                )
            ) {
                title()
            }
        }

        Box(
            modifier = Modifier
                .widthIn(sizeAreaIconHeader)
                .onSizeChanged {
                    if (it.width > maxSizePx) {
                        maxSizePx = it.width
                    }
                },
            contentAlignment = Alignment.CenterEnd
        ) {
            trailingIcon?.invoke()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun FullFocusBottomSheetWithHeaderPreview() {
    FullFocusTheme(
        dynamicColor = false,
        darkTheme = false
    ) {

        FullFocusBottomSheet(
            sheetState = rememberModalBottomSheetState(),
            header = {
                FullFocusBottomSheetHeader(
                    leadingIcon = {
                        Icon(
                            modifier = Modifier.size(20.dp),
                            painter = painterResource(id = R.drawable.boxicons_seal_check),
                            contentDescription = null
                        )
                    },
                    title = {
                        Text(
                            text = "Título",
                        )
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = {}
                        ) {
                            Icon(
                                modifier = Modifier.size(16.dp),
                                painter = painterResource(id = R.drawable.baseline_close_24),
                                contentDescription = null
                            )
                        }
                    }
                )
            },
            onDismissRequest = {}
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp),
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = "Conteudo do bottom sheet",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun FullFocusBottomSheetLightPreview() {
    FullFocusTheme(
        dynamicColor = false,
        darkTheme = false
    ) {

        FullFocusBottomSheet(
            sheetState = rememberModalBottomSheetState(),
            onDismissRequest = {}
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Conteudo do bottom sheet",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun FullFocusBottomSheetDarkPreview() {
    FullFocusTheme(
        dynamicColor = false,
        darkTheme =
            true
    ) {

        FullFocusBottomSheet(
            sheetState = rememberModalBottomSheetState(),
            onDismissRequest = {}
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Conteudo do bottom sheet",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}