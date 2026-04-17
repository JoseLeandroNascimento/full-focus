package com.joseleandro.fullfocus.ui.screen.list_tasks.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
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
import com.joseleandro.fullfocus.domain.data.TaskDomain

@Composable
fun TaskCard(
    modifier: Modifier = Modifier,
    task: TaskDomain,
    onClick: () -> Unit
) {

    Surface(
        modifier = modifier.fillMaxWidth(),
        tonalElevation = .4.dp,
        shape = MaterialTheme.shapes.large,
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = .2f)
        ),
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 16.dp)
                .padding(start = 16.dp, end = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                modifier = Modifier.size(28.dp),
                painter = painterResource(id = R.drawable.boxicons_seal_check),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary.copy(alpha = .8f)
            )

            Text(
                modifier = Modifier.weight(1f),
                text = task.title,
                style = MaterialTheme.typography.titleSmall.copy(
                    color = MaterialTheme.colorScheme.onSurface
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "${task.progress}/${task.pomodoros}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurface.copy(
                            alpha = .4f
                        )
                    )
                )
                IconButton(
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary.copy(alpha = .9f),
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    onClick = onClick
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.round_play_arrow_24),
                        contentDescription = null
                    )
                }
            }

        }
    }
}