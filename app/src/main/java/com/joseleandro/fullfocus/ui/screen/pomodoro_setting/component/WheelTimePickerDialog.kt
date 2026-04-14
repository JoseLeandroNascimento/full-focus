package com.joseleandro.fullfocus.ui.screen.pomodoro_setting.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@Composable
fun WheelTimePickerDialog(
    initialValue: Int,
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit,
    title: String = "Selecionar Tempo"
) {
    var selectedValue by remember { mutableIntStateOf(initialValue) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
        },
        text = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp),
                contentAlignment = Alignment.Center
            ) {
                // Linhas indicadoras fixas no centro
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 40.dp),
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
                    )
                    Box(modifier = Modifier.height(55.dp)) // Altura exata da zona de seleção
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 40.dp),
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
                    )
                }

                WheelPicker(
                    range = 1..60,
                    initialValue = initialValue,
                    onValueChanged = { selectedValue = it }
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(selectedValue) }) {
                Text("Confirmar", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun WheelPicker(
    range: IntRange,
    initialValue: Int,
    onValueChanged: (Int) -> Unit
) {
    val items = remember { range.toList() }
    val scope = rememberCoroutineScope()
    
    val initialPage = remember { items.indexOf(initialValue).coerceAtLeast(0) }
    val pagerState = rememberPagerState(
        initialPage = initialPage,
        pageCount = { items.size }
    )

    // Notifica o valor quando a página estabiliza no centro
    LaunchedEffect(pagerState.currentPage) {
        onValueChanged(items[pagerState.currentPage])
    }

    VerticalPager(
        state = pagerState,
        modifier = Modifier
            .height(240.dp)
            .fillMaxWidth(),
        // Padding vertical para que o item centralizado tenha espaço em cima e embaixo
        // Altura total (240) - Altura item (55) / 2 = 92.5
        contentPadding = PaddingValues(vertical = 92.5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) { page ->
        val isSelected = page == pagerState.currentPage
        
        // Efeito visual de escala e opacidade baseado na distância do centro
        val pageOffset = (
                (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
            ).absoluteValue

        Box(
            modifier = Modifier
                .height(55.dp)
                .fillMaxWidth()
                .graphicsLayer {
                    // Suaviza a transição de escala e transparência
                    val scale = lerp(
                        start = 0.7f,
                        stop = 1f,
                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                    )
                    scaleX = scale
                    scaleY = scale
                    alpha = lerp(
                        start = 0.3f,
                        stop = 1f,
                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                    )
                }
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    scope.launch {
                        pagerState.animateScrollToPage(page)
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = items[page].toString(),
                style = MaterialTheme.typography.displaySmall.copy(
                    fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                ),
                fontSize = 32.sp
            )
        }
    }
}