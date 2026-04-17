package com.joseleandro.fullfocus.ui.screen.pomodoro.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.joseleandro.fullfocus.R

@Composable
fun SelectedTaskCard(
    modifier: Modifier = Modifier,
    taskTitle: String,
    onResetTask: () -> Unit
) {
    Surface(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = .5f),
        tonalElevation = 6.dp,
        shape = MaterialTheme.shapes.large
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.secondary.copy(.1f),
                        shape = MaterialTheme.shapes.large
                    )
                    .padding(8.dp)
                    .size(28.dp),
                painter = painterResource(id = R.drawable.boxicons_seal_check),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary
            )

            Text(
                modifier = Modifier.weight(1f),
                text = taskTitle,
                style = MaterialTheme.typography.titleSmall,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )

            IconButton(
                onClick = onResetTask
            ) {
                Icon(
                    modifier = Modifier.size(20.dp),
                    painter = painterResource(id = R.drawable.baseline_close_24),
                    contentDescription = null
                )
            }
        }
    }
}
