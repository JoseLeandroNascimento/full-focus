package com.joseleandro.fullfocus.ui.component

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLocale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joseleandro.fullfocus.R
import com.joseleandro.fullfocus.ui.theme.FullFocusTheme
import com.kizitonwose.calendar.compose.CalendarState
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.daysOfWeek
import java.time.format.TextStyle

val daysOfWeek = daysOfWeek()

@Composable
fun FullFocusCalendarStrike(
    modifier: Modifier = Modifier, state: CalendarState = rememberCalendarState()
) {

    HorizontalCalendar(modifier = modifier, state = state, monthHeader = { calendarMonth ->
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Atividade do mês"
            )
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
                        ),
                        style = MaterialTheme.typography.titleSmall,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }, dayContent = { calendarDay ->

        val focused = calendarDay.date.dayOfWeek == state.firstVisibleMonth.weekDays.first()
            .first().date.dayOfWeek

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            DayStrike(
                calendarDay = calendarDay, focused = focused
            )
        }
    })
}

@Composable
fun DayStrike(
    modifier: Modifier = Modifier, calendarDay: CalendarDay, focused: Boolean = false
) {

    val transitionAnim = updateTransition(targetState = focused, label = "focused")
    val background by transitionAnim.animateColor(label = "background") { focused ->
        when (focused) {
            true -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = .6f)
            false -> Color.Transparent
        }
    }

    val labelColor by transitionAnim.animateColor(label = "label") { focused ->
        when (focused) {
            true -> MaterialTheme.colorScheme.onSurface
            false -> MaterialTheme.colorScheme.onSurfaceVariant
        }
    }

    Box(
        modifier = modifier.size(40.dp), contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier.size(32.dp), shape = CircleShape, color = background
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = calendarDay.date.dayOfMonth.toString(),
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = labelColor,
                        fontWeight = if (focused) FontWeight.Bold else FontWeight.Normal,
                    )
                )
            }
        }

        if (focused) {
            Image(
                painter = painterResource(R.drawable.fluent_emoji_flat_fire),
                contentDescription = null,
                modifier = Modifier
                    .size(14.dp)
                    .align(Alignment.TopEnd)
            )
        }
    }
}

@Preview
@Composable
private fun DayStrikeLightPreview() {

    val state = rememberCalendarState()
    FullFocusTheme(
        dynamicColor = false, darkTheme = false
    ) {
        DayStrike(
            calendarDay = state.firstVisibleMonth.weekDays.first().first(),
            focused = true
        )
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