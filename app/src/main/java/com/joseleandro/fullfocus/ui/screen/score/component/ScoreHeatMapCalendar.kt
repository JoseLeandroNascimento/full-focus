package com.joseleandro.fullfocus.ui.screen.score.component

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLocale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import com.joseleandro.fullfocus.R
import com.joseleandro.fullfocus.domain.model.HeatMapDomain
import com.joseleandro.fullfocus.domain.model.HeatMapDataDomain
import com.joseleandro.fullfocus.ui.theme.FullFocusTheme
import com.kizitonwose.calendar.compose.HeatMapCalendar
import com.kizitonwose.calendar.compose.heatmapcalendar.HeatMapCalendarState
import com.kizitonwose.calendar.compose.heatmapcalendar.rememberHeatMapCalendarState
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle as JavaTextStyle

@Composable
fun heatMapColor(value: HeatMapDomain): Color {
    return when (value) {
        HeatMapDomain.HEAT_MAP_0 -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = .1f)
        HeatMapDomain.HEAT_MAP_1 -> Color(0xFFC6E48B)
        HeatMapDomain.HEAT_MAP_2 -> Color(0xFF7BC96F)
        HeatMapDomain.HEAT_MAP_3 -> Color(0xFF239A3B)
        else -> Color(0xFF196127)
    }
}

@Composable
fun ScoreHeatMapCalendar(
    modifier: Modifier = Modifier,
    state: HeatMapCalendarState,
    value: List<HeatMapDataDomain>,
    selectedDate: LocalDate?,
    onDateSelected: (LocalDate?) -> Unit,
) {

    val density = LocalDensity.current

    HeatMapCalendar(
        modifier = modifier.padding(horizontal = 16.dp),
        state = state,
        dayContent = { day, _ ->
            val data = value.find { it.date == day.date }
            val value = data?.heatMap ?: HeatMapDomain.HEAT_MAP_0
            val isSelected = selectedDate == day.date
            val minutes = data?.timeFocus ?: 0L

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
                            onDateSelected(if (isSelected) null else day.date)
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
                                text = stringResource(R.string.min, minutes),
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
                text = month.yearMonth.month.getDisplayName(JavaTextStyle.SHORT, locale)
                    .replace(".", ""),
                fontSize = 12.sp,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }
    )
}

@Preview(name = "Heat Map Calendar light")
@Preview(
    name = "Heat Map Calendar dark",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun ScoreHeatMapCalendarPreview() {

    val currentMonth = remember { YearMonth.now() }
    val today = remember { LocalDate.now() }

    val state = rememberHeatMapCalendarState(
        startMonth = currentMonth.withMonth(1),
        endMonth = currentMonth.withMonth(12),
        firstDayOfWeek = firstDayOfWeekFromLocale(),
        firstVisibleMonth = currentMonth
    )

    val data = remember {
        listOf(
            HeatMapDataDomain(today, 10L, HeatMapDomain.HEAT_MAP_1),
            HeatMapDataDomain(today.minusDays(1), 25L, HeatMapDomain.HEAT_MAP_2),
            HeatMapDataDomain(today.minusDays(2), 45L, HeatMapDomain.HEAT_MAP_3),
            HeatMapDataDomain(today.minusDays(3), 60L, HeatMapDomain.HEAT_MAP_4),
            HeatMapDataDomain(today.minusDays(5), 30L, HeatMapDomain.HEAT_MAP_2),
        )
    }

    FullFocusTheme(
        dynamicColor = false,
    ) {
        Surface {
            ScoreHeatMapCalendar(
                state = state,
                value = data,
                selectedDate = today,
                onDateSelected = {}
            )
        }
    }
}