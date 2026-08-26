package com.joseleandro.fullfocus.ui.screen.score

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.joseleandro.fullfocus.R
import com.joseleandro.fullfocus.domain.model.HeatMapDataDomain
import com.joseleandro.fullfocus.domain.model.HeatMapDomain
import com.joseleandro.fullfocus.ui.event.ScoreEvent
import com.joseleandro.fullfocus.ui.screen.score.component.BottomAxisLabelKey
import com.joseleandro.fullfocus.ui.screen.score.component.ScoreCartesianChartCard
import com.joseleandro.fullfocus.ui.screen.score.component.ScoreHeatMapCalendar
import com.joseleandro.fullfocus.ui.screen.score.component.ScoreWeeklyGoalCard
import com.joseleandro.fullfocus.ui.screen.score.component.heatMapColor
import com.joseleandro.fullfocus.ui.state.ScoreUiState
import com.joseleandro.fullfocus.ui.theme.FullFocusTheme
import com.kizitonwose.calendar.compose.heatmapcalendar.rememberHeatMapCalendarState
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.compose.cartesian.data.columnModel
import org.koin.compose.viewmodel.koinViewModel
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun ScoreScreen(
    openDrawer: () -> Unit
) {

    val viewModel = koinViewModel<ScoreViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.onEvent(event = ScoreEvent.OnLoad)
    }

    ScoreScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        openDrawer = openDrawer
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScoreScreen(
    uiState: ScoreUiState,
    onEvent: (ScoreEvent) -> Unit,
    openDrawer: () -> Unit
) {

    val modelProducer = remember { CartesianChartModelProducer() }

    val currentMonth = remember { YearMonth.now() }

    val heatMapState = rememberHeatMapCalendarState(
        startMonth = currentMonth.withMonth(1),
        endMonth = currentMonth.withMonth(12),
        firstDayOfWeek = firstDayOfWeekFromLocale(),
        firstVisibleMonth = currentMonth
    )

    LaunchedEffect(uiState.chartData) {
        if (uiState.chartData.isEmpty()) return@LaunchedEffect

        modelProducer.runTransaction {
            columnModel {
                series(uiState.chartData.values)
                extras { it[BottomAxisLabelKey] = uiState.chartData.keys.toList() }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("") },
                navigationIcon = {
                    IconButton(
                        onClick = openDrawer
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
                .padding(top = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            ScoreWeeklyGoalCard(
                current = uiState.weeklyGoalCurrent,
                total = uiState.weeklyGoal,
                streak = uiState.dailyStreak,
                totalHours = uiState.totalHours,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Column(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Text(
                    text = stringResource(R.string.consistencia_de_atividade),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = stringResource(R.string.frequencia_de_sessoes_de_foco_ao_longo_do_ano),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            ScoreHeatMapCalendar(
                state = heatMapState,
                value = uiState.heatMapData,
                selectedDate = uiState.selectedDate,
                onDateSelected = { onEvent(ScoreEvent.OnDateSelected(it)) }
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = stringResource(R.string.menos),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.size(4.dp))
                HeatMapDomain.entries.forEach { heatMap ->
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 1.dp)
                            .size(12.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(heatMapColor(heatMap))
                    )
                }
                Spacer(modifier = Modifier.size(4.dp))
                Text(
                    text = stringResource(R.string.mais),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            ScoreCartesianChartCard(
                modelProducer = modelProducer,
                weekFocusTime = uiState.weekFocusTime,
                onWeekFocusTimeChange = {
                    onEvent(ScoreEvent.OnWeekFocusTimeChange(it))
                }
            )

            Spacer(
                modifier = Modifier.height(24.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ScoreScreenLightPreview() {
    FullFocusTheme(
        dynamicColor = false,
        darkTheme = false
    ) {
        Surface {
            ScoreScreen(
                uiState = ScoreUiState(
                    chartData = dummyChartData,
                    heatMapData = dummyHeatMapData
                ),
                onEvent = {},
                openDrawer = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ScoreScreenDarkPreview() {
    FullFocusTheme(
        dynamicColor = false,
        darkTheme = true
    ) {
        Surface {
            ScoreScreen(
                uiState = ScoreUiState(
                    chartData = dummyChartData,
                    heatMapData = dummyHeatMapData
                ),
                onEvent = {},
                openDrawer = {}
            )
        }
    }
}

private val dummyChartData = mapOf(
    "Dom" to 30L * 60_000,
    "Seg" to 45L * 60_000,
    "Ter" to 20L * 60_000
)

private val dummyHeatMapData = listOf(
    HeatMapDataDomain(LocalDate.now(), 10L, HeatMapDomain.HEAT_MAP_1),
    HeatMapDataDomain(LocalDate.now().minusDays(1), 25L, HeatMapDomain.HEAT_MAP_2),
    HeatMapDataDomain(LocalDate.now().minusDays(2), 45L, HeatMapDomain.HEAT_MAP_3),
)

