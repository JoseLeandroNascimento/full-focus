package com.joseleandro.fullfocus.ui.screen.score

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.joseleandro.fullfocus.R
import com.joseleandro.fullfocus.ui.component.FullFocusAchievementSection
import com.joseleandro.fullfocus.ui.component.FullFocusCalendarStrike
import com.joseleandro.fullfocus.ui.component.FullFocusHeroStreakCard
import com.joseleandro.fullfocus.ui.component.FullFocusMonthlyActivityCard
import com.joseleandro.fullfocus.ui.component.FullFocusStatCard
import com.joseleandro.fullfocus.ui.component.FullFocusWeeklyChart
import com.joseleandro.fullfocus.ui.theme.FullFocusTheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ScoreScreen() {

    val viewModel = koinViewModel<ScoreViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ScoreScreenContent(
        uiState = uiState,
        onPreviousMonth = viewModel::onPreviousMonth,
        onNextMonth = viewModel::onNextMonth,
        onChartPeriodSelected = viewModel::onChartPeriodSelected
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScoreScreenContent(
    uiState: ScoreUiState,
    onPreviousMonth: () -> Unit = {},
    onNextMonth: () -> Unit = {},
    onChartPeriodSelected: (String) -> Unit = {}
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column() {
                        Text(
                            text = "Estatísticas",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Seu foco, suas evoluções.",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Normal
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(
                            painter = painterResource(id = R.drawable.material_symbols_menu_rounded),
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            
            // 1. Destaque de Streak
            item {
                FullFocusHeroStreakCard(
                    streak = uiState.dailyStreak,
                    highestStreak = uiState.highestStreak,
                    streakFreezes = uiState.streakFreezes
                )
            }

            // 2. Estatísticas Principais (Bento Grid)
            item {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        FullFocusStatCard(
                            modifier = Modifier.weight(1f),
                            title = "Tempo de Foco",
                            value = uiState.totalHoursMonth,
                            iconRes = R.drawable.mingcute_time_line,
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        FullFocusStatCard(
                            modifier = Modifier.weight(1f),
                            title = "Pomodoros",
                            value = uiState.pomodorosCompleted.toString(),
                            iconRes = R.drawable.boxicons_timer,
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        FullFocusStatCard(
                            modifier = Modifier.weight(1f),
                            title = "Meta Semanal",
                            value = uiState.weeklyGoalProgress,
                            iconRes = R.drawable.ri_target_fill,
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                            contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                        FullFocusStatCard(
                            modifier = Modifier.weight(1f),
                            title = "Consistência",
                            value = "${uiState.consistencyRate}%",
                            iconRes = R.drawable.outline_bar_chart_24,
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            // 3. Atividade Mensal (Calendário)
            item {
                FullFocusMonthlyActivityCard(
                    monthLabel = uiState.currentMonthLabel,
                    calendarMonthName = uiState.calendarMonthName,
                    monthlyStrikeLabel = uiState.monthlyStrikeLabel,
                    daysFocused = uiState.monthlyGoalDays,
                    totalDays = uiState.monthlyGoalTotal,
                    onPreviousMonth = onPreviousMonth,
                    onNextMonth = onNextMonth,
                    calendarContent = {
                        FullFocusCalendarStrike(
                            showMonthHeader = false,
                            daySize = 36.dp
                        )
                    }
                )
            }

            // 4. Histórico Semanal
            item {
                FullFocusWeeklyChart(
                    activity = uiState.weeklyActivity,
                    selectedPeriod = uiState.selectedChartPeriod,
                    periodOptions = uiState.chartPeriodOptions,
                    onPeriodSelected = onChartPeriodSelected
                )
            }

            // 5. Conquistas
            item {
                FullFocusAchievementSection(
                    achievements = uiState.achievements
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ScoreScreenDarkPreview() {
    FullFocusTheme(darkTheme = true, dynamicColor = false) {
        ScoreScreenContent(
            uiState = ScoreUiState(
                totalHoursMonth = "48h 32m",
                focusSessionsCompleted = 386,
                pomodorosCompleted = 386,
                dailyStreak = 12,
                highestStreak = 21,
                streakFreezes = 1,
                weeklyGoalProgress = "20/35",
                monthlyGoalDays = 16,
                monthlyGoalTotal = 31,
                consistencyRate = 87,
                currentMonthLabel = "Maio 2025",
                weeklyActivity = listOf(
                    WeeklyHistoryData("7-13", "Abr", 0.6f),
                    WeeklyHistoryData("14-20", "Abr", 0.75f),
                    WeeklyHistoryData("21-27", "Abr", 0.85f),
                    WeeklyHistoryData("19-25", "Mai", 0.95f, "10h 45m")
                ),
                achievements = listOf(
                    AchievementUiState("1", "Primeira semana", "7 dias seguidos", R.drawable.fluent_emoji_flat_fire, true, 0xFFFF8C00),
                    AchievementUiState("2", "Um mês focado", "30 dias seguidos", R.drawable.mynaui_coffee, true, 0xFFE91E63)
                )
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ScoreScreenLightPreview() {
    FullFocusTheme(darkTheme = false, dynamicColor = false) {
        ScoreScreenContent(
            uiState = ScoreUiState(
                totalHoursMonth = "48h 32m",
                focusSessionsCompleted = 386,
                pomodorosCompleted = 386,
                dailyStreak = 12,
                highestStreak = 21,
                streakFreezes = 1,
                weeklyGoalProgress = "20/35",
                monthlyGoalDays = 16,
                monthlyGoalTotal = 31,
                consistencyRate = 87,
                currentMonthLabel = "Maio 2025",
                calendarMonthName = "Julho",
                monthlyStrikeLabel = "12 dias",
                selectedChartPeriod = "Por semana",
                weeklyActivity = listOf(
                    WeeklyHistoryData("Seg", "Abr", 0.4f),
                    WeeklyHistoryData("Ter", "Abr", 0.6f),
                    WeeklyHistoryData("Qua", "Abr", 0.85f),
                    WeeklyHistoryData("Qui", "Mai", 0.55f),
                    WeeklyHistoryData("Sex", "Mai", 0.65f),
                    WeeklyHistoryData("Sáb", "Mai", 0.75f),
                    WeeklyHistoryData("Dom", "Mai", 0.95f, "11h")
                ),
                achievements = listOf(
                    AchievementUiState("1", "Primeira semana", "7 dias seguidos", R.drawable.fluent_emoji_flat_fire, true, 0xFFFF8C00),
                    AchievementUiState("2", "Um mês focado", "30 dias seguidos", R.drawable.mynaui_coffee, true, 0xFFE91E63)
                )
            )
        )
    }
}
