package com.joseleandro.fullfocus.ui.screen.score

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.joseleandro.fullfocus.R
import com.joseleandro.fullfocus.ui.event.ScoreEvent
import com.joseleandro.fullfocus.ui.state.ScoreUiState
import com.joseleandro.fullfocus.ui.theme.FullFocusTheme
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.compose.cartesian.data.columnModel
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
import org.koin.compose.viewmodel.koinViewModel
import java.time.DayOfWeek
import java.time.Instant
import java.time.ZoneId
import java.util.Locale
import java.time.format.TextStyle as JavaTextStyle

const val Y_DIVISOR = 60_000
private val BottomAxisLabelKey = ExtraStore.Key<List<DayOfWeek>>()
private val StartAxisValueFormatter = CartesianValueFormatter { _, value, _ ->
    "${(value / Y_DIVISOR).toInt()} min"
}
private val BottomAxisValueFormatter = CartesianValueFormatter { context, x, _ ->
    context.model.extraStore[BottomAxisLabelKey][x.toInt()].getDisplayName(
        JavaTextStyle.SHORT,
        Locale.getDefault()
    )
}
private val MarkerValueFormatter =
    DefaultCartesianMarker.ValueFormatter { _, targets ->
        val column = (targets[0] as ColumnCartesianLayerMarkerTarget).columns[0]
        buildAnnotatedString {
            withStyle(SpanStyle(column.color)) {
                val valueInMinutes = (column.entry.y / Y_DIVISOR).toInt()
                append("$valueInMinutes min")
            }
        }
    }

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
fun ScoreScreen() {

    val viewModel = koinViewModel<ScoreViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.onEvent(event = ScoreEvent.OnLoad)
    }

    ScoreScreen(
        uiState = uiState
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScoreScreen(
    uiState: ScoreUiState
) {

    val modelProducer = remember { CartesianChartModelProducer() }

    fun getDayOfWeek(timestamp: Long): DayOfWeek {
        return Instant
            .ofEpochMilli(timestamp)
            .atZone(ZoneId.systemDefault())
            .dayOfWeek
    }

    LaunchedEffect(uiState.dateTimeWithSessionGroup) {

        if (uiState.dateTimeWithSessionGroup.isNotEmpty()) {

            val grouped = uiState.dateTimeWithSessionGroup
                .entries
                .groupBy { (timestamp, _) -> getDayOfWeek(timestamp) }
                .mapValues { (_, entries) ->
                    entries.sumOf { (_, sessions) ->
                        sessions.sumOf { it.elapsedTime }
                    }
                }

            val daysSorted =
                listOf(DayOfWeek.SUNDAY) + DayOfWeek.entries.filter { it != DayOfWeek.SUNDAY }

            val data = daysSorted.associateWith { day ->
                grouped[day] ?: 0L
            }
            modelProducer.runTransaction {
                columnModel {
                    series(data.values)
                    extras { it[BottomAxisLabelKey] = data.keys.toList() }
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = {}
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.material_symbols_menu_rounded),
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(state = rememberScrollState())
                .padding(innerPadding)
                .padding(top = 16.dp)
        ) {

            OutlinedCard(
                modifier = Modifier.padding(horizontal = 16.dp),
                shape = MaterialTheme.shapes.large
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Tempo de Foco (Semanal)",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    CartesianChartHost(
                        placeholder = {
                            Text(
                                modifier = Modifier.align(alignment = Alignment.Center),
                                text = "Sem dados para mostrar"
                            )
                        },
                        chart = rememberCartesianChart(
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
                                        fontSize = 10.sp,
                                        color = MaterialTheme.colorScheme.primary
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
                                        fontSize = 10.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
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
                                valueFormatter = BottomAxisValueFormatter,
                                label = rememberTextComponent(
                                    style = TextStyle(
                                        fontSize = 10.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                ),
                                labelRotationDegrees = -45f
                            ),
                            marker = rememberMarker(MarkerValueFormatter),
                            layerPadding = {
                                CartesianLayerPadding(
                                    scalableStart = 8.dp,
                                    scalableEnd = 8.dp
                                )
                            },
                        ),
                        modelProducer = modelProducer
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun ScoreScreenLightPreview() {
    FullFocusTheme(
        dynamicColor = false,
        darkTheme = false
    ) {
        ScoreScreen(
            uiState = ScoreUiState()
        )
    }
}

@Preview
@Composable
private fun ScoreScreenDarkPreview() {
    FullFocusTheme(
        dynamicColor = false,
        darkTheme = true
    ) {
        ScoreScreen(
            uiState = ScoreUiState()
        )
    }
}

