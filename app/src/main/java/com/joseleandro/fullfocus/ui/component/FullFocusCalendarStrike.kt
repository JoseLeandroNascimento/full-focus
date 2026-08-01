package com.joseleandro.fullfocus.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLocale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joseleandro.fullfocus.R
import com.joseleandro.fullfocus.ui.theme.FullFocusTheme
import com.kizitonwose.calendar.compose.CalendarState
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.daysOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle

val daysOfWeek = daysOfWeek()

@Composable
fun FullFocusCalendarStrike(
    modifier: Modifier = Modifier,
    focusedDates: List<LocalDate> = emptyList(),
    currentMonth: YearMonth = YearMonth.now(),
    showMonthHeader: Boolean = true,
    daySize: Dp = 40.dp,
    onDayClick: (CalendarDay) -> Unit = {}
) {
    val state = rememberCalendarState(
        startMonth = currentMonth.minusMonths(12),
        endMonth = currentMonth.plusMonths(12),
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = daysOfWeek.first()
    )

    // Synchronize state when currentMonth changes from outside
    LaunchedEffect(currentMonth) {
        state.scrollToMonth(currentMonth)
    }

    HorizontalCalendar(
        modifier = modifier,
        state = state,
        monthHeader = { calendarMonth ->
            if (showMonthHeader) {
                MonthHeaderStriker(
                    calendarMonth = calendarMonth
                )
            }
        },
        dayContent = { calendarDay ->
            val today = LocalDate.now()
            val isToday = calendarDay.date == today
            val focused = focusedDates.contains(calendarDay.date)

            DayStrike(
                modifier = Modifier.size(daySize),
                calendarDay = calendarDay,
                focused = focused,
                isToday = isToday,
                onClick = { onDayClick(calendarDay) }
            )
        }
    )
}

@Composable
fun MonthHeaderStriker(
    modifier: Modifier = Modifier,
    calendarMonth: CalendarMonth
) {
    Column(
        modifier = modifier.padding(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = calendarMonth.yearMonth.month.getDisplayName(
                    TextStyle.FULL,
                    LocalLocale.current.platformLocale
                ).replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Surface(
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.clip(RoundedCornerShape(16.dp))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(R.drawable.fluent_emoji_flat_fire),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "12 dias",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            daysOfWeek.forEach { dayOfWeek ->
                Text(
                    modifier = Modifier.weight(1f),
                    text = dayOfWeek.getDisplayName(
                        TextStyle.SHORT, LocalLocale.current.platformLocale
                    ).take(1).uppercase(), // Apenas a primeira letra (S, T, Q...)
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.sp
                    ),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun DayStrike(
    modifier: Modifier = Modifier,
    calendarDay: CalendarDay,
    focused: Boolean = false,
    isToday: Boolean = false,
    onClick: () -> Unit = {}
) {
    val scale by animateFloatAsState(
        targetValue = if (focused) 1.15f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "scale"
    )

    val transitionAnim = updateTransition(targetState = focused, label = "focused")
    val background by transitionAnim.animateColor(label = "background") { isFocused ->
        if (isFocused) MaterialTheme.colorScheme.primary else Color.Transparent
    }

    val labelColor by transitionAnim.animateColor(label = "label") { isFocused ->
        if (isFocused) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
    }

    Box(
        modifier = modifier
            .scale(scale)
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier.size(28.dp),
            shape = RoundedCornerShape(8.dp),
            color = background,
            tonalElevation = if (focused) 2.dp else 0.dp
        ) {
            Box(contentAlignment = Alignment.Center) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = calendarDay.date.dayOfMonth.toString(),
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = labelColor,
                            fontWeight = if (focused || isToday) FontWeight.ExtraBold else FontWeight.Medium,
                            fontSize = 10.sp
                        )
                    )
                    
                    if (isToday && !focused) {
                        Box(
                            modifier = Modifier
                                .padding(top = 1.dp)
                                .size(2.dp)
                                .background(MaterialTheme.colorScheme.primary, CircleShape)
                        )
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = focused,
            enter = fadeIn() + scaleIn(initialScale = 0.5f),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(2.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.fluent_emoji_flat_fire),
                contentDescription = "Strike active",
                modifier = Modifier.size(12.dp)
            )
        }
    }
}

@Preview
@Composable
private fun DayStrikeLightPreview() {
    val state = rememberCalendarState()
    FullFocusTheme(dynamicColor = false, darkTheme = false) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            DayStrike(
                calendarDay = state.firstVisibleMonth.weekDays.first().first(),
                focused = true,
                isToday = true
            )
            DayStrike(
                calendarDay = state.firstVisibleMonth.weekDays.first().first(),
                focused = false,
                isToday = true
            )
        }
    }
}

@Preview
@Composable
private fun FullFocusCalendarStrikeLightPreview() {
    FullFocusTheme(
        dynamicColor = false, darkTheme = false
    ) {
        FullFocusCalendarStrike()
    }
}

@Preview
@Composable
private fun FullFocusCalendarStrikeDarkPreview() {
    FullFocusTheme(
        dynamicColor = false, darkTheme = true
    ) {
        FullFocusCalendarStrike()
    }
}