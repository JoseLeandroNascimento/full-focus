package com.joseleandro.fullfocus.ui.component

import android.content.res.Configuration
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.SnapLayoutInfoProvider
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joseleandro.fullfocus.ui.theme.FullFocusTheme
import kotlinx.coroutines.launch

val ITEM_HEIGHT = 50.dp

@Composable
fun FullFocusWheelPicker(
    modifier: Modifier = Modifier,
    items: List<String>,
    initialSelection: String? = null,
    visibleItemsCount: Int = if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE) 3 else 5,
    onItemSelected: (String) -> Unit
) {

    val initialIndex = remember(items, initialSelection) {
        val center = Int.MAX_VALUE / 2
        val offset = if (initialSelection != null) {
            items.indexOf(initialSelection).coerceAtLeast(0)
        } else 0
        if (items.isEmpty()) 0 else (center - (center % items.size)) + offset
    }

    BoxWithConstraints(
        modifier = modifier.height(ITEM_HEIGHT * visibleItemsCount),
        contentAlignment = Alignment.Center
    ) {
        val boxHeight = maxHeight
        val middlePadding = ((boxHeight - ITEM_HEIGHT) / 2).coerceAtLeast(0.dp)

        var selectedIndex by remember(items, initialSelection) {
            val index = if (initialSelection != null) {
                items.indexOf(initialSelection).coerceAtLeast(0)
            } else 0
            mutableIntStateOf(index)
        }

        val listState = rememberLazyListState(
            initialFirstVisibleItemIndex = initialIndex
        )

        val flingBehavior = rememberSnapFlingBehavior(
            snapLayoutInfoProvider = remember(listState) {
                SnapLayoutInfoProvider(
                    lazyListState = listState,
                    snapPosition = SnapPosition.Center
                )
            }
        )

        LazyColumn(
            state = listState,
            flingBehavior = flingBehavior,
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(
                vertical = middlePadding
            )
        ) {
            items(Int.MAX_VALUE) { index ->
                val itemIndex = index % items.size
                val isSelected = itemIndex == selectedIndex

                FullFocusWheelPickerItem(
                    index = index,
                    label = items[itemIndex],
                    selected = isSelected,
                    listState = listState
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(ITEM_HEIGHT)
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(12.dp)
                )
        )

        val derivedSelectedIndex = remember(items) {
            derivedStateOf {
                val layoutInfo = listState.layoutInfo
                val visibleItems = layoutInfo.visibleItemsInfo
                if (visibleItems.isEmpty()) return@derivedStateOf -1

                val center = (layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset) / 2

                visibleItems.minByOrNull {
                    val itemCenter = it.offset + it.size / 2
                    kotlin.math.abs(itemCenter - center)
                }?.index ?: -1
            }
        }

        LaunchedEffect(derivedSelectedIndex.value) {
            val index = derivedSelectedIndex.value
            if (index != -1 && items.isNotEmpty()) {
                val newIndex = index % items.size
                if (selectedIndex != newIndex) {
                    selectedIndex = newIndex
                    onItemSelected(items[newIndex])
                }
            }
        }
    }
}

@Composable
private fun FullFocusWheelPickerItem(
    modifier: Modifier = Modifier,
    label: String,
    listState: LazyListState,
    index: Int,
    selected: Boolean
) {

    val coroutineScope = rememberCoroutineScope()

    Text(
        text = label,
        modifier = modifier
            .fillMaxWidth()
            .height(ITEM_HEIGHT)
            .clickable {
                coroutineScope.launch {
                    listState.animateScrollToItem(index)
                }
            }
            .wrapContentHeight(Alignment.CenterVertically),
        textAlign = TextAlign.Center,
        fontSize = if (selected) 24.sp else 18.sp,
        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
        color = if (selected) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        }
    )
}

@Preview
@Composable
private fun FullFocusWheelPickerLightPreview() {

    FullFocusTheme(
        dynamicColor = false,
        darkTheme = false
    ) {
        FullFocusWheelPicker(
            items = listOf(
                "Janeiro",
                "Fevereiro",
                "Março",
                "Abril",
                "Maio",
                "Junho",
                "Julho",
                "Agosto",
                "Setembro",
                "Outubro",
                "Novembro",
                "Dezembro"
            ),
            initialSelection = "Março",
            onItemSelected = {
                println(it)
            }
        )
    }
}

@Preview
@Composable
private fun FullFocusWheelPickerDarkPreview() {

    FullFocusTheme(
        dynamicColor = false,
        darkTheme = true
    ) {
        FullFocusWheelPicker(
            items = listOf(
                "Janeiro",
                "Fevereiro",
                "Março",
                "Abril",
                "Maio",
                "Junho",
                "Julho",
                "Agosto",
                "Setembro",
                "Outubro",
                "Novembro",
                "Dezembro"
            ),
            initialSelection = "Maio",
            onItemSelected = {
                println(it)
            }
        )
    }
}