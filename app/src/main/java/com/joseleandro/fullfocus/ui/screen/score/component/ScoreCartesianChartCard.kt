package com.joseleandro.fullfocus.ui.screen.score.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joseleandro.fullfocus.R
import com.joseleandro.fullfocus.ui.screen.score.WeekFocusTime
import com.joseleandro.fullfocus.ui.theme.FullFocusTheme
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.compose.cartesian.layer.CartesianLayerPadding
import com.patrykandpatrick.vico.compose.cartesian.layer.ColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.marker.ColumnCartesianLayerMarkerTarget
import com.patrykandpatrick.vico.compose.cartesian.marker.DefaultCartesianMarker
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.common.DashedShape
import com.patrykandpatrick.vico.compose.common.Fill
import com.patrykandpatrick.vico.compose.common.Insets
import com.patrykandpatrick.vico.compose.common.MarkerCornerBasedShape
import com.patrykandpatrick.vico.compose.common.component.ShapeComponent
import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent
import com.patrykandpatrick.vico.compose.common.component.rememberShapeComponent
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import com.patrykandpatrick.vico.compose.common.data.ExtraStore

const val Y_DIVISOR = 60_000
val BottomAxisLabelKey = ExtraStore.Key<List<String>>()
private val StartAxisValueFormatter = CartesianValueFormatter { _, value, _ ->
    "${(value / Y_DIVISOR).toInt()} min"
}

private val MarkerValueFormatter = DefaultCartesianMarker.ValueFormatter { _, targets ->
    val column = (targets[0] as ColumnCartesianLayerMarkerTarget).columns[0]
    buildAnnotatedString {
        withStyle(SpanStyle(column.color)) {
            val valueInMinutes = (column.entry.y / Y_DIVISOR).toInt()
            append("$valueInMinutes min")
        }
    }
}

data class DropdownItem(
    val text: String,
    @get:DrawableRes val icon: Int,
    val value: WeekFocusTime
)

@Composable
fun rememberMarker(
    valueFormatter: DefaultCartesianMarker.ValueFormatter = DefaultCartesianMarker.ValueFormatter.default(),
): DefaultCartesianMarker {
    val label = rememberTextComponent(
        style = TextStyle(color = MaterialTheme.colorScheme.onSurface),
        background = rememberShapeComponent(
            fill = Fill(MaterialTheme.colorScheme.surface),
            shape = MarkerCornerBasedShape(CircleShape as CornerBasedShape),
        ),
        padding = Insets(horizontal = 8.dp, vertical = 4.dp),
    )
    return remember(label, valueFormatter) {
        DefaultCartesianMarker(
            label = label,
            valueFormatter = valueFormatter,
            indicator = { color ->
                ShapeComponent(fill = Fill(color), shape = CircleShape)
            },
        )
    }
}

@Composable
fun ScoreCartesianChartCard(
    modifier: Modifier = Modifier,
    modelProducer: CartesianChartModelProducer,
    weekFocusTime: WeekFocusTime,
    onWeekFocusTimeChange: (WeekFocusTime) -> Unit
) {

    var expandedDropdownMenu by remember { mutableStateOf(false) }

    val optionsMenu = listOf(
        DropdownItem(
            text = stringResource(R.string.semanal),
            icon = R.drawable.outline_calendar_view_week_24,
            value = WeekFocusTime.WEEKLY
        ),
        DropdownItem(
            text = stringResource(R.string.mensal),
            icon = R.drawable.outline_calendar_view_month_24,
            value = WeekFocusTime.MONTHLY
        ),
        DropdownItem(
            text = stringResource(R.string.anual),
            icon = R.drawable.outline_calendar_month_24,
            value = WeekFocusTime.YEARLY
        )
    )

    OutlinedCard(
        modifier = modifier.padding(horizontal = 16.dp),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = .5f)
        ),
        shape = MaterialTheme.shapes.large
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    val title = when (weekFocusTime) {
                        WeekFocusTime.WEEKLY -> R.string.tempo_de_foco_semanal
                        WeekFocusTime.MONTHLY -> R.string.tempo_de_foco_mensal
                        WeekFocusTime.YEARLY -> R.string.tempo_de_foco_anual
                    }
                    val subtitle = when (weekFocusTime) {
                        WeekFocusTime.WEEKLY -> R.string.duracao_total_acumulada_por_dia_em_minutos
                        WeekFocusTime.MONTHLY -> R.string.duracao_total_acumulada_por_semana_em_minutos
                        WeekFocusTime.YEARLY -> R.string.duracao_total_acumulada_por_mes_em_minutos
                    }

                    Text(
                        text = stringResource(title),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = stringResource(subtitle),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }

                Box() {
                    Icon(
                        modifier = Modifier
                            .clip(shape = CircleShape)
                            .clickable {
                                expandedDropdownMenu = !expandedDropdownMenu
                            }
                            .padding(4.dp),
                        painter = painterResource(id = R.drawable.outline_more_vert_24),
                        contentDescription = null
                    )

                    DropdownMenu(
                        expanded = expandedDropdownMenu,
                        onDismissRequest = { expandedDropdownMenu = false },
                        containerColor = MaterialTheme.colorScheme.surfaceContainer,
                        shape = MaterialTheme.shapes.extraLarge,
                        tonalElevation = 4.dp,
                        shadowElevation = 1.dp
                    ) {


                        optionsMenu.forEach { (label, icon, mode) ->
                            val isSelected = weekFocusTime == mode

                            DropdownMenuItem(
                                modifier = Modifier
                                    .padding(horizontal = 8.dp)
                                    .clip(shape = MaterialTheme.shapes.large),
                                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 2.dp),
                                leadingIcon = {
                                    Icon(
                                        painter = painterResource(id = icon),
                                        contentDescription = null,
                                        tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                },
                                text = {
                                    Text(
                                        text = label,
                                        style = MaterialTheme.typography.labelLarge,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                    )
                                },
                                colors = MenuDefaults.itemColors(
                                    leadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                    textColor = MaterialTheme.colorScheme.onSurface
                                ),
                                onClick = {
                                    expandedDropdownMenu = false
                                    onWeekFocusTimeChange(mode)
                                }
                            )
                        }
                    }
                }

            }

            ScoreCartesianChartHost(
                modelProducer = modelProducer
            )
        }
    }
}

