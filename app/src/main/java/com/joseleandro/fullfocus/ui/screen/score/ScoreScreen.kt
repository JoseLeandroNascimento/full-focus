package com.joseleandro.fullfocus.ui.screen.score

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLocale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.joseleandro.fullfocus.R
import com.joseleandro.fullfocus.ui.event.ScoreEvent
import com.joseleandro.fullfocus.ui.state.ScoreUiState
import com.joseleandro.fullfocus.ui.theme.FullFocusTheme
import com.kizitonwose.calendar.compose.HeatMapCalendar
import com.kizitonwose.calendar.compose.heatmapcalendar.rememberHeatMapCalendarState
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
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
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.TextStyle as JavaTextStyle

const val Y_DIVISOR = 60_000
private val BottomAxisLabelKey = ExtraStore.Key<List<DayOfWeek>>()
private val StartAxisValueFormatter = CartesianValueFormatter { _, value, _ ->
    "${(value / Y_DIVISOR).toInt()} min"
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
        uiState = uiState,
        onEvent = viewModel::onEvent
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScoreScreen(
    uiState: ScoreUiState,
    onEvent: (ScoreEvent) -> Unit
) {


    val density = LocalDensity.current

    val modelProducer = remember { CartesianChartModelProducer() }

    fun getDayOfWeek(timestamp: Long): DayOfWeek {
        return Instant
            .ofEpochMilli(timestamp)
            .atZone(ZoneId.systemDefault())
            .dayOfWeek
    }

    @Composable
    fun heatMapColor(value: Int): Color =
        when (value) {
            0 -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = .1f)
            1 -> Color(0xFFC6E48B)
            2 -> Color(0xFF7BC96F)
            3 -> Color(0xFF239A3B)
            else -> Color(0xFF196127)
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


            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "Consistência de Atividade",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Frequência de sessões de foco ao longo do ano",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            val currentMonth = remember { YearMonth.now() }

            val state = rememberHeatMapCalendarState(
                startMonth = currentMonth.withMonth(1),
                endMonth = currentMonth.withMonth(12),
                firstDayOfWeek = firstDayOfWeekFromLocale(),
                firstVisibleMonth = currentMonth
            )

            HeatMapCalendar(
                modifier = Modifier.padding(horizontal = 16.dp),
                state = state,
                dayContent = { day, _ ->
                    val value = uiState.heatMapData[day.date] ?: 0
                    val isSelected = uiState.selectedHeatMapDate == day.date
                    val minutes = uiState.heatMapMinutes[day.date] ?: 0L

                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .padding(2.dp)
                            .size(20.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(4.dp))
                                .background(heatMapColor(value))
                                .then(
                                    if (isSelected) Modifier.background(
                                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                                        shape = RoundedCornerShape(4.dp)
                                    ) else Modifier
                                )
                                .clickable {
                                    onEvent(ScoreEvent.OnDateSelected(if (isSelected) null else day.date))
                                }
                        )

                        if (isSelected && minutes > 0) {
                            Popup(
                                alignment = Alignment.TopCenter,
                                offset = IntOffset(0, with(density) { -(28.dp.roundToPx()) })
                            ) {
                                Surface(
                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                    shape = RoundedCornerShape(4.dp),
                                    tonalElevation = 4.dp
                                ) {
                                    Text(
                                        text = "$minutes min",
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(
                                            horizontal = 6.dp,
                                            vertical = 2.dp
                                        )
                                    )
                                }
                            }
                        }
                    }
                },
                weekHeader = { dayOfWeek ->
                    val locale = LocalLocale.current.platformLocale
                    Text(
                        text = dayOfWeek.getDisplayName(JavaTextStyle.NARROW, locale),
                        fontSize = 10.sp,
                        modifier = Modifier.padding(horizontal = 4.dp),
                    )
                },
                monthHeader = { month ->
                    val locale = LocalLocale.current.platformLocale
                    Text(
                        text = month.yearMonth.month.getDisplayName(JavaTextStyle.SHORT, locale),
                        fontSize = 12.sp,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "Menos",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.size(4.dp))
                repeat(5) { level ->
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 1.dp)
                            .size(12.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(heatMapColor(level))
                    )
                }
                Spacer(modifier = Modifier.size(4.dp))
                Text(
                    text = "Mais",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(24.dp))


            OutlinedCard(
                modifier = Modifier.padding(horizontal = 16.dp),
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = .5f)
                ),
                shape = MaterialTheme.shapes.large
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Tempo de Foco Semanal",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Duração total acumulada por dia (em minutos)",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
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
                                valueFormatter = let {
                                    val locale = LocalLocale.current.platformLocale
                                    remember(locale) {
                                        CartesianValueFormatter { context, x, _ ->
                                            context.model.extraStore[BottomAxisLabelKey][x.toInt()].getDisplayName(
                                                JavaTextStyle.SHORT,
                                                locale
                                            )
                                        }
                                    }
                                },
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
            uiState = ScoreUiState(),
            onEvent = {}
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
            uiState = ScoreUiState(),
            onEvent = {}
        )
    }
}

