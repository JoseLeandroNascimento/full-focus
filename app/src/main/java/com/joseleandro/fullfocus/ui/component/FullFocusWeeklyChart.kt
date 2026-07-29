package com.joseleandro.fullfocus.ui.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joseleandro.fullfocus.R
import com.joseleandro.fullfocus.ui.screen.score.WeeklyHistoryData
import com.joseleandro.fullfocus.ui.theme.FullFocusTheme

@Composable
fun FullFocusWeeklyChart(
    modifier: Modifier = Modifier,
    activity: List<WeeklyHistoryData>,
    selectedPeriod: String = "Por semana",
    periodOptions: List<String> = listOf("Por semana", "Por mês"),
    onPeriodSelected: (String) -> Unit = {},
    barColor: Color = MaterialTheme.colorScheme.primary
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Tempo de foco",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                
                Box {
                    Card(
                        onClick = { expanded = true },
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        ),
                        shape = MaterialTheme.shapes.small
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = selectedPeriod,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Icon(
                                painter = painterResource(id = R.drawable.material_symbols_menu_rounded),
                                contentDescription = null,
                                modifier = Modifier.size(12.dp)
                            )
                        }
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        periodOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { 
                                    Text(
                                        text = option,
                                        style = MaterialTheme.typography.labelMedium
                                    ) 
                                },
                                onClick = {
                                    onPeriodSelected(option)
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                // Eixo Y (Lateral)
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(bottom = 24.dp, end = 8.dp),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.End
                ) {
                    listOf("12h", "8h", "4h", "0h").forEach { hour ->
                        Text(
                            text = hour,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                                fontSize = 10.sp
                            )
                        )
                    }
                }

                // Área do Gráfico
                Row(
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    activity.forEachIndexed { index, data ->
                        val animatedValue = remember { Animatable(0f) }
                        
                        LaunchedEffect(data.value) {
                            animatedValue.animateTo(
                                targetValue = data.value,
                                animationSpec = tween(durationMillis = 1000, delayMillis = index * 50)
                            )
                        }

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Bottom,
                            modifier = Modifier.weight(1f)
                        ) {
                            // Valor em cima da barra
                            Text(
                                text = data.timeLabel.ifEmpty { "${(data.value * 12).toInt()}h" },
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (index == activity.lastIndex) barColor else MaterialTheme.colorScheme.onSurfaceVariant
                                ),
                                modifier = Modifier.padding(bottom = 4.dp)
                            )

                            Canvas(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                                    .padding(horizontal = 4.dp)
                            ) {
                                val barWidth = size.width
                                val barHeight = size.height * animatedValue.value
                                
                                drawRoundRect(
                                    color = barColor.copy(alpha = if (index == activity.lastIndex) 1f else 0.2f),
                                    topLeft = Offset(0f, size.height - barHeight),
                                    size = Size(barWidth, barHeight),
                                    cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
                                )
                            }
                            
                            // Dia da semana / Label em baixo
                            Text(
                                text = data.label,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Medium
                                ),
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun FullFocusWeeklyChartPreview() {
    FullFocusTheme(darkTheme = false, dynamicColor = false) {
        FullFocusWeeklyChart(
            activity = listOf(
                WeeklyHistoryData("7-13", "Abr", 0.6f),
                WeeklyHistoryData("14-20", "Abr", 0.75f),
                WeeklyHistoryData("21-27", "Abr", 0.85f),
                WeeklyHistoryData("19-25", "Mai", 0.95f, "10h 45m")
            )
        )
    }
}