@Composable
fun ScoreCartesianChartHost(
    modifier: Modifier = Modifier,
    modelProducer: CartesianChartModelProducer
) {
    CartesianChartHost(
        modifier = modifier,
        chart = rememberScoreCartesianChart(),
        modelProducer = modelProducer
    )
}

@Composable
private fun rememberScoreCartesianChart() = rememberCartesianChart(
    rememberColumnCartesianLayer(
        columnProvider = ColumnCartesianLayer.ColumnProvider.series(
            rememberLineComponent(
                fill = Fill(MaterialTheme.colorScheme.primary),
                thickness = 24.dp,
                shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
            )
        ),
        dataLabel = rememberTextComponent(
            style = TextStyle(
                fontSize = 10.sp, color = MaterialTheme.colorScheme.primary
            ),
            padding = Insets(bottom = 2.dp)
        ),
        dataLabelValueFormatter = CartesianValueFormatter { _, value, _ ->
            if (value > 0) "${(value / Y_DIVISOR).toInt()}" else ""
        }
    ),
    startAxis = VerticalAxis.rememberStart(
        valueFormatter = StartAxisValueFormatter,
        itemPlacer = VerticalAxis.ItemPlacer.step({ Y_DIVISOR.toDouble() }),
        label = rememberTextComponent(
            style = TextStyle(
                fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        ),
        guideline = rememberLineComponent(
            fill = Fill(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)),
            thickness = 1.dp,
            shape = DashedShape(dashLength = 4.dp, gapLength = 4.dp)
        )
    ),
    bottomAxis = HorizontalAxis.rememberBottom(
        itemPlacer = remember { HorizontalAxis.ItemPlacer.segmented() },
        valueFormatter = remember {
            CartesianValueFormatter { context, x, _ ->
                context.model.extraStore.getOrNull(BottomAxisLabelKey)
                    ?.getOrNull(x.toInt())
                    ?: ""
            }
        },
        label = rememberTextComponent(
            style = TextStyle(
                fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        ),
        labelRotationDegrees = -45f
    ),
    marker = rememberMarker(MarkerValueFormatter),
    layerPadding = {
        CartesianLayerPadding(
            scalableStart = 8.dp, scalableEnd = 8.dp
        )
    },
)

@Preview(name = "Light Theme", showBackground = true)
@Composable
private fun ScoreCartesianChartHostLightPreview() {
    FullFocusTheme(dynamicColor = false, darkTheme = false) {
        Surface {
            ScoreCartesianChartHost(
                modelProducer = remember { CartesianChartModelProducer() }
            )
        }
    }
}

@Preview(name = "Dark Theme", showBackground = true)
@Composable
private fun ScoreCartesianChartHostDarkPreview() {
    FullFocusTheme(dynamicColor = false, darkTheme = true) {
        Surface {
            ScoreCartesianChartHost(
                modelProducer = remember { CartesianChartModelProducer() }
            )
        }
    }
}
